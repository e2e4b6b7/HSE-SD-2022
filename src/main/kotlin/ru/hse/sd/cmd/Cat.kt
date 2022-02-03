package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write

object Cat: Command {
    override fun eval(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        if (args.size != 1) {
            io.errorStream.write("Cat ")
            return ReturnCode(1)
        }
        // TODO
        args.forEach { io.outputStream.write(it.toByteArray()) }
        return ReturnCode(0)
    }
}
