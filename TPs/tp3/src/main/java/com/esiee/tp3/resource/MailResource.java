package com.esiee.tp3.servlet.resource;

import com.esiee.tp3.domain.Mail;
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

@WebServlet("/api/resources/mails/*")
public class MailResource extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Datamodel datamodel = Datamodel.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String jsonResponse = null;

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Mail> mails = datamodel.getMails(); // Attention : retourne TOUS les mails
                jsonResponse = objectMapper.writeValueAsString(mails);
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

                    Mail mail = datamodel.getMail(id);
                    if (mail != null) {
                        jsonResponse = objectMapper.writeValueAsString(mail);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Mail not found.");
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
            // Désérialiser en Mail. Le JSON devra contenir les IDs pour Person et MailType.
            // Exemple JSON: { "address": "new@example.com", "type": {"id": 1}, "person": {"id": 1} }
            Mail newMail = objectMapper.readValue(body, Mail.class);

            if (newMail.getId() != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID must be null for creation.");
                return;
            }
            if (newMail.getPerson() == null || newMail.getPerson().getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Person ID is required to create a mail.");
                return;
            }
            if (newMail.getType() == null || newMail.getType().getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "MailType ID is required to create a mail.");
                return;
            }

            // La méthode saveMail du Datamodel gère la liaison correcte
            Mail savedMail = datamodel.saveMail(newMail);
            if(savedMail == null){ // Gestion d'erreur si la personne ou le type n'existe pas dans saveMail
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to save mail, check person or mailtype existence.");
                return;
            }

            String jsonResponse = objectMapper.writeValueAsString(savedMail);

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
            Mail updatedMail = objectMapper.readValue(body, Mail.class);

            if (updatedMail.getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for update.");
                return;
            }
            if (datamodel.getMail(updatedMail.getId()) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Mail not found for update.");
                return;
            }
            // Ajouter validations pour Person et MailType si nécessaire

            Mail savedMail = datamodel.saveMail(updatedMail); // saveMail gère aussi la mise à jour
            if(savedMail == null){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update mail, check person or mailtype existence.");
                return;
            }

            String jsonResponse = objectMapper.writeValueAsString(savedMail);

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

            if (datamodel.getMail(id) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Mail not found for deletion.");
                return;
            }

            boolean deleted = datamodel.deleteMail(id);
            if (deleted) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete mail.");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing DELETE: " + e.getMessage());
        }
    }
}