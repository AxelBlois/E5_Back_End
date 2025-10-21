package com.esiee.tp2.servlet;

import com.esiee.tp2.domain.Person;
import com.esiee.tp2.models.Datamodel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/person_manage")
public class PersonManageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String personId = req.getParameter("id");
        if (personId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing person ID");
        }

        Long id;
        try {
            id = Long.parseLong(personId);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return;
        }

        Person person = Datamodel.getInstance().getPerson(id);
        if (person == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Person not found");
            return;
        }

        req.setAttribute("person", person);
        req.getRequestDispatcher("/WEB-INF/views/person_manage.jsp").forward(req, resp);
    }
}
