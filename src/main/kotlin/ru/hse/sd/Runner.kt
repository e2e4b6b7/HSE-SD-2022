package ru.hse.sd

import ru.hse.sd.cmd.*
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Runner(private val cmdEnv: List<CommandEnvironment>, private val varEnv: VariableEnvironment) {
    fun run(statement: Statement, io: IO) {
        if (statement.tasks.isEmpty()) {
            return
        }
        if (statement.tasks.size == 1) {
            runCommand(statement.tasks.first(), io)
            return
        }
        var curOut = ByteArrayOutputStream()
        var curIo = IO(io.inputStream, curOut, io.errorStream)
        for (task in statement.tasks) {
            val result = runCommand(task, curIo)
            if (!(result is ReturnCode && result == ReturnCode.success)) {
                return
            }
            val nextIn = ByteArrayInputStream(curOut.toByteArray())
            curOut = ByteArrayOutputStream()
            curIo = IO(nextIn, curOut, io.errorStream)
        }
        io.outputStream.write(curOut.toByteArray())
    }

    private fun runCommand(task: Task, io: IO): CommandResult? {
        when (task) {
            is CommandRun -> {
                val command = cmdEnv.firstNotNullOfOrNull { it.getCommand(task.name) }
                if (command == null) {
                    io.errorStream.write("Unknown command")
                    return null
                }
                return command.execute(varEnv.mapView, task.args, io)
            }
            is Assignment -> {
                varEnv[task.varName] = task.value
                return ReturnCode.success
            }
        }
    }
}
