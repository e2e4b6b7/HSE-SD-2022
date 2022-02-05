package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write
import java.nio.file.Path

object Pwd : Command {
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        io.outputStream.write(Path.of("").toAbsolutePath().toString())
        io.outputStream.write('\n'.code)
        return ReturnCode.success
    }
}
