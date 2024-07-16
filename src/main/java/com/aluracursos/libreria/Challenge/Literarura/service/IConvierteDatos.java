package com.aluracursos.libreria.Challenge.Literarura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
