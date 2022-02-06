package ru.hse.sd.parser

/**
 * Data class representing single `SShell` statement.
 * Where statement is the list of piped tasks.
 */
data class Statement(val tasks: List<Task>)
