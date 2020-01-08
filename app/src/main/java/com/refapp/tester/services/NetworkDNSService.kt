package com.refapp.tester.services

import android.annotation.TargetApi
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.os.Build
import android.util.Log
import com.refapp.tester.models.StringResult
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.net.InetAddress

class NetworkDNSService(val context:Context) {
    private val TAG = "DnsServersDetector"
    private val METHOD_EXEC_PROP_DELIM = "]: ["

    fun getNslookupResult(hostname: String) : StringResult {
        val strResult :StringResult
        strResult = try {
            val result = InetAddress.getAllByName(hostname)
            StringResult(result[0].hostAddress, false, "")
        } catch (e: Exception) {
            val message =" $hostname locked up lookup with error ${e.message}"
            Log.d("NetworkDNSService", message )
            StringResult("", true, message)
        }
        return strResult
    }

    /**
     * Returns android DNS servers used for current connected network
     * @return Dns servers array
     */
    fun getServers():Array<String> {
        // METHOD 1: old deprecated system properties
        var result:Array<String> = getServersMethodSystemProperties()
        if (result.isNotEmpty()) {
            return result
        }
        // METHOD 2 - use connectivity manager
        result = getServersMethodConnectivityManager()
        if (result.isNotEmpty()) {
            return result
        }
        // LAST METHOD: detect android DNS servers by executing getprop string command in a separate process
        // This method fortunately works in Oreo too but many people may want to avoid exec
        // so it's used only as a failsafe scenario
        result = getServersMethodExec()
        if (result.isNotEmpty()) {
            return result
        }
        //no results found
        var noResults = ArrayList<String>()
        noResults.add("No DNS Configuration Found")
        result = noResults.toTypedArray()

        //Failed to return any DNS servers
        return result
    }

