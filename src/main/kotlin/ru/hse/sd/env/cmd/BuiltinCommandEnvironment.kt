package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.*
/**
 * Environment of builtin commands.
 */
class BuiltinCommandEnvironment : CommandEnvironment {
    private val commands = mutableMapOf<String, Command>()

    /**
     * Register new builtin command [cmd] with name [name].
     */
    fun registerCommand(name: String, cmd: Command) {
        commands[name] = cmd
    }

    /**
     * Search command with name [commandName].
     * If the command is found then return it, otherwise return null
     */
    override fun getCommand(commandName: String): Command? {
        return commands[commandName]
    }
}
