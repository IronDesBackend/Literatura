package com.aluracursos.libreria.Challenge.Literarura.models;

import com.aluracursos.libreria.Challenge.Literarura.dto.DatosLibros;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )

    private List<Autor> autores;

    @Column(name = "lenguajes")
    private String lenguajes;

    private Double totalDescargas;

    public Libro() {
    }

    public Libro(DatosLibros datosLibros, List<Autor> autores) {
        this.titulo = datosLibros.titulo();
        this.autores = autores;
        this.lenguajes = String.join(",", datosLibros.idioma());
        this.totalDescargas = datosLibros.totalDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public String getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(String lenguajes) {
        this.lenguajes = lenguajes;
    }

    public Double getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(Double totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    @Override
    public String toString() {
        String autoresNombre =  autores.stream()
                .map(Autor::getNombre)
                .collect(Collectors.joining());
        return
                "Titulo: " + titulo +
                " - Lenguajes: " + lenguajes +
                " - TotalDescargas: " + totalDescargas +
                "\nAutores: " + autores;

    }


}
