package ru.hse.sd.parser

/**
 * Marking interface for tasks available in `SShell`.
 */
sealed interface Task

/**
 * Task for assigning new value to variable.
 */
data class Assignment(val varName: String, val value: String) : Task

/**
 * Common task for executing some command.
 */
data class CommandRun(val name: String, val args: List<String>) : Task
