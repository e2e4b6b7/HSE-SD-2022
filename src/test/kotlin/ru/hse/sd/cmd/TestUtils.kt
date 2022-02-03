package ru.hse.sd.cmd

import ru.hse.sd.IO
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

fun IO.checkStreams(outputStreamString: String, errorStreamString: String = "") {
    assertEquals(outputStreamString, outputStream.toString())
    assertEquals(errorStreamString, errorStream.toString())
}

fun IO(inputStreamString: String) =
    IO(ByteArrayInputStream(inputStreamString.toByteArray()), ByteArrayOutputStream(), ByteArrayOutputStream())
