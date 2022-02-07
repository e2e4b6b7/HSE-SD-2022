package ru.hse.sd

import java.io.OutputStream

/** Merge list of [String] in one [String] */
fun List<String>.merge() = joinToString("")

/** Write [String] to the [OutputStream] */
fun OutputStream.write(str: String) = write(str.toByteArray())
