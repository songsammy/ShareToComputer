/*
    This file is part of Share To Computer  Copyright (C) 2019  Jimmy <https://github.com/jimmod/ShareToComputer>.

    Share To Computer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Share To Computer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Share To Computer.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.jim.sharetocomputer.webserver

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class WebServerTextTest {

    private lateinit var webServer: WebServerText

    @Before
    fun setup() {
        webServer = WebServerText(TEST_PORT)
        webServer.start()
    }

    @After
    fun end() {
        webServer.stop()
    }

    @Test
    fun default_body() {
        val (code, _) = httpGet(TEST_URL)

        Assert.assertEquals(404, code)
    }

    @Test
    fun check_response_body() {
        webServer.setText(SAMPLE_TEXT)

        val (code, body) = httpGet(TEST_URL)

        Assert.assertEquals(200, code)
        Assert.assertEquals(SAMPLE_TEXT, body)
    }

    private fun httpGet(url: String): Pair<Int, String> {
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        val code = con.responseCode
        val sb = StringBuilder()
        try {
            BufferedReader(InputStreamReader(con.inputStream)).useLines { lines ->
                lines.forEach { line ->
                    sb.append(line)
                }
            }
        } catch (e: Exception) {}
        val body = sb.toString()
        return Pair(code, body)
    }

    companion object {
        private const val TEST_PORT = 8080
        private const val TEST_URL = "http://localhost:$TEST_PORT"

        private const val SAMPLE_TEXT = "Hello World"

    }

}