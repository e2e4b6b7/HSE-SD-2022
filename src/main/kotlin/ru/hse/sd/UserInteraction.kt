package ru.hse.sd

import java.io.InputStream
import java.io.OutputStream

class UserInteraction(
    inputStream: InputStream,
    private val outputStream: OutputStream
) {
    private val inputStream = inputStream.bufferedReader()

    fun read(): String {
        outputStream.write(COMMAND_LINE_PREFIX)
        return inputStream.readLine()
    }

    companion object {
        private const val COMMAND_LINE_PREFIX = ">>> "
    }
}
