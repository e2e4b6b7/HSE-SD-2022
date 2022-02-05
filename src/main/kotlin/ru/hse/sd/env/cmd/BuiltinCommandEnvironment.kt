package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.*

class BuiltinCommandEnvironment : CommandEnvironment {
    private val commands = mutableMapOf<String, Command>()

    fun registerCommand(name: String, cmd: Command) {
        commands[name] = cmd
    }

    override fun getCommand(commandName: String): Command? {
        return commands[commandName]
    }
}

