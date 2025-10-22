package com.esiee.tp3.servlet.resource;

import com.esiee.tp3.domain.Mail;
import com.esiee.tp3.domain.Person;
import com.esiee.tp3.model.Datamodel;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/resources/persons/*")
public class PersonResource extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Datamodel datamodel = Datamodel.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String jsonResponse = null;

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/resources/persons -> Liste des personnes
                List<Person> persons = datamodel.getPersons();
                jsonResponse = objectMapper.writeValueAsString(persons);
            } else {
                String[] splits = pathInfo.split("/");
                if (splits.length > 1) {
                    Long personId = null;
                    try {
                        personId = Long.parseLong(splits[1]);
                    } catch (NumberFormatException e) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Person ID format.");
                        return;
                    }

                    Person person = datamodel.getPerson(personId);
                    if (person == null) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Person not found.");
                        return;
                    }

                    if (splits.length > 2) {
                        // Gérer les sous-ressources, ex: /api/resources/persons/{id}/mails
                        String subResource = splits[2];
                        if ("mails".equalsIgnoreCase(subResource)) {
                            List<Mail> mails = datamodel.getMailsByPersonId(personId);
                            jsonResponse = objectMapper.writeValueAsString(mails);
                        } else {
                            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown sub-resource: " + subResource);
                            return;
                        }
                    } else {
                        // GET /api/resources/persons/{id} -> Une personne
                        jsonResponse = objectMapper.writeValueAsString(person);
                    }
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL pattern.");
                    return;
                }
            }

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            if (!resp.isCommitted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An internal server error occurred.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Person newPerson = objectMapper.readValue(body, Person.class);

            if (newPerson.getId() != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID must be null for creation.");
                return;
            }
            // Ajouter des validations si nécessaire (ex: login unique, champs obligatoires)

            Person savedPerson = datamodel.savePerson(newPerson);
            String jsonResponse = objectMapper.writeValueAsString(savedPerson);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing POST: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Person updatedPerson = objectMapper.readValue(body, Person.class);

            if (updatedPerson.getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for update.");
                return;
            }
            if (datamodel.getPerson(updatedPerson.getId()) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Person not found for update.");
                return;
            }
            // Ajouter des validations si nécessaire

            Person savedPerson = datamodel.savePerson(updatedPerson);
            String jsonResponse = objectMapper.writeValueAsString(savedPerson);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing PUT: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.split("/").length <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for deletion.");
            return;
        }

        try {
            String[] splits = pathInfo.split("/");
            Long id = Long.parseLong(splits[1]);

            if (datamodel.getPerson(id) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Person not found for deletion.");
                return;
            }

            boolean deleted = datamodel.deletePerson(id);
            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                // Devrait être rare si la vérification ci-dessus passe, mais en cas d'erreur interne
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete person.");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing DELETE: " + e.getMessage());
        }
    }
}