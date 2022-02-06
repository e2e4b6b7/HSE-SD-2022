package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.Command

/**
 * Environment of commands, where calling commands are searched.
 */
sealed interface CommandEnvironment {
    /**
     * Search command with name [commandName].
     * If the command is found then return it, otherwise return null
     */
    fun getCommand(commandName: String): Command?
}
