package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.write
import java.io.File
import kotlin.io.path.Path

class Pwd: Command {

    override fun eval(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        // TODO validate function for arguments in all classes that implements Command (?)
        if (args.isNotEmpty()) {
            io.errorStream.write("pwd: too many arguments")
            return ReturnCode(1)
        }
        io.outputStream.write(System.getProperty("user.dir"))
        return ReturnCode(0)
    }
}

fun main() {
    val pwd = Pwd()
    pwd.eval(mapOf(), listOf(), IO(System.`in`, System.out, System.err))
}
