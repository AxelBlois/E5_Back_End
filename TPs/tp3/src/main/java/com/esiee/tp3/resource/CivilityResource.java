package com.esiee.tp3.resource;

import com.esiee.tp3.domain.Civility;
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

@WebServlet("/api/resources/civilities/*")
public class CivilityResource extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Datamodel datamodel = Datamodel.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String jsonResponse = null;

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Civility> civilities = datamodel.getCivilities();
                jsonResponse = objectMapper.writeValueAsString(civilities);
            } else {
                String[] splits = pathInfo.split("/");
                if (splits.length > 1) {
                    Long id = null;
                    try {
                        id = Long.parseLong(splits[1]);
                    } catch (NumberFormatException e) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
                        return;
                    }

                    Civility civility = datamodel.getCivility(id);
                    if (civility != null) {
                        jsonResponse = objectMapper.writeValueAsString(civility);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Civility not found.");
                        return;
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
            Civility newCivility = objectMapper.readValue(body, Civility.class);

            if (newCivility.getId() != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID must be null for creation.");
                return;
            }

            Civility savedCivility = datamodel.saveCivility(newCivility); // Utilise la méthode save
            String jsonResponse = objectMapper.writeValueAsString(savedCivility);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing POST: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Civility updatedCivility = objectMapper.readValue(body, Civility.class);

            if (updatedCivility.getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for update.");
                return;
            }
            if (datamodel.getCivility(updatedCivility.getId()) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Civility not found for update.");
                return;
            }

            Civility savedCivility = datamodel.saveCivility(updatedCivility); // Utilise la méthode save
            String jsonResponse = objectMapper.writeValueAsString(savedCivility);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(jsonResponse);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing PUT: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for deletion.");
            return;
        }

        try {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                Long id = Long.parseLong(splits[1]);
                if (datamodel.getCivility(id) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Civility not found for deletion.");
                    return;
                }

                boolean deleted = datamodel.deleteCivility(id);
                if (deleted) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    // Cas où la suppression échoue (ex: civilité utilisée)
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Cannot delete Civility: resource might be in use.");
                }

            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL pattern for deletion.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing DELETE: " + e.getMessage());
        }
    }
}
