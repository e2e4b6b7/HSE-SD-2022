package ru.hse.sd.cmd

import ru.hse.sd.IO

sealed interface CommandResult

class ReturnCode(val code: Int) : CommandResult
object Exit : CommandResult

sealed interface Command {
    fun eval(args: List<String>, io: IO): CommandResult
}

