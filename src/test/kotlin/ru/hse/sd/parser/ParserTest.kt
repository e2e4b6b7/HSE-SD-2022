package ru.hse.sd.parser

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ParserTest {

    private fun checkParser(input: String, expected: List<Statement>, variables: Map<String, String> = emptyMap()) {
        val parser = Parser()
        assertEquals(expected, parser.parse(input).map { parser.subst(it, variables) })
    }

    @Test
    fun `test parser simple assignment`() {
        val input = "x=x"
        val expected = listOf(
            stmt(
                assn("x", "x")
            )
        )
        checkParser(input, expected)
    }

    @Test
    fun `test parser hard assignment`() {
        val input = "x=x\"x\"'x'"
        val expected = listOf(
            stmt(
                assn("x", "xxx")
            )
        )
        checkParser(input, expected)
    }

    @Test
    fun `test parser simple command run`() {
        val input = "ls /"
        val expected = listOf(
            stmt(
                cmdRun("ls", "/")
            )
        )
        checkParser(input, expected)
    }

    @Test
    fun `test parser multiple commands`() {
        val input = "cmd=echo; arg=Hello; echo Hello"
        val expected = listOf(
            stmt(
                assn("cmd", "echo")
            ),
            stmt(
                assn("arg", "Hello")
            ),
            stmt(
                cmdRun("echo", "Hello")
            )
        )
        checkParser(input, expected)
    }

    @Test
    fun `test parser multiple args`() {
        val input = "supercommand / -name=text --tmp=root -- git"
        val expected = listOf(
            stmt(
                cmdRun("supercommand", "/", "-name=text", "--tmp=root", "--", "git")
            )
        )
        checkParser(input, expected)
    }

    @Test
    fun `test parser strange command run`() {
        val input = "\"super\"' com'ma'nd' / -name=text \"--tmp=root -- git\" 'Hello World!'"
        val expected = listOf(
            stmt(
                cmdRun("super command", "/", "-name=text", "--tmp=root -- git", "Hello World!")
            )
        )
        checkParser(input, expected)
    }

    @Test
    fun `test parser only whitespaces`() {
        val input = " "
        val expected = listOf(stmt())
        checkParser(input, expected)
    }

    @Test
    fun `test parser empty input`() {
        val input = ""
        val expected = listOf(stmt())
        checkParser(input, expected)
    }
}
