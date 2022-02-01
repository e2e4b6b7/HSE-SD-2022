package ru.hse.sd.cmd

sealed interface CommandResult

object Exit : CommandResult

class ReturnCode(val code: Int) : CommandResult {
    companion object {
        val success = ReturnCode(0)
    }
}
