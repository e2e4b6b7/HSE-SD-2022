package ru.hse.sd

import ru.hse.sd.cmd.*
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Runner(private val cmdEnv: List<CommandEnvironment>, private val varEnv: VariableEnvironment) {
    fun run(statement: Statement, io: IO): Boolean {
        if (statement.tasks.isEmpty()) {
            return false
        }
        if (statement.tasks.size == 1) {
            val result = checkResult(runCommand(statement.tasks.first(), io), io)
            return result ?: false
        }
        var curOut = ByteArrayOutputStream()
        var curIo = IO(io.inputStream, curOut, io.errorStream)
        for (task in statement.tasks) {
            val result = checkResult(runCommand(task, curIo), curIo)
            if (result != null) return result
            val nextIn = ByteArrayInputStream(curOut.toByteArray())
            curOut = ByteArrayOutputStream()
            curIo = IO(nextIn, curOut, io.errorStream)
        }
        io.outputStream.write(curOut.toByteArray())
        return false
    }

    private fun checkResult(result: CommandResult?, io: IO): Boolean? {
        return when (result) {
            null -> {
                io.errorStream.write("Command not found")
                false
            }
            is Exit -> true
            is ReturnCode -> {
                if (result != ReturnCode.success) {
                    io.errorStream.write("Command failed")
                    false
                } else {
                    null
                }
            }
        }
    }

    private fun runCommand(task: Task, io: IO): CommandResult? {
        when (task) {
            is CommandRun -> {
                val command = cmdEnv.firstNotNullOfOrNull { it.getCommand(task.name) } ?: return null
                return command.execute(varEnv.mapView, task.args, io)
            }
            is Assignment -> {
                varEnv[task.varName] = task.value
                return ReturnCode.success
            }
        }
    }
}
