package com.cosync.cosyncjwt

import org.junit.Assert.assertEquals
import org.junit.Test
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
	@Test
	fun testMD5() {
		fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
		fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

		val md5Hex = md5("hello world").toHex()
		assertEquals(md5Hex, "5eb63bbbe01eeed093cb22bb8f5acdc3")
	}
}
