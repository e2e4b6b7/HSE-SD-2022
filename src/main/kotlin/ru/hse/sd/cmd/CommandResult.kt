package ru.hse.sd.cmd

/** Type of objects which are returned after execution of [Command] */
sealed interface CommandResult

/** Means that CLI has to be stopped after executing command, which returns this result */
object Exit : CommandResult

/** Result which contains code return from command */
class ReturnCode(val code: Int) : CommandResult {
    companion object {
        val success = ReturnCode(0)
    }
}
