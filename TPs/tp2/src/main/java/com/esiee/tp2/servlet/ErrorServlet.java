package com.esiee.tp2.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.print.PrinterGraphics;
import java.io.IOException;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        String uri = (String) request.getAttribute("jakarta.servlet.error.request_uri");

        if (uri == null) uri = "Unknow";
        if (message == null) message = "No additional details availables";

        String userMessage = switch (statusCode) {
            case 401 -> "You are not authorized to access this ressource.";
            case 404 -> "The page you are looking for does not exist.";
            case 500 -> "An unexpected server error occurred.";
            default -> "An unexpected error occurred.";
        };

        request.setAttribute("statusCode", statusCode);
        request.setAttribute("message", message);
        request.setAttribute("userMessage", userMessage);
        request.setAttribute("uri", uri);

        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request,response);
    }
}
