package ru.hse.sd.cmd

import ru.hse.sd.IO

object Echo : Command {
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        io.outputStream.write(args.joinToString(separator = " ").toByteArray())
        return ReturnCode.success
    }
}
