package ru.hse.sd.parser

fun stmt(vararg tasks: Task) = Statement(tasks.asList())

fun cmdRun(cmdName: String, vararg args: String) = CommandRun(cmdName, args.asList())

fun assn(varName: String, value: String) = Assignment(varName, value)
