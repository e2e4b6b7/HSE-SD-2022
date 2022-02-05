package ru.hse.sd

import java.io.InputStream
import java.io.OutputStream

/** Class, which interacts with user */
class UserInteraction(
    /** input stream, where user writes his commands */
    inputStream: InputStream,
    /** output stream, where user interaction takes place: printing greeting, CLI prefix etc */
    private val outputStream: OutputStream
) {
    private val inputStream = inputStream.bufferedReader()

    /** Prints CLI prefix and reads string */
    fun read(): String {
        outputStream.write(COMMAND_LINE_PREFIX)
        return inputStream.readLine()
    }

    companion object {
        private const val COMMAND_LINE_PREFIX = ">>> "
    }
}
