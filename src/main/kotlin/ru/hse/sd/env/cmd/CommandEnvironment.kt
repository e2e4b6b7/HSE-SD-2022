package ru.hse.sd.env.cmd

import ru.hse.sd.cmd.Command

sealed interface CommandEnvironment {
    fun getCommand(commandName: String): Command?
}
