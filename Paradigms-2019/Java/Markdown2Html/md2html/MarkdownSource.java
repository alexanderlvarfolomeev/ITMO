package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MarkdownSource implements AutoCloseable{
    static final char END = '\0';

    private char c;

    private final Reader reader;

    MarkdownSource(final String fileName) throws Md2HtmlException {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw Md2HtmlException.error("Error opening input file '%s': %s", fileName, e.getMessage());
        }
    }

    private char readChar() throws IOException {
        final int read = reader.read();
        return read == -1 ? END : (char) read;
    }

    char getChar() {
        return c;
    }

    char nextChar() throws Md2HtmlException {
        try {
            c = readChar();
            if (c == '\r') {
                c = readChar();
            }
            return c;
        } catch (final IOException e) {
            throw Md2HtmlException.error("Source read error", e.getMessage());
        }
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
