package ru.hse.sd.cmd

import ru.hse.sd.*
import java.nio.charset.StandardCharsets
import java.util.regex.PatternSyntaxException

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
        val grepArgs = try {
            parse(args.toTypedArray())
        } catch (e: Exception) {
            io.errorStream.write("Error in parsing arguments: ${e.message}")
            return ReturnCode.success
        }

        val pattern = try {
            grepArgs.pattern.toRegex(grepArgs.regexOptions())
        } catch (e: PatternSyntaxException) {
            io.errorStream.write("Error in regular exception: ${e.message}")
            return ReturnCode.success
        }

        val inputLines = if (grepArgs.filePath != null) {
            checkedFile(grepArgs.filePath, io.errorStream::write)?.readLines() ?: listOf()
        } else {
            String(io.inputStream.readAllBytes(), StandardCharsets.UTF_8).lines()
        }

        var lastLineForPrintIndex = -1
        for ((i, line) in inputLines.withIndex()) {
            val matches = pattern.findAll(line)
            if (grepArgs.wholeWord) {
                for (match in matches) {
                    val prevCharacter = line.getOrNull(match.range.first - 1)
                    val nextCharacter = line.getOrNull(match.range.last + 1)
                    if (prevCharacter?.isNonWordConstituent() != false && nextCharacter?.isNonWordConstituent() != false) {
                        lastLineForPrintIndex = maxOf(lastLineForPrintIndex, i + grepArgs.numOfLinesAfterMatched)
                        break
                    }
                }
            } else if (matches.any()) {
                lastLineForPrintIndex = maxOf(lastLineForPrintIndex, i + grepArgs.numOfLinesAfterMatched)
            }
            if (i <= lastLineForPrintIndex)
                io.outputStream.write("$line\n")
        }

        return ReturnCode.success
    }

    private fun GrepArguments.regexOptions(): Set<RegexOption> =
        mutableSetOf<RegexOption>().apply {
            if (ignoreCase) add(RegexOption.IGNORE_CASE)
        }

    private fun Char.isNonWordConstituent() = !isLetterOrDigit() && this != '_'
}
