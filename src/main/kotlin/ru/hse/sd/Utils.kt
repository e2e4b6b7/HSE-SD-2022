package ru.hse.sd

import java.io.OutputStream

fun List<String>.merge() = this.joinToString("")
fun OutputStream.write(str: String) = write(str.toByteArray())
