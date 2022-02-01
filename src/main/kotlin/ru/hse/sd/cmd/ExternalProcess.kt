package ru.hse.sd.cmd

import ru.hse.sd.IO
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.channels.Pipe
import java.nio.file.Path

class ExternalProcess(private val executable: Path) : Command {
    override fun eval(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        val process = ProcessBuilder().apply {
            command(executable.toAbsolutePath().toString())
            command().addAll(args)
            environment().putAll(env)
        }.start()
        io.inputStream.transferTo(process.outputStream)
        process.waitFor()
        process.inputStream.transferTo(io.outputStream)
        process.errorStream.transferTo(io.errorStream)
        return ReturnCode(process.exitValue())
    }
}
