package ru.itmo.tpl.web;

import freemarker.template.*;
import freemarker.template.utility.NumberUtil;
import freemarker.template.utility.StringUtil;
import ru.itmo.tpl.util.DataUtil;
import ru.itmo.tpl.util.DebugUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerServlet extends HttpServlet {
    private Configuration freemarkerConfiguration;

    @Override
    public void init() throws ServletException {
        super.init();

        freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_29);

        File freemarkerDirectory = DebugUtil.getFile(getServletContext(), "WEB-INF/templates");
        try {
            freemarkerConfiguration.setDirectoryForTemplateLoading(freemarkerDirectory);
        } catch (IOException e) {
            throw new ServletException("Unable to configure freemarker configuration:"
                    + " freemarkerConfiguration.setDirectoryForTemplateLoading(freemarkerDirectory) failed"
                    + " [freemarkerDirectory=" + freemarkerDirectory + "].", e);
        }

        freemarkerConfiguration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        freemarkerConfiguration.setFallbackOnNullLoopVariable(false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (request.getRequestURI().equals("") || request.getRequestURI().equals("/")) {
            response.sendRedirect("/index");
            response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        }
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        Template template;
        try {
            template = freemarkerConfiguration.getTemplate(
                    URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8.name()) + ".ftlh");
        } catch (TemplateNotFoundException e) {
            template = freemarkerConfiguration.getTemplate("/misc/404-page.ftlh");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        response.setContentType("text/html");
        Map<String, Object> data = new HashMap<>();
        putData(request, data);

        try {
            template.process(data, response.getWriter());
        } catch (TemplateException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    private void putData(HttpServletRequest request, Map<String, Object> data) {
        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            String key = e.getKey();
            if (e.getValue() != null && e.getValue().length == 1) {
                Object value = e.getValue()[0];




                if (key != null && key.endsWith("_id")) {
                    Long valueLong = toLong((String) value);
                    value = valueLong == null ? value : valueLong;
                }
                data.put(key, value);
            }
        }
        data.put("uri", request.getRequestURI());
        DataUtil.putData(data);
    }

    private Long toLong(String parameter) {
        try {
            return Long.parseLong(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
