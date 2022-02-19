package ru.hse.sd.cmd

import ru.hse.sd.IO

/** Command from CLI representation */
interface Command {
    /**
     * Execute command from CLI.
     * [env] contains environment variables.
     * [args] contains arguments for command.
     * [io] contains streams for input, output and errors.
     */
    fun execute(env: Map<String, String>, args: List<String>, io: IO): CommandResult
}
