package com.refapp.tester.services

import java.io.BufferedReader
import java.io.InputStreamReader

class ShellCommandService () {

    fun getShellCommandResults(command: String, options: Array<String> = emptyArray() ) : String {
        val output = StringBuffer()
        val runtime = Runtime.getRuntime()

        try {
            val p = if (options.isNotEmpty()) {
                runtime.exec(command, options)
            } else {
                runtime.exec(command)
            }
            p.waitFor()
            val reader = BufferedReader(InputStreamReader(p.inputStream))
            var line = reader.readLine()
            while (line != null) {
                output.append("$line\n\n")
                line = reader.readLine()
            }

        }catch(e : Exception) {
            e.printStackTrace()
        }
       return output.toString()
    }
}