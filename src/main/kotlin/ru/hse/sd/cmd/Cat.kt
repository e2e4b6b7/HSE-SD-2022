package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write
import java.nio.file.Path

object Cat: Command {
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        if (args.isNotEmpty()) {
            val fileName = args[0]
            val file = Path.of(fileName).toFile()
            if (!file.exists()) {
                io.errorStream.write("No such file $fileName")
                return ReturnCode(1)
            }
            if (!file.isFile) {
                io.errorStream.write("$fileName is not a file")
                return ReturnCode(1)
            }
            io.outputStream.write(file.readBytes())
            return ReturnCode.success
        }
        io.outputStream.write(io.inputStream.readAllBytes())
        return ReturnCode.success
    }
}
