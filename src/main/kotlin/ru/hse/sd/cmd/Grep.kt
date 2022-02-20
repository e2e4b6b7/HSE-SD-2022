package ru.hse.sd.cmd

import ru.hse.sd.IO

/** `grep` CLI command implementation */
object Grep : Command {
    /**
     * Executes `grep` CLI command.
     * Prints to output from [io] lines containing at least one matching a pattern
     * First argument -- pattern specified by the regular expression
     * Second argument -- input file. If file isn't provided, input from [io] is used.
     *
     * Available options:
     * -w -- search the whole word (substring separated by non-word constituent characters)
     * -i -- case-insensitive search
     * -A <number> -- print <number> lines after every matched line.
     */
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        TODO("Unsupported operation")
    }

    private data class GrepArguments(
        val pattern: String,
        val filePath: String?,
        val wholeWord: Boolean = false,
        val ignoreCase: Boolean = false,
        val numOfLinesAfterMatched: Int = 0
        )
}
