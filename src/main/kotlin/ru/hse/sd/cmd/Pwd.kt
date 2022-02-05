package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write

object Pwd : Command {
    override fun eval(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        // TODO validate function for arguments in all classes that implements Command (?)
        if (args.isNotEmpty()) {
            io.errorStream.write("pwd: too many arguments")
            return ReturnCode(1)
        }
        io.outputStream.write(System.getProperty("user.dir"))
        return ReturnCode.success
    }
}
