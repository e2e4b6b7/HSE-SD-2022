package ru.hse.sd.parser

import ru.hse.sd.parser.antlr.SShellParse

/**
 * Type of parsed data from single `SShell` statement before substitutions.
 */
typealias PreStatement = SShellParse.StmtContext

/**
 * Data class representing single `SShell` statement.
 * Where statement is the list of piped tasks.
 */
data class Statement(val tasks: List<Task>)
