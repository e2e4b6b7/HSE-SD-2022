package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write
import java.nio.charset.StandardCharsets

object WordCount : Command {
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        val str =
            if (args.isNotEmpty()) {
                val file = checkedFile(args[0], io.errorStream::write) ?: return ReturnCode(1)
                String(file.readBytes(), StandardCharsets.UTF_8)
            } else {
                String(io.inputStream.readAllBytes(), StandardCharsets.UTF_8)
            }
        val counts = count(str)
        io.outputStream.write("\t\t${counts.lines}\t\t${counts.words}\t\t${counts.symbols}")
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
