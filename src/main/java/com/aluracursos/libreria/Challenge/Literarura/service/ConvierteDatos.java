package com.aluracursos.libreria.Challenge.Literarura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return  objectMapper.readValue(json, clase);
        }catch(JsonProcessingException e) {
            throw new RuntimeException("No es posible obtener datos: " + e);
        }
    }
}
