package com.esiee.tp2.servlet;

import com.esiee.tp2.domain.Person;
import com.esiee.tp2.model.Datamodel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/persons")
public class PersonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        List<Person> persons = Datamodel.getInstance().getPersons();

        request.setAttribute("persons", persons);
        request.getRequestDispatcher("/persons.jsp").forward(request,response);
    }

}
