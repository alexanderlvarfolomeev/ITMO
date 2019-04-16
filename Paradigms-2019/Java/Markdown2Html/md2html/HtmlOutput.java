package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class HtmlOutput implements AutoCloseable{

    private final Writer writer;

    HtmlOutput(final String fileName) throws Md2HtmlException {
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw Md2HtmlException.error("Error output file error '%s': %s", fileName, e.getMessage());
        }
    }

    void add(String block) {
        try {
            writer.write(block + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

