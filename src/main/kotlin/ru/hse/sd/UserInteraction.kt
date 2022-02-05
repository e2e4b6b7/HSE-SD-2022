package ru.hse.sd

import java.io.InputStream
import java.io.OutputStream

class UserInteraction(private val inputStream: InputStream = System.`in`,
                      private val outputStream: OutputStream = System.out) {
    companion object {
        private const val COMMAND_LINE_PREFIX = ">>> "
    }

    fun read(): String {
        outputStream.write(COMMAND_LINE_PREFIX)
        return inputStream.readBytes().toString()
    }
}
