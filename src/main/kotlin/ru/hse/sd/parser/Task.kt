package ru.hse.sd.parser

sealed interface Task

data class Assignment(val varName: String, val value: String) : Task

data class CommandRun(val name: String, val args: List<String>) : Task
