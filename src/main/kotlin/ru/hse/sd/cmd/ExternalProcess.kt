package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment
import java.nio.file.Path

/** External command implementation */
class ExternalProcess(
    /** Path to executable file for this process */
    private val executable: Path
) : Command {
    /**
     * Executes external process
     * Get all arguments from [args], environment variable from [env] and pass it to the external process
     * Transfer input, output and error stream from [io] to process streams
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        val process = ProcessBuilder().apply {
            command(env.getWorkingDirectory().toString())
            command().addAll(args)
            environment().putAll(env.mapView)
        }.start()
        io.inputStream.transferTo(process.outputStream)
        process.waitFor()
        process.inputStream.transferTo(io.outputStream)
        process.errorStream.transferTo(io.errorStream)
        return ReturnCode(process.exitValue())
    }
}
