package ru.hse.sd.cmd

import ru.hse.sd.IO

object Echo : Command {
    override fun eval(args: List<String>, io: IO): CommandResult {
        args.forEach{io.outputStream.write(it.toByteArray())}
        return ReturnCode(0)
    }
}
