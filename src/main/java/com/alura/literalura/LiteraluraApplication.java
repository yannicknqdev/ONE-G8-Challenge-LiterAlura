package com.alura.literalura;

import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.service.LibroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LibroService libroService = new LibroService();
		
		System.out.println("=== BÚSQUEDA: Pride and Prejudice ===");
		List<DatosLibro> librosPride = libroService.buscarLibrosPorTitulo("pride and prejudice");
		
		if (!librosPride.isEmpty()) {
			DatosLibro primerLibro = librosPride.get(0);
			libroService.mostrarInformacionLibro(primerLibro);
		}
		
		System.out.println("\n=== ESTADÍSTICAS DE LA BÚSQUEDA ===");
		libroService.mostrarEstadisticas(librosPride);
		
		System.out.println("\n=== LIBROS ORDENADOS POR DESCARGAS ===");
		List<DatosLibro> librosOrdenados = libroService.ordenarPorDescargas(librosPride);
		librosOrdenados.stream()
				.limit(3)
				.forEach(libro -> {
					System.out.println("• " + libro.getTitulo() + " - Descargas: " + libro.getNumeroDeDescargas());
				});
	}
}