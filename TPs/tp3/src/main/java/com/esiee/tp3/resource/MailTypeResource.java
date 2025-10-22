package com.esiee.tp3.servlet.resource;

import com.esiee.tp3.domain.MailType;
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

@WebServlet("/api/resources/mailtypes/*")
public class MailTypeResource extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Datamodel datamodel = Datamodel.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String jsonResponse = null;

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<MailType> mailTypes = datamodel.getMailTypes();
                jsonResponse = objectMapper.writeValueAsString(mailTypes);
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

                    MailType mailType = datamodel.getMailType(id);
                    if (mailType != null) {
                        jsonResponse = objectMapper.writeValueAsString(mailType);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "MailType not found.");
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
            MailType newMailType = objectMapper.readValue(body, MailType.class);

            if (newMailType.getId() != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID must be null for creation.");
                return;
            }

            MailType savedMailType = datamodel.saveMailType(newMailType);
            String jsonResponse = objectMapper.writeValueAsString(savedMailType);

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
            MailType updatedMailType = objectMapper.readValue(body, MailType.class);

            if (updatedMailType.getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for update.");
                return;
            }
            if (datamodel.getMailType(updatedMailType.getId()) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "MailType not found for update.");
                return;
            }

            MailType savedMailType = datamodel.saveMailType(updatedMailType);
            String jsonResponse = objectMapper.writeValueAsString(savedMailType);

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
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for deletion.");
            return;
        }

        try {
            String[] splits = pathInfo.split("/");
            if (splits.length > 1) {
                Long id = Long.parseLong(splits[1]);
                if (datamodel.getMailType(id) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "MailType not found for deletion.");
                    return;
                }

                boolean deleted = datamodel.deleteMailType(id);
                if (deleted) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Cannot delete MailType: resource might be in use.");
                }

            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL pattern for deletion.");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing DELETE: " + e.getMessage());
        }
    }
}