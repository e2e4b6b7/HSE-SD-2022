package ru.hse.sd.cmd

import ru.hse.sd.IO

object ExitCommand : Command {
    override fun eval(env: Map<String, String>, args: List<String>, io: IO): CommandResult {
        return Exit
    }
}
