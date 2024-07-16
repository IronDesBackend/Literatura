package com.aluracursos.libreria.Challenge.Literarura.main;

import com.aluracursos.libreria.Challenge.Literarura.dto.*;
import com.aluracursos.libreria.Challenge.Literarura.models.*;
import com.aluracursos.libreria.Challenge.Literarura.repository.*;
import com.aluracursos.libreria.Challenge.Literarura.service.ConsumoAPI;
import com.aluracursos.libreria.Challenge.Literarura.service.ConvierteDatos;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Menu {

        private static final String URL_BASE = "https://gutendex.com/books/";
        private ConsumoAPI consumoAPI = new ConsumoAPI();
        private ConvierteDatos conversor = new ConvierteDatos();
        private Scanner teclado = new Scanner(System.in);

        @Autowired
        private LibroRepository libroRepository;

        @Autowired
        private AutorRepository autorRepository;


        public List<Autor> getAutoresYear (int year){
            return autorRepository.autoresYear(year);
        }

    public Menu(LibroRepository libroRepository, AutorRepository autorRepository) {
            this.libroRepository = libroRepository;
            this.autorRepository = autorRepository;
        }

        public void muestraMenu () {
            int opcion = -1;
            while (opcion != 0) {
                System.out.println("------------------------------------------------------------------\n");
                var menu = """
                        1 - Buscar libro por titulo
                        2 - Filtrar libros por idioma
                        3 - Listar libros registrados
                        4 - Filtrar autores registrados
                        5 - Filtrar autores vivos por un año determinado
                        0 - Salir
                        """;
                System.out.println(menu);
                System.out.println("------------------------------------------------------------------");
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        libroPorTitulo();
                        break;
                    case 2:
                        libroPorIdioma();
                        break;
                    case 3:
                        librosRegistrados();
                        break;
                    case 4:
                        librosPorAutor();
                        break;
                    case 5:
                        autorPorYear();
                        break;

                    case 0:
                        System.out.println("********Finalizando aplicación********");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            }
        }


        private DatosLibros buscarLibroPorTitulo (String tituloLibro) throws IOException {
            String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
            Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

            Optional<DatosLibros> librosBuscados = datosBusqueda.libros().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst();

            return librosBuscados.orElse(null);
        }

        private void libroPorTitulo () {
            System.out.println("------------------------------------------------------------------\n");
            System.out.println("Indica el titulo del libro que deseas buscar");
            System.out.println("\n------------------------------------------------------------------");
            String tituloLibro = teclado.nextLine();
            DatosLibros libro = null;
            try {
                libro = buscarLibroPorTitulo(tituloLibro);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (libro != null) {
                guardarLibroEnBaseDeDatos(libro);
                System.out.println("------------------------------------------------------------------\n");
                System.out.println("Libro Encontrado: ");
                System.out.println("\n------------------------------------------------------------------\n");
                System.out.println("LIBRO");
                System.out.println("Título: " + libro.titulo());
                System.out.println("Autor(es): " + libro.autor().stream().map(DatosAutor::nombre).collect(Collectors.joining(", ")));
                System.out.println("Idiomas: " + String.join(", ", libro.idioma()));
                System.out.println("Total de descargas: " + libro.totalDescargas());
                System.out.println("------------------------------------------------------------------\n");
            } else {
                System.out.println("Libro no Encontrado. Indica una opción diferente.");
            }
            System.out.println("\n");
        }

        public void guardarLibroEnBaseDeDatos (DatosLibros datosLibros){
            // Verificar si el libro ya existe en la base de datos
            Libro libroExistente = libroRepository.findByTituloIgnoreCase(datosLibros.titulo());

            if (libroExistente != null) {
                System.out.println("No es posible registrar este libro, ya fue ingresado anteriormente");
            } else {
                // Buscar y/o guardar autores
                List<Autor> autores = datosLibros.autor().stream()
                        .map(datosAutor -> {
                            Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                            return autorExistente.orElseGet(() -> autorRepository.save(new Autor(datosAutor)));
                        })
                        .collect(Collectors.toList());

                // Crear el nuevo libro
                Libro nuevoLibro = new Libro(datosLibros, autores);
                libroRepository.save(nuevoLibro);
                System.out.println("Libro guardado exitosamente: " + nuevoLibro);
            }
        }
        // Nuevo método para mostrar todos los libros registrados


        private void librosRegistrados () {

            List<Libro> libros = libroRepository.buscarTuplaPorAutor();
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados.");
            } else {
                System.out.println("------------------------------------------------------------------\n");
                System.out.println("LIBROS REGISTRADOS");
                System.out.println("\n------------------------------------------------------------------");
                for (Libro libro : libros) {
                    System.out.println("Título: " + libro.getTitulo());
                    System.out.println("Autor(es): " + libro.getAutores().stream().map(Autor::getNombre).collect(Collectors.joining(", ")));
                    System.out.println("Idiomas: " + String.join(", ", libro.getLenguajes()));
                    System.out.println("Total de Descargas: " + libro.getTotalDescargas());
                    System.out.println("------------------------------------------------------------------");
                }
            }
            System.out.println("\n");
        }

        @Transactional
        public void librosPorAutor () {
            List<Autor> autores = autorRepository.librosAutor();
            if (autores.isEmpty()) {
                System.out.println("No hay autores registrados.");
            } else {
                System.out.println("------------------------------------------------------------------\n");
                System.out.println("AUTORES REGISTRADOS");
                System.out.println("\n------------------------------------------------------------------");
                for (Autor autor : autores) {
                    System.out.println(autor.toString());
                    System.out.println("------------------------------------------------------------------");
                }
            }
            System.out.println("\n");
        }

        @Transactional
        public void autorPorYear () {

            System.out.println("Indica un año para buscar los autores vivos en esa época");
            int year = teclado.nextInt();

            List<Autor> autores = autorRepository.autoresYear(year);
            if (autores.isEmpty()) {
                System.out.println("No hay autores que vivieron en el año " + year + ".");
            } else {
                System.out.println("------------------------------------------------------------------\n");
                System.out.println("AUTORES QUE VIVIERON EN EL AÑO " + year);
                System.out.println("\n------------------------------------------------------------------\n");
                for (Autor autor : autores) {
                    System.out.println(autor.toString());
                    System.out.println("------------------------------------------------------------------\n");
                }
            }
            System.out.println("\n");
        }

        @Transactional
        private void libroPorIdioma () {
            System.out.println("Selecciona el idioma por el que deseas filtrar: ");
            while (true) {
                String opciones = """
                        1. EN - Inglés
                        2. ES - Español
                        3. FR - Francés
                        4. PT - Portugués
                        0. Volver a las opciones anteriores
                        """;
                System.out.println(opciones);
                int opcion;
                while (true) {
                    if (teclado.hasNextInt()) {
                        opcion = teclado.nextInt();
                        teclado.nextLine(); // Consumir el salto de línea
                        break;
                    } else {
                        System.out.println("Formato no válido, ingrese una de las opciones del menu");
                        teclado.nextLine(); // Limpiar el buffer
                    }
                }
                switch (opcion) {
                    case 0:
                        return; // Salir del menú
                    case 1:
                        buscarLibrosPorIdioma(Idiomas.EN);
                        break;
                    case 2:
                        buscarLibrosPorIdioma(Idiomas.ES);
                        break;
                    case 3:
                        buscarLibrosPorIdioma(Idiomas.FR);
                        break;
                    case 4:
                        buscarLibrosPorIdioma(Idiomas.PT);
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo nuevamente.");


                        break;
                }
            }
        }
        @Transactional
        public void buscarLibrosPorIdioma (Idiomas idioma){
            String codigoIdioma = idioma.getCodigo();
            List<Libro> libros = libroRepository.findByLenguajes(codigoIdioma);

            if (libros.isEmpty()) {
                System.out.println("No se encontraron libros en el idioma seleccionado.");
            } else {
                System.out.println("------------------------------------------------------------------\n");
                System.out.println("Libros encontrados en " + idioma.getDescripcion() + ":");
                System.out.println("\n------------------------------------------------------------------");
                for (Libro libro : libros) {
                    // Cargar explícitamente la colección de autores
                    Hibernate.initialize(libro.getAutores());
                    System.out.println(libro);
                    System.out.println("------------------------------------------------------------------");
                }
            }
        }

    }
