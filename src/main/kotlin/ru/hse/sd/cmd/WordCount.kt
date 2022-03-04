package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.write
import java.nio.charset.StandardCharsets

/** `wc` CLI command implementation */
object WordCount : Command {
    /**
     * Executes `wc` CLI command.
     * Print word, line, character, and byte count of content to output stream from [io].
     * Content may be the file, if filename is in the [args] or content from [io] input stream otherwise.
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        val str =
            if (args.isNotEmpty()) {
                val file = checkedFile(args[0], io.errorStream::write) ?: return ReturnCode(1)
                String(file.readBytes(), StandardCharsets.UTF_8)
            } else {
                String(io.inputStream.readAllBytes(), StandardCharsets.UTF_8)
            }
        val counts = count(str)
        io.outputStream.write("\t\t${counts.lines}\t\t${counts.words}\t\t${counts.symbols}")
        io.outputStream.write('\n'.code)
        return ReturnCode.success
    }

    private data class Counts(val lines: Int, val words: Int, val symbols: Int)

    private fun count(str: String): Counts {
        val lines = str.splitToSequence('\n').count()
        val words = str.splitToSequence(whitespaceRegex).filter { it.isNotEmpty() }.count()
        return Counts(lines, words, str.length)
    }

    private val whitespaceRegex = Regex("\\s")
}
