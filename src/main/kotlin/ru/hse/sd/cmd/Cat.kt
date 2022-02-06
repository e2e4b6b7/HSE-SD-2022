package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write
import java.io.File
import java.nio.file.Path


object Cat : Command {
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        if (args.isNotEmpty()) {
            val file = checkedFile(args[0], io.errorStream::write) ?: return ReturnCode(1)
            io.outputStream.write(file.readBytes())
            return ReturnCode.success
        }
        io.outputStream.write(io.inputStream.readAllBytes())
        return ReturnCode.success
    }
}
