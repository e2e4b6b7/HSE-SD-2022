package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.*

object BuiltinCommandEnvironment : CommandEnvironment {
    private val builtinCommand = mapOf(
        Pair("echo", Echo),
        Pair("exit", ExitCommand)
    )

    override fun getCommand(commandName: String): Command? {
        return builtinCommand[commandName]
    }
}

