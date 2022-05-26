package ru.hse.sd

import ru.hse.sd.cmd.*
import ru.hse.sd.env.VariableEnvironment
import ru.hse.sd.env.cmd.CommandEnvironment
import ru.hse.sd.parser.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/** Class for running one task or pipe of tasks */
class Runner(
    /** Environments of commands, where calling commands are searched */
    private val cmdEnv: List<CommandEnvironment>,
    /** Contains all environment variables */
    private val varEnv: VariableEnvironment
) {
    /**
     * Runs [statement] and using [io] for getting input stream for first [Task],
     * output stream for last [Task] of [statement] and other streams (error stream, etc.)
     *
     * Returns true if CLI has to be stopped (exit was called), false otherwise.
     */
    fun run(statement: Statement, io: IO): Boolean {
        if (statement.tasks.isEmpty()) {
            return false
        }
        if (statement.tasks.size == 1) {
            val result = checkResult(runCommand(statement.tasks.first(), io), io)
            return result ?: false
        }
        var curOut = ByteArrayOutputStream()
        var nextIn = io.inputStream
        for (task in statement.tasks) {
            curOut = ByteArrayOutputStream()
            val curIo = IO(nextIn, curOut, io.errorStream)
            val result = checkResult(runCommand(task, curIo), curIo)
            if (result != null) return result
            nextIn = ByteArrayInputStream(curOut.toByteArray())
        }
        io.outputStream.write(curOut.toByteArray())
        return false
    }

    private fun checkResult(result: CommandResult?, io: IO): Boolean? {
        return when (result) {
            null -> {
                io.errorStream.write("Command not found\n")
                false
            }
            is Exit -> true
            is ReturnCode -> {
                if (result != ReturnCode.success) {
                    io.errorStream.write("Command failed\n")
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
                return command.execute(varEnv, task.args, io)
            }
            is Assignment -> {
                varEnv[task.varName] = task.value
                return ReturnCode.success
            }
        }
    }
}
