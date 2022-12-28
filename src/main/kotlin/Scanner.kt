import TokenType.*

enum class TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // One or two character tokens.
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR, PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE, EOF
}

data class Token(val type: TokenType, val lexeme: String, val literal: Any?, val line: Int)

class Scanner(private val source: String) {
    private val tokens: MutableList<Token> = mutableListOf()
    private var start = 0
    private var current = 0
    private val line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current
            scanToken()
        }
        tokens.add(Token(EOF, "", null, line))
        return tokens
    }

    private fun scanToken() {
        //c is the  char 1 *before* current
        val c: Char = advance()
        when (c) {
            // Ignore whitespace. {}
            ' ', '\n', '\r', '\t' -> {}
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(if (curCharMatches('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if (curCharMatches('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (curCharMatches('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (curCharMatches('=')) GREATER_EQUAL else GREATER)
            '/' -> {
                if (curCharMatches('/')) {
                    // if comment, advance to next line
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
            }
            else -> error(line, "Unexpected character.")
        }
    }

    //looks ahead without consuming
    private fun peek(): Char = if (isAtEnd()) '\u0000' else source[current]

    private fun advance(): Char {
        return source[current++]
    }

    private fun curCharMatches(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false
        current++ //consume if it matches
        return true
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text: String = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun isAtEnd(): Boolean =
        current >= source.length
}