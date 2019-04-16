package md2html;

import java.util.Map;

public class Md2HtmlParser implements AutoCloseable {
    private final MarkdownSource source;
    private final HtmlOutput output;

    static private final Map<Character, String> specificSymbols = Map.of
            (
                    '&', "&amp;",
                    '<', "&lt;",
                    '>', "&gt;"
            );

    private final StringBuilder builder = new StringBuilder();

    Md2HtmlParser(final String source, final String output) throws Md2HtmlException {
        this.source = new MarkdownSource(source);
        this.output = new HtmlOutput(output);
    }

    void parse() throws Md2HtmlException {
        source.nextChar();
        while (source.getChar() != MarkdownSource.END) {
            skipLines();
            parseBlock();
        }
    }

    private void skipLines() throws Md2HtmlException {
        while (source.getChar() == '\n') {
            source.nextChar();
        }
    }

    private void parseBlock() throws Md2HtmlException {
        builder.delete(0, builder.length());
        int hashCount = 0;
        while (testNext('#')) {
            hashCount++;
        }
        if (hashCount > 0 && testNext(' ')) {
            output.add("<h" + hashCount + '>' + nextBlock() + "</h" + hashCount + '>');
        } else {
            for (int i = 0; i < hashCount; i++) {
                builder.append('#');
            }
            output.add("<p>" + nextBlock() + "</p>");
        }

    }

    private String nextBlock() throws Md2HtmlException {
        while (!test(MarkdownSource.END)) {
            if (test('\n')) {
                if (testNextBlock()) {
                    return builder.toString();
                } else {
                    builder.append('\n');
                }
            }
            appendChar();
        }
        return builder.toString();
    }

    private void appendChar() throws Md2HtmlException {
        char previousChar = toNextChar();
        switch (previousChar) {
            case '`':
                parseCode();
                break;
            case '*':
            case '_':
                parseEmphasis(previousChar);
                break;
            case '-':
                if (testNext('-')) {
                    parseCancel();
                } else {
                    appendSymbol(previousChar);
                }
                break;
            case '~':
                parseMark();
                break;
            case '\\':
                if (test('*') || test('_')) {
                    builder.append(source.getChar());
                    source.nextChar();
                } else {
                    appendSymbol(previousChar);
                }
                break;
            default:
                appendSymbol(previousChar);
        }
    }

    private void parseMark() throws Md2HtmlException {
        builder.append("<mark>");

        while (!test('~')) {
            appendChar();
        }
        builder.append("</mark>");
        source.nextChar();
    }

    private void appendSymbol(final char symbol) {
        if (specificSymbols.containsKey(symbol)) {
            builder.append(specificSymbols.get(symbol));
        } else {
            builder.append(symbol);
        }
    }

    private char toNextChar() throws Md2HtmlException {
        char previous = source.getChar();
        source.nextChar();
        return previous;
    }

    private void parseCode() throws Md2HtmlException {
        builder.append("<code>");

        while (!test('`')) {
            appendChar();
        }
        builder.append("</code>");
        source.nextChar();
    }

    private void parseEmphasis(final char indicator) throws Md2HtmlException {
        if (testNext(indicator)) {
            parseStrongEmphasis(indicator);
        } else if (Character.isWhitespace(source.getChar())) {
            builder.append(indicator);
        } else {
            builder.append("<em>");

            while (!test(indicator)) {
                appendChar();
            }
            builder.append("</em>");
            source.nextChar();
        }
    }

    private void parseStrongEmphasis(final char indicator) throws Md2HtmlException {
        builder.append("<strong>");

        while (testIndicator(indicator)) {
            appendChar();
        }
        builder.append("</strong>");
    }

    private boolean testIndicator(final char indicator) throws Md2HtmlException {
        if (test(indicator)) {
            if (test(MarkdownSource.END) || source.nextChar() == indicator) {
                source.nextChar();
                return false;
            }
            builder.append(indicator);
            return !test(MarkdownSource.END);
        }
        return true;
    }

    private void parseCancel() throws Md2HtmlException {
        builder.append("<s>");

        while (testIndicator('-')) {
            appendChar();
        }
        builder.append("</s>");
    }


    private boolean testNextBlock() throws Md2HtmlException {
        return source.nextChar() == '\n' || test(MarkdownSource.END);
    }

    private boolean testNext(final char c) throws Md2HtmlException {
        if (test(c)) {
            source.nextChar();
            return true;
        } else {
            return false;
        }
    }

    private boolean test(final char c) {
        return source.getChar() == c || source.getChar() == MarkdownSource.END;
    }

    public void close() {
        source.close();
        output.close();
    }
}