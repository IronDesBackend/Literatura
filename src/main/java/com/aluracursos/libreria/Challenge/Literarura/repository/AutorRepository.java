package com.aluracursos.libreria.Challenge.Literarura.repository;

import com.aluracursos.libreria.Challenge.Literarura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> librosAutor();

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE a.fechaDeNacimiento <= :year AND (a.fechaDeDefunsion = 0 OR a.fechaDeDefunsion >= :year)")
    List<Autor> autoresYear(@Param("year") int year);

}
