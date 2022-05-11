package ru.hse.sd.cmd

import kotlinx.cli.*

/**
 * Data class representing arguments for [Grep] command.
 */
data class GrepArguments(
    /** Pattern specified by the regular expression*/
    val pattern: String,
    /** Input file. If file isn't provided, input from io stream is used */
    val filePath: String?,
    /** Search the whole word (substring separated by non-word constituent characters) */
    val wholeWord: Boolean,
    /** Case-insensitive search */
    val ignoreCase: Boolean,
    /** How many lines need to print after every matched line */
    val numOfLinesAfterMatched: Int
)

/**
 * Parses GrepArguments from input.
 */
fun parse(input: Array<String>): GrepArguments {
    val parser = ArgParser("Grep")
    val pattern by parser
        .argument(ArgType.String)
    val file by parser
        .argument(ArgType.String)
        .optional()
    val fullLine by parser
        .option(ArgType.Boolean, shortName = "w", description = "Full-line search")
        .default(false)
    val ignoreCase by parser
        .option(ArgType.Boolean, shortName = "i", description = "Case-insensitive search")
        .default(false)
    val numOfLinesAfterMatched by parser
        .option(ArgType.Int, shortName = "A", description = "Count of lines after match to print")
        .default(0)
    parser.parse(input)
    require(numOfLinesAfterMatched >= 0)
    return GrepArguments(pattern, file, fullLine, ignoreCase, numOfLinesAfterMatched)
}
