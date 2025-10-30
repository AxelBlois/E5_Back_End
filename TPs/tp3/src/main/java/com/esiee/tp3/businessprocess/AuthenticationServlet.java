package com.esiee.tp3.businessprocess;


import com.esiee.tp3.model.Datamodel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import javax.xml.crypto.Data;

@WebServlet("api/businessProcess/authentication")
public class AuthenticationServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Datamodel datamodel = Datamodel.getInstance();

    private final TokenService tokenService = 

}
