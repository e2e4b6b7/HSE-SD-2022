package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.write

/** `cat` CLI command implementation */
object Cat : Command {
    /**
     * Executes `cat` CLI command.
     * Write content of file with the name given in [args] to output from [io].
     * If the file is not provided, then input from [io] is used instead.
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        if (args.isNotEmpty()) {
            val filePath = env.getWorkingDirectory().resolve(args[0])
            val file = checkedFile(filePath.toString(), io.errorStream::write) ?: return ReturnCode(1)
            io.outputStream.write(file.readBytes())
            return ReturnCode.success
        }
        io.outputStream.write(io.inputStream.readAllBytes())
        return ReturnCode.success
    }
}
