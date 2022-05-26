package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.write
import java.nio.file.Path

/** `cd` CLI command implementation */
object Cd : Command {
    /**
     * Executes `cd` CLI command.
     * If 0 [args], then return the initial working directory.
     * If 1 [args], then change the current working directory depending on the passed argument.
     * If such a directory or file does not exist or more than one [args] is passed, then return an error.
     * Get and change the current working directory from [env].
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        if (args.isEmpty()) {
            env.resetWorkingDirectory()
        } else if (args.size == 1) {
            val path = Path.of(args[0])
            env.changeWorkingDirectory(path, io.errorStream::write) ?: return ReturnCode(1)
        } else {
            io.errorStream.write("Too many arguments\n")
            return ReturnCode(1)
        }

        io.outputStream.write(env.getWorkingDirectory().toString())
        io.outputStream.write('\n'.code)

        return ReturnCode.success
    }
}
