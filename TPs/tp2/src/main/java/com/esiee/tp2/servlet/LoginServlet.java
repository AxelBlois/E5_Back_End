package com.esiee.tp2.servlet;

import com.esiee.tp2.domain.Person;
import com.esiee.tp2.model.Datamodel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected  void doGet(HttpServletRequest request, HttpServletResponse response)
        throws  ServletException, IOException {
        response.getWriter().println("Servlet Login Ready");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Person userVerif = null;
        for (Person p : Datamodel.getInstance().getPersons()){
            if (p.getLogin().equals(login) && p.getPassword().equals(password)){
                userVerif = p;
                break;
            }
        }

        if (userVerif != null) {
            request.getSession().setAttribute("user", userVerif);
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }

    }
}
