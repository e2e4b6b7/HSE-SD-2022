package ru.hse.sd.cmd

import ru.hse.sd.IO

/** `exit` CLI command implementation */
object ExitCommand : Command {
    /**
     * Executes `exit` CLI command.
     * Returns [Exit] as a command result, which means that CLI has to be stopped.
     */
    override fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        return Exit
    }
}
