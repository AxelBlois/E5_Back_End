package com.esiee.tp3.servlet.resource;

import com.esiee.tp3.domain.Function;
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

@WebServlet("/api/resources/functions/*")
public class FunctionResource extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Datamodel datamodel = Datamodel.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String jsonResponse = null;

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Function> functions = datamodel.getFunctions();
                jsonResponse = objectMapper.writeValueAsString(functions);
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

                    Function function = datamodel.getFunction(id);
                    if (function != null) {
                        jsonResponse = objectMapper.writeValueAsString(function);
                    } else {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Function not found.");
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
            Function newFunction = objectMapper.readValue(body, Function.class);

            if (newFunction.getId() != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID must be null for creation.");
                return;
            }

            Function savedFunction = datamodel.saveFunction(newFunction);
            String jsonResponse = objectMapper.writeValueAsString(savedFunction);

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
            Function updatedFunction = objectMapper.readValue(body, Function.class);

            if (updatedFunction.getId() == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required for update.");
                return;
            }
            if (datamodel.getFunction(updatedFunction.getId()) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Function not found for update.");
                return;
            }

            Function savedFunction = datamodel.saveFunction(updatedFunction);
            String jsonResponse = objectMapper.writeValueAsString(savedFunction);

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
                if (datamodel.getFunction(id) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Function not found for deletion.");
                    return;
                }

                boolean deleted = datamodel.deleteFunction(id);
                if (deleted) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "Cannot delete Function: resource might be in use.");
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