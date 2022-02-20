package ru.hse.sd.cmd

import kotlinx.cli.*

data class GrepArguments(
    val pattern: String,
    val filePath: String?,
    val wholeWord: Boolean,
    val ignoreCase: Boolean,
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
    return GrepArguments(pattern, file, fullLine, ignoreCase, numOfLinesAfterMatched)
}