    /**
     * Detect android DNS servers by using connectivity manager
     * This method is working in android LOLLIPOP or later
     * @return Dns servers array
     */
    private fun getServersMethodConnectivityManager():Array<String> {
        // This code only works on LOLLIPOP and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                val priorityServersArrayList = ArrayList<String>()
                val serversArrayList = ArrayList<String>()
                val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivityManager != null)
                {
                    // Iterate all networks
                    // Notice that android LOLLIPOP or higher allow iterating multiple connected networks of SAME type
                    for (network in connectivityManager.allNetworks) {
                        val networkInfo = connectivityManager.getNetworkInfo(network)
                        if (networkInfo.isConnected())
                        {
                            val linkProperties = connectivityManager.getLinkProperties(network)
                            val dnsServersList = linkProperties.dnsServers
                            // Prioritize the DNS servers for link which have a default route
                            if (linkPropertiesHasDefaultRoute(linkProperties)) {
                                for (element in dnsServersList) {
                                    val dnsHost = element.getHostAddress()
                                    priorityServersArrayList.add(dnsHost)
                                }
                            } else {
                                for (element in dnsServersList) {
                                    val dnsHost = element.hostAddress
                                    serversArrayList.add(dnsHost)
                                }
                            }
                        }
                    }
                }
                // Append secondary arrays only if priority is empty
                if (priorityServersArrayList.isEmpty()) {
                    priorityServersArrayList.addAll(serversArrayList)
                }
                // Stop here if we have at least one DNS server
                if (priorityServersArrayList.isNotEmpty()) {
                    return priorityServersArrayList.toArray(arrayOfNulls<String>(0))
                }
            }
            catch (ex:Exception) {
                Log.d(TAG, "Exception detecting DNS servers using ConnectivityManager method", ex)
            }
        }
        // Failure
        return emptyArray()
    }

    /**
     * Detect android DNS servers by using old deprecated system properties
     * This method is NOT working anymore in Android 8.0
     * Official Android documentation state this in the article Android 8.0 Behavior Changes.
     * The system properties net.dns1, net.dns2, net.dns3, and net.dns4 are no longer available,
     * a change that improves privacy on the platform.
     * https://developer.android.com/about/versions/oreo/android-8.0-changes.html#o-pri
     * @return Dns servers array
     */
    private fun getServersMethodSystemProperties():Array<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            // This originally looked for all lines containing .dns;
            // but http://code.google.com/p/android/issues/detail?id=2207#c73
            // indicates that net.dns* should always be the active nameservers, so we use those.
            val re1 = "^\\d+(\\.\\d+){3}$"
            val re2 = "^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$"
            val serversArrayList = ArrayList<String>()
            try
            {
                val SystemProperties = Class.forName("android.os.SystemProperties")
                val method = SystemProperties.getMethod("get", *arrayOf<Class<*>>(String::class.java))
                val netdns = arrayOf<String>("net.dns1", "net.dns2", "net.dns3", "net.dns4")
                for (i in netdns.indices) {
                    val args = arrayOf<Any>(netdns[i])
                    val v = method.invoke(null, args) as String
                    if (v != null && (v.matches((re1).toRegex()) || v.matches((re2).toRegex())) && !serversArrayList.contains(v)) {
                        serversArrayList.add(v)
                    }
                }
                // Stop here if we have at least one DNS server
                if (serversArrayList.isNotEmpty()) {
                    return serversArrayList.toArray(arrayOfNulls<String>(0))
                }
            }
            catch (ex:Exception) {
                Log.d(TAG, "Exception detecting DNS servers using SystemProperties method", ex)
            }
        }
        // Failed
        return emptyArray()
    }

    /**
     * Detect android DNS servers by executing getprop string command in a separate process
     * Notice there is an android bug when Runtime.exec() hangs without providing a Process object.
     * This problem is fixed in Jelly Bean (Android 4.1) but not in ICS (4.0.4) and probably it will never be fixed in ICS.
     * https://stackoverflow.com/questions/8688382/runtime-exec-bug-hangs-without-providing-a-process-object/11362081
     *
     * @return Dns servers array
     */
    private fun getServersMethodExec():Array<String> {
        // We are on the safe side and avoid any bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            try {
                val process = Runtime.getRuntime().exec("getprop")
                val inputStream = process.getInputStream()
                val lineNumberReader = LineNumberReader(InputStreamReader(inputStream))
                val serversSet = methodExecParseProps(lineNumberReader)
                if (serversSet != null && serversSet.isNotEmpty()) {
                    return serversSet.toTypedArray()
                }
            }
            catch (ex:Exception) {
                Log.d(TAG, "Exception in getServersMethodExec", ex)
            }
        }
        // Failed
        return emptyArray()
    }
    /**
     * Parse properties produced by executing getprop command
     * @param lineNumberReader
     * @return Set of parsed properties
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun methodExecParseProps(lineNumberReader: BufferedReader):Set<String> {
        var line:String = lineNumberReader.readLine()
        val serversSet = HashSet<String>(10)

        while (line  != null) {
            val split = line.indexOf(METHOD_EXEC_PROP_DELIM)
            if (split == -1) {
                continue
            }
            val property = line.substring(1, split)
            val valueStart = split + METHOD_EXEC_PROP_DELIM.length
            val valueEnd = line.length - 1
            if (valueEnd < valueStart) {
                // This can happen if a newline sneaks in as the first character of the property value. For example
                // "[propName]: [\n…]".
                Log.d(TAG, "Malformed property detected: \"$line)")
                continue
            }
            var value = line.substring(valueStart, valueEnd)
            if (value.isEmpty()) {
                continue
            }
            if ((property.endsWith(".dns") ||
                        property.endsWith(".dns1") ||
                        property.endsWith(".dns2") ||
                        property.endsWith(".dns3") ||
                        property.endsWith(".dns4"))) {
                // normalize the address
                val ip = InetAddress.getByName(value)
                if (ip != null) {
                    value = ip.hostAddress
                    if (value == null || value.isEmpty()) continue
                    serversSet.add(value)
                }
            }
            line = lineNumberReader.readLine()
        }
        return serversSet
    }

    /**
     * Returns true if the specified link properties have any default route
     * @param linkProperties
     * @return true if the specified link properties have default route or false otherwise
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun linkPropertiesHasDefaultRoute(linkProperties: LinkProperties ) : Boolean {
        var result = false
        for(route in linkProperties.routes){
            if (route.isDefaultRoute) {
                result = true
            }
        }
        return result
    }
}