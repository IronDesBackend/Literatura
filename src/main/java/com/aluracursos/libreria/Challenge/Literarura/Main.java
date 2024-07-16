package com.aluracursos.libreria.Challenge.Literarura;

import com.aluracursos.libreria.Challenge.Literarura.main.Menu;
import com.aluracursos.libreria.Challenge.Literarura.repository.AutorRepository;
import com.aluracursos.libreria.Challenge.Literarura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Menu menu = new Menu(libroRepository, autorRepository);
        menu.muestraMenu();
    }
}
