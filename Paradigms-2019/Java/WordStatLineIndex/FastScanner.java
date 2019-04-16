import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.function.Function;

public final class FastScanner implements AutoCloseable {

    private Reader stream;

    private int positionInBuffer = 0;

    private StringBuilder buffer = new StringBuilder();

    private boolean endOfStream = false;

    FastScanner(String string) {
        stream = new StringReader(string);
    }

    FastScanner(Reader reader) {
        stream = reader;
    }

    private void fill() throws IOException {
        int bufferMaxSize = 1 << 13;
        char[] chars = new char[bufferMaxSize];
        int bufferSize = stream.read(chars, 0, bufferMaxSize);
        if (bufferSize == -1) {
            endOfStream = true;
        } else {
            buffer.append(chars, 0, bufferSize);
        }
    }

    private char read() throws IOException {
        if (bufferIsEmpty()) {
            fill();
            if (endOfStream) {
                return 0;
            }
        }
        return buffer.charAt(positionInBuffer++);
    }

    String nextLine() throws IOException {
        return next(FastScanner::checkForLines);
    }

    String nextWord() throws IOException {
        return next(FastScanner::checkForWords);
    }

    private String next(Function<Character, Boolean> isNotDelimiter) throws IOException {
        int firstPosition = positionInBuffer;
        char c = read();
        while (!(isNotDelimiter.apply(c) || endOfStream)) {
            firstPosition++;
            c = read();
        }
        if (endOfStream) {
            return null;
        }
        while (isNotDelimiter.apply(c)) {
            c = read();
        }
        String token = buffer.substring(firstPosition, positionInBuffer - 1);
        buffer.delete(0, positionInBuffer);
        positionInBuffer = 0;
        return token.toLowerCase();
    }

    private static boolean checkForWords(char c) {
        return Character.isLetter(c) || (Character.getType(c) == Character.DASH_PUNCTUATION) || c == '\'';
    }

    private static boolean checkForLines(char c) {
        return (c != '\n' && c != 0);
    }

    private boolean bufferIsEmpty() {
        return (positionInBuffer >= buffer.length());
    }

    public void close() throws IOException {
        stream.close();
    }
}