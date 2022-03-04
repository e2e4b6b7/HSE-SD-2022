package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment

/** `echo` CLI command implementation */
object Echo : Command {
    /**
     * Executes `echo` CLI command.
     * Write all [args], separated by space, into output stream from [io] and returns success code.
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        io.outputStream.write(args.joinToString(separator = " ").toByteArray())
        io.outputStream.write('\n'.code)
        return ReturnCode.success
    }
}
