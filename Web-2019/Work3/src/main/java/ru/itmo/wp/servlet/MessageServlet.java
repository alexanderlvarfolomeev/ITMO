package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class MessageServlet extends HttpServlet {

    private ArrayList<UserTextPair> messages = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object objectToConvert = objectFromAction(request);
        if (objectToConvert != null) {
            String json = new Gson().toJson(objectToConvert);
            response.getWriter().print(json);
            response.getWriter().flush();
            response.setContentType("application/json");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private Object objectFromAction(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String action = request.getRequestURI();
        switch (action) {
            case "/message/auth":
                String user = request.getParameter("user");
                user = user == null ? "" : user;
                session.setAttribute("user", user);
                return user;
            case "/message/add":
                messages.add(new UserTextPair((String) session.getAttribute("user"), request.getParameter("text")));
                return messages.get(messages.size() - 1);
            case "/message/findAll":
                return messages;
            default:
                return null;
        }
    }
}
