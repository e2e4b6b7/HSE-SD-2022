package ru.hse.sd.cmd

import ru.hse.sd.IO
import ru.hse.sd.env.VariableEnvironment

/** Command from CLI representation */
interface Command {
    /**
     * Execute command from CLI.
     * [env] contains environment variables.
     * [args] contains arguments for command.
     * [io] contains streams for input, output and errors.
     */
    fun execute(env: VariableEnvironment, args: List<String>, io: IO): CommandResult
}
