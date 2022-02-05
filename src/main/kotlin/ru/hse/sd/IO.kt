package ru.hse.sd;

import java.io.InputStream
import java.io.OutputStream

/** Container of input, output and other streams */
data class IO(val inputStream: InputStream, val outputStream: OutputStream, val errorStream: OutputStream)
