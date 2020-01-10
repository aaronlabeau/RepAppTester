package com.refapp.tester.services

import android.icu.util.Output
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.regex.Matcher
import java.util.regex.Pattern

import java.lang.Runtime.getRuntime

class NetworkTraceRouteService {

    class TraceRoute private constructor(
        address: String,
        output: StringBuilder,
        complete: Callback
    ) : Task {
        private val address: String
        private val output: StringBuilder
        private val complete: Callback
        @Volatile
        private var stopped = false
        lateinit var result: Result

        init {
            this.address = address
            this.output = output
            this.complete = complete
        }

        override fun stop() {
            stopped = true
        }

        @Throws(IOException::class)
        private fun executePingCmd(host: String, hop: Int): Process {
            val command = "ping -n -c 1 -t $hop $host"
            // System.out.println("cmd> " + command);
            return getRuntime().exec(command)
        }

        private fun getPingtOutput(process: Process): String {
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String
            val text = StringBuilder()
            try {
                line = reader.readLine()
                while (line.isNotEmpty()) {
                    text.append(line)
                    line = reader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            try {
                process.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            process.destroy()
            return text.toString()
        }

        private fun printNormal(m: Matcher, time: Long, lineBuffer: StringBuilder) {
            val pingIp = getIpFromTraceMatcher(m)
            lineBuffer.append("\t")
            lineBuffer.append(pingIp)
            lineBuffer.append("\t\t")
            lineBuffer.append(time)
            lineBuffer.append("ms\t")
            output.append(lineBuffer.toString())
            result.append(lineBuffer.toString())
        }

        private fun printEnd(m: Matcher, out: String, lineBuffer: StringBuilder) {
            val pingIp = m.group()
            val matcherTime = timeMatcher(out)
            if (matcherTime.find()) {
                val time = matcherTime.group()
                lineBuffer.append("\t\t")
                lineBuffer.append(pingIp)
                lineBuffer.append("\t\t")
                lineBuffer.append(time)
                lineBuffer.append("\t")
                updateOut(lineBuffer.toString())
            }
        }

        private fun updateOut(str: String) {
            if (str.isNotEmpty()) {
                output.append(str)
            }
            result.append(str)
        }

        private fun run() {
            var hop = 1
            val ip: String
            try {
                ip = getIp(this.address)
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                updateOut("unknown host " + this.address)
                result = Result("")
                this.complete.complete(result)
                return
            }
            result = Result(ip)
            var p: Process
            while (hop < MaxHop && !stopped) {
                val t1 = System.currentTimeMillis()
                try {
                    p = executePingCmd(ip, hop)
                } catch (e: IOException) {
                    e.printStackTrace()
                    updateOut("ping cmd error $e.message")
                    break
                }
                val t2 = System.currentTimeMillis()
                val str = getPingtOutput(p)
                if (str.length == 0) {
                    updateOut(Error)
                    break
                }
                val m = traceMatcher(str)
                val lineBuffer = StringBuilder(256)
                lineBuffer.append(hop).append(".")
                if (m.find()) {
                    printNormal(m, (t2 - t1) / 2, lineBuffer)
                } else {
                    val matchPingIp = ipMatcher(str)
                    if (matchPingIp.find()) {
                        printEnd(matchPingIp, str, lineBuffer)
                        break
                    } else {
                        lineBuffer.append("\t\t * \t")
                        updateOut(lineBuffer.toString())
                    }
                }
                hop++
            }
            this.complete.complete(result)
        }

        interface Callback {
            fun complete(r: Result)
        }

        class Result(ip: String) {
            val ip: String
            private val builder = StringBuilder()
            private var allData = ""

            init {
                this.ip = ip
            }

            fun content(): String {
                if (allData.isNotEmpty()) {
                    return allData
                }
                allData = builder.toString()
                return allData
            }

            fun append(str: String) {
                builder.append(str)
            }
        }

        companion object {
            private val MaxHop = 31
            private val Error = "network error"
            private val MATCH_TRACE_IP = "(?<=From )(?:[0-9]{1,3}\\.){3}[0-9]{1,3}"
            private val MATCH_PING_IP = "(?<=from ).*(?=: icmp_seq=1 ttl=)"
            private val MATCH_PING_TIME = "(?<=time=).*?ms"
            internal fun traceMatcher(str: String): Matcher {
                val patternTrace = Pattern.compile(MATCH_TRACE_IP)
                return patternTrace.matcher(str)
            }

            internal fun timeMatcher(str: String): Matcher {
                val patternTime = Pattern.compile(MATCH_PING_TIME)
                return patternTime.matcher(str)
            }

            internal fun ipMatcher(str: String): Matcher {
                val patternIp = Pattern.compile(MATCH_PING_IP)
                return patternIp.matcher(str)
            }

            internal fun getIpFromTraceMatcher(m: Matcher): String {
                var pingIp = m.group()
                val start = pingIp.indexOf('(')
                if (start >= 0) {
                    pingIp = pingIp.substring(start + 1)
                }
                return pingIp
            }

            fun start(address: String, output: StringBuilder, complete: Callback): Task {
                val t = TraceRoute(address, output, complete)
                Thread(object : Runnable {
                    public override fun run() {
                        t.run()
                    }
                }).start()
                return t
            }

            @Throws(UnknownHostException::class)
            private fun getIp(host: String): String {
                val i = InetAddress.getByName(host)
                return i.getHostAddress()
            }
        }
    }

    interface Task {
        fun stop() {}
    }
}