package ru.hse.sd.parser

import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.TerminalNode
import ru.hse.sd.merge
import ru.hse.sd.parser.antlr.SShellLex
import ru.hse.sd.parser.antlr.SShellParse

/** `SShell` parser */
class Parser {
    /**
     * Transform `SShell` code to list of pre-statements.
     *
     * [code] `SShell` code
     */
    fun parse(code: String): List<PreStatement>? = try {
        val charStream = CharStreams.fromString(code)
        val lexer = SShellLex(charStream)
        lexer.addErrorListener(ThrowingErrorListener)
        val tokensStream = CommonTokenStream(lexer)
        val parser = SShellParse(tokensStream)
        parser.addErrorListener(ThrowingErrorListener)
        parser.prog().stmt()
    } catch (e: ParseCancellationException) {
        null
    }

    /**
     * Substitutes variables in pre-statement and transform it to finalized statement.
     */
    fun subst(preStatement: PreStatement, variables: Map<String, String>): Statement {
        return transformStmt(preStatement, variables)
    }

    private fun transformStmt(stmt: SShellParse.StmtContext, variables: Map<String, String>): Statement {
        return Statement(stmt.task().mapNotNull { transformTask(it, variables) })
    }

    private fun transformTask(task: SShellParse.TaskContext, variables: Map<String, String>): Task? {
        task.assn()?.let {
            return transformAssn(it, variables)
        }
        task.cmd()?.let {
            return transformCmd(it, variables)
        }
        return null
    }

    /**
     * Iterates over the children of the cmd and dispatch them to related functions.
     */
    private inline fun iterateCmd(
        children: List<ParseTree>,
        onQuote: (SShellParse.QuoteContext) -> Unit,
        onTerminal: (TerminalNode) -> Unit,
        onDelimiter: () -> Unit
    ) {
        for (child in children) {
            when (child) {
                is TerminalNode ->
                    if (child.symbol.type == SShellParse.Whitespace)
                        onDelimiter()
                    else
                        onTerminal(child)
                is SShellParse.QuoteContext ->
                    onQuote(child)
            }
        }
        onDelimiter()
    }

    /**
     * Split and merge parsed elements into command tokens.
     */
    private fun transformCmd(cmd: SShellParse.CmdContext, variables: Map<String, String>): CommandRun? {
        val children = cmd.children ?: return null

        val values = mutableListOf<String>()
        val lastValue = mutableListOf<String>()
        iterateCmd(children,
            onQuote = { quote ->
                lastValue.add(transformQuote(quote, variables))
            },
            onTerminal = { terminal ->
                lastValue.add(text(terminal, variables))
            },
            onDelimiter = {
                if (lastValue.isNotEmpty()) {
                    values.add(lastValue.merge())
                    lastValue.clear()
                }
            })

        if (values.isEmpty()) return null

        val commandName = values[0]
        values.removeAt(0)
        return CommandRun(commandName, values)
    }

    private fun transformAssn(assn: SShellParse.AssnContext, variables: Map<String, String>): Assignment {
        val varName = assn.Assn().text
        val values = mutableListOf<String>()
        val childIterator = assn.children.iterator()
        do {
            val child = childIterator.next()
        } while (child is TerminalNode && child.symbol.type != SShellParse.Assn)
        for (child in childIterator) {
            when (child) {
                is TerminalNode ->
                    values.add(text(child, variables))
                is SShellParse.QuoteContext ->
                    values.add(transformQuote(child, variables))
            }
        }
        return Assignment(varName, values.merge())
    }

    private fun transformQuote(quote: SShellParse.QuoteContext, variables: Map<String, String>): String {
        val values = mutableListOf<String>()
        val children = quote.children
        check(children.size >= 2) { "At least left and right quotes expected as children" }
        for (child in children.subList(1, children.size - 1)) {
            check(child is TerminalNode) { "Unknown node for quote node" }
            values.add(text(child, variables))
        }
        return values.merge()
    }

    /**
     * Get textual value of terminal token.
     */
    private fun text(node: TerminalNode, variables: Map<String, String>): String {
        return when (node.symbol.type) {
            SShellParse.String -> {
                node.text
            }
            SShellParse.Subst -> {
                variables[node.text] ?: ""
            }
            else -> error("Invalid token")
        }
    }
}
