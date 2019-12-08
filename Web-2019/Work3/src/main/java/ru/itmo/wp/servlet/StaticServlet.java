package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class StaticServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        File[] files = Arrays.stream(uri.split("\\+"))
                .filter(splittedUri -> !splittedUri.isEmpty())
                .map(this::getFile).toArray(File[]::new);
        if (Arrays.stream(files).allMatch(File::isFile)) {
            response.setContentType(getContentTypeFromName(files[0].getName()));

            OutputStream outputStream = response.getOutputStream();
            for (File file : files) {
                Files.copy(file.toPath(), outputStream);
            }
            outputStream.flush();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private File getFile(final String uri) {
        String path = getServletContext().getRealPath(".");
        File file = new File(path, Paths.get("../../src/main/webapp/static", uri).toString());
        if (!file.isFile()) {
            file = new File(getServletContext().getRealPath(Paths.get("/static", uri).toString()));
        }
        return file;
    }

    private String getContentTypeFromName(String name) {
        name = name.toLowerCase();

        if (name.endsWith(".png")) {
            return "image/png";
        }

        if (name.endsWith(".jpg")) {
            return "image/jpeg";
        }

        if (name.endsWith(".html")) {
            return "text/html";
        }

        if (name.endsWith(".css")) {
            return "text/css";
        }

        if (name.endsWith(".js")) {
            return "application/javascript";
        }

        throw new IllegalArgumentException("Can't find content type for '" + name + "'.");
    }
}
