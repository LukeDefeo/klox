import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

var hadError = false

fun main(args: Array<String>) {

    if (args.size > 1) {
        System.out.println("Usage: jlox [script]");
        System.exit(64);
    } else if (args.size == 1) {
        runFile(args[0]);
        if (hadError) System.exit(65);
    } else {
        runPrompt();
    }

}

private fun runPrompt() {
    val input = InputStreamReader(System.`in`);
    val reader = BufferedReader(input);

    while (true) {
        System.out.print("> ");
        val line = reader.readLine();
        if (line == null) break;
        run(line);
    }
}

@kotlin.Throws(IOException::class)
private fun runFile(path: String) {
    val bytes: ByteArray = Files.readAllBytes(Paths.get(path))
    run(String(bytes, Charset.defaultCharset()))
}


fun run(script: String) {
    val scanner = Scanner(script)
    val tokens: List<Token> = scanner.scanTokens()

    // For now, just print the tokens.
    for (token in tokens) {
        System.out.println(token)
    }
}

fun error(line: Int, message: String) {
    report(line, "", message)
}

private fun report(
    line: Int, where: String,
    message: String
) {
    System.err.println(
        "[line $line] Error$where: $message"
    )
    hadError = true
}

