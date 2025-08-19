package com.alura.literalura;

import com.alura.literalura.model.Resultado;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoAPI consumoAPI = new ConsumoAPI();
		ConvierteDatos conversor = new ConvierteDatos();
		
		var json = consumoAPI.obtenerDatos("https://gutendex.com/books/?search=pride%20and%20prejudice");
		Resultado datos = conversor.obtenerDatos(json, Resultado.class);
		
		System.out.println("Total de libros encontrados: " + datos.total());
		datos.resultados().stream()
				.limit(5)
				.forEach(libro -> {
					System.out.println("Título: " + libro.titulo());
					System.out.println("Autor: " + (libro.autores().isEmpty() ? "Desconocido" : libro.autores().get(0).nombre()));
					System.out.println("Idioma: " + libro.idiomas());
					System.out.println("Descargas: " + libro.numeroDeDescargas());
					System.out.println("------------------------");
				});
	}
}