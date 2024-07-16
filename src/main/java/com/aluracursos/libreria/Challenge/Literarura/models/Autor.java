package com.aluracursos.libreria.Challenge.Literarura.models;

import com.aluracursos.libreria.Challenge.Literarura.dto.DatosAutor;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeDefunsion;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeDefunsion = datosAutor.fechaDeDefunsion();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeDefunsion() {
        return fechaDeDefunsion;
    }

    public void setFechaDeDefunsion(Integer fechaDeDefunsion) {
        this.fechaDeDefunsion = fechaDeDefunsion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        String listaLibros = libros != null ? libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", ")) : "Ninguno";
        return "AUTOR\n\n" +
                "Nombre: " + nombre + '\n' +
                "Fecha De Nacimiento: " + fechaDeNacimiento + '\n' +
                "Fecha De Defunsion: " + fechaDeDefunsion + '\n' +
                "Libros: " + listaLibros;
    }
}
