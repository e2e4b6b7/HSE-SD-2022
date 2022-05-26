package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.write
import java.nio.file.Path

/** `pwd` CLI command implementation */
object Pwd : Command {
    /**
     * Executes `pwd` CLI command.
     * Write working directory name to output from [io].
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        io.outputStream.write(env.getWorkingDirectory().toString())
        io.outputStream.write('\n'.code)
        return ReturnCode.success
    }
}
