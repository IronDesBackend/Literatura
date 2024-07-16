package com.aluracursos.libreria.Challenge.Literarura.repository;

import com.aluracursos.libreria.Challenge.Literarura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long>  {
    Libro findByTituloIgnoreCase(String titulo);

    @Query("SELECT l FROM Libro l JOIN FETCH l.autores a WHERE a.nombre = :nombre")
    List<Libro> buscarPorAutor(String nombre);

    @Query("SELECT l FROM Libro l JOIN FETCH l.autores")
    List<Libro> buscarTuplaPorAutor();

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autores WHERE l.lenguajes = :codigoIdioma")
    List<Libro> findByLenguajes(@Param("codigoIdioma") String codigoIdioma);
}
