package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.write
import java.io.File
import java.nio.file.*
import java.util.*


/** `ls` CLI command implementation */
object Ls : Command {
    /**
     * Executes `ls` CLI command.
     * If there is no arg, then it prints names of files in the current directory alphabetical order.
     * If arg is a directory, then it prints names of files in this directory in alphabetical order.
     * If arg is not a directory, then it prints an arg.
     * If there are more than 1 arg, then return an error.
     * If there is no such file or directory, then it prints "ls: cannot access 'arg': No such file or directory" and return an error.
     */
    override fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult {
        val path: Path = if (args.isEmpty()) {
            env.getWorkingDirectory()
        } else if (args.size == 1) {
            Paths.get(env.getWorkingDirectory().toString(), args[0])
        } else {
            io.errorStream.write("Too many arguments\n")
            return ReturnCode(1)
        }
        if (!Files.exists(path)) {
            io.errorStream.write("ls: cannot access '${path.fileName}': No such file or directory\n")
            return ReturnCode(1)
        } else if (!Files.isDirectory(path)) {
            io.outputStream.write(path.fileName.toString() + "\n")
        } else {
            val files = File(path.toString()).listFiles()
            Arrays.sort(files)
            val result = StringBuilder()
            for (file in files) {
                result.append(file.name).append("\n")
            }
            io.outputStream.write(result.toString())
        }
        return ReturnCode.success

    }
}

