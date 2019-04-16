import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;

public final class FastScanner implements Closeable {

    private DataInputStream stream;

    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    private static final int DEFAULT_BYTES_COUNT = 1 << 7;

    private int bufferMaxSize, bufferSize, positionInBuffer;

    private byte[] buffer;

    private Charset charset;

    private boolean endOfStream = false;

    private FastScanner(final InputStream source, final Charset charset, final int bufferSize) {
        this.stream = new DataInputStream(source);
        positionInBuffer = 0;
        this.charset = charset;
        bufferMaxSize = bufferSize;
        buffer = new byte[bufferMaxSize];
    }

    FastScanner(final InputStream source) {
        this(source, DEFAULT_CHARSET, DEFAULT_BYTES_COUNT);
    }

    FastScanner(final File file) throws FileNotFoundException {
        this(new FileInputStream(file), DEFAULT_CHARSET, DEFAULT_BYTES_COUNT);
    }

    private void fill() {
        positionInBuffer = 0;
        try {
            bufferSize = stream.read(buffer, 0, bufferMaxSize);
            if (bufferSize == -1) {
                endOfStream = true;
            }
        } catch (IOException e) {
            System.out.print("Not all bytes was read.");
        }
    }

    private byte read() {
        if (bufferIsEmpty()) {
            fill();
            if (positionInBuffer == -1) {
                return 0;
            }
        }
        return buffer[positionInBuffer++];
    }

    public String nextLine() {
        StringBuilder stringBuilder = new StringBuilder();
        if (endOfStream) {
            return null;
        }
        int firstPosition = positionInBuffer;
        byte b;
        while ((b = read()) != 0) {
            if (b == '\n') {
                positionInBuffer--;
                break;
            }
            if (b == '\r') {
                break;
            }
            if (bufferIsEmpty()) {
                stringBuilder.append(new String(
                        buffer,
                        firstPosition,
                        positionInBuffer - firstPosition,
                        charset));
                firstPosition = 0;
            }
        }
        stringBuilder.append(new String(buffer, firstPosition, positionInBuffer - firstPosition, charset));
        goNextLine();
        return new String(stringBuilder);
    }

    boolean hasNextLine() {
        if (bufferIsEmpty()) {
            fill();
        }
        return (positionInBuffer < bufferSize || !endOfStream);
    }

    private void goNextLine() {
        if (read() != '\n') {
            positionInBuffer--;
        }
    }

    public String next() {
        skipSpaces();
        StringBuilder stringBuilder = new StringBuilder();
        if (endOfStream) {
            return null;
        }
        byte[] toLine = new byte[bufferMaxSize];
        int linePosition = 0;
        byte b;
        while ((b = read()) != 0) {
            if (isWhitespace(b)) {
                if (b != '\r') {
                    positionInBuffer--;
                }
                break;
            }
            toLine[linePosition++] = b;
        }
        stringBuilder.append(new String(toLine, 0, linePosition, charset));
        return new String(stringBuilder);
    }

    public boolean hasNext() {
        return (buffer[positionInBuffer] != '\n');
    }

    private void skipSpaces() {
        while (read() == ' ') {
            positionInBuffer++;
        }
    }

    private boolean isWhitespace(byte b) {
        return b == ' '
                || b == '\r'
                || b == '\n';
    }

    public int nextInt() {
        String nextInteger = next();
        try {
            return Integer.parseInt(Objects.requireNonNull(nextInteger));
        } catch (NumberFormatException e) {
            System.out.print("Wrong format of Integer variable: \"" + nextInteger + "\"\n");
        }
        return 0;
    }

    ArrayList<String> splitLine() {
        ArrayList<String> tokens = new ArrayList<>();
        String lineToSplit = nextLine();
        if (lineToSplit == null) {
            return tokens;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(lineToSplit);
        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens;
    }

    ArrayList<Integer> splitLineToInteger() {
        ArrayList<Integer> listToReturn = new ArrayList<>();
        for (String token : splitLine()) {
            try {
                listToReturn.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                System.out.print("Wrong format of Integer variable: \"" + token + "\"\n");
            }
        }
        return listToReturn;
    }

    private boolean bufferIsEmpty() {
        return (positionInBuffer == bufferSize);
    }

    public void close() throws IOException {
        if (stream == null)
            return;
        try {
            stream.close();
        } finally {
            stream = null;
            buffer = null;
        }
    }
}