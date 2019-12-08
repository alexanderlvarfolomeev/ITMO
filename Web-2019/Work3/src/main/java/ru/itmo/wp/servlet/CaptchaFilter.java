package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (session.getAttribute("passed") == null) {
            if (session.getAttribute("captcha") == null) {
                changeCaptcha(session);
            }

            switch (request.getRequestURI()) {
                case "/captcha/enter":
                    String captchaResponse = request.getParameter("captcha");
                    if (session.getAttribute("captcha").toString().equals(captchaResponse)) {
                        session.setAttribute("passed", true);
                        session.removeAttribute("captcha");
                    } else {
                        changeCaptcha(session);
                    }
                    String redirectedUri = (String) session.getAttribute("uri");
                    response.sendRedirect(redirectedUri);
                    break;
                case "/captcha.png":
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(ImageUtils.toPng(session.getAttribute("captcha").toString()));
                    outputStream.close();
                    response.setContentType("image/png");
                    break;
                default:
                    if (session.getAttribute("uri") == null) {
                        session.setAttribute("uri", request.getRequestURI());
                    }
                    Writer writer = response.getWriter();
                    writer.write("<img src=\"captcha.png\" alt=\"#\">\n" +
                            "    <form action=\"captcha/enter\" method=\"post\">\n" +
                            "        <label for=\"captcha-enter-form__captcha\"></label>\n" +
                            "        <input name=\"captcha\" id=\"captcha-enter-form__captcha\">\n" +
                            "    </form>");
                    writer.flush();
                    response.setContentType("text/html");
                    changeCaptcha(session);
            }
        } else {
            if (!request.getRequestURI().equals("/captcha/enter")) {
                chain.doFilter(request, response);
            } else {
                response.sendRedirect((String) session.getAttribute("uri"));
            }
        }
    }

    private void changeCaptcha(HttpSession session) {
        int result = new Random().nextInt(900) + 100;
        session.setAttribute("captcha", result);
    }
}
