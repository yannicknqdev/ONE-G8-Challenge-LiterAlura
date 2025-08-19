package com.alura.literalura.service;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {
    private ConsumoAPI consumoAPI;
    private ConvierteDatos conversor;
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private AutorRepository autorRepository;

    public LibroService() {
        this.consumoAPI = new ConsumoAPI();
        this.conversor = new ConvierteDatos();
    }

    public List<DatosLibro> buscarLibrosPorTitulo(String titulo) {
        String json = consumoAPI.obtenerDatosRelativa("books/?search=" + titulo.replace(" ", "%20"));
        Resultado resultado = conversor.obtenerDatos(json, Resultado.class);
        return resultado.getResultados();
    }

    public List<DatosLibro> obtenerTodosLosLibros() {
        String json = consumoAPI.obtenerDatosRelativa("books/");
        Resultado resultado = conversor.obtenerDatos(json, Resultado.class);
        return resultado.getResultados();
    }

    public void mostrarInformacionLibro(DatosLibro libro) {
        System.out.println("=== INFORMACIÓN DEL LIBRO ===");
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor(es): " + obtenerNombresAutores(libro));
        System.out.println("Idioma(s): " + String.join(", ", libro.getIdiomas()));
        System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
        System.out.println("ID: " + libro.getId());
        System.out.println("=============================");
    }

    public String obtenerNombresAutores(DatosLibro libro) {
        if (libro.getAutores() == null || libro.getAutores().isEmpty()) {
            return "Autor desconocido";
        }
        
        return libro.getAutores().stream()
                .map(DatosAutor::getNombre)
                .collect(Collectors.joining(", "));
    }

    public List<DatosLibro> filtrarPorIdioma(List<DatosLibro> libros, String idioma) {
        return libros.stream()
                .filter(libro -> libro.getIdiomas().contains(idioma))
                .collect(Collectors.toList());
    }

    public List<DatosLibro> ordenarPorDescargas(List<DatosLibro> libros) {
        return libros.stream()
                .sorted((l1, l2) -> Double.compare(
                    l2.getNumeroDeDescargas() != null ? l2.getNumeroDeDescargas() : 0,
                    l1.getNumeroDeDescargas() != null ? l1.getNumeroDeDescargas() : 0
                ))
                .collect(Collectors.toList());
    }

    public void mostrarEstadisticas(List<DatosLibro> libros) {
        System.out.println("=== ESTADÍSTICAS ===");
        System.out.println("Total de libros: " + libros.size());
        
        long librosConAutor = libros.stream()
                .filter(libro -> libro.getAutores() != null && !libro.getAutores().isEmpty())
                .count();
        System.out.println("Libros con autor conocido: " + librosConAutor);
        
        double promedioDescargas = libros.stream()
                .filter(libro -> libro.getNumeroDeDescargas() != null)
                .mapToDouble(DatosLibro::getNumeroDeDescargas)
                .average()
                .orElse(0.0);
        System.out.println("Promedio de descargas: " + String.format("%.2f", promedioDescargas));
        System.out.println("===================");
    }

    // ============ MÉTODOS DE BASE DE DATOS ============
    
    public Libro buscarYGuardarLibro(String titulo) {
        // Primero verificamos si ya existe en la base de datos
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(titulo);
        if (libroExistente.isPresent()) {
            System.out.println("✅ Libro ya existe en la base de datos: " + libroExistente.get().getTitulo());
            return libroExistente.get();
        }

        // Si no existe, buscamos en la API
        List<DatosLibro> resultadosAPI = buscarLibrosPorTitulo(titulo);
        if (resultadosAPI.isEmpty()) {
            System.out.println("❌ No se encontró el libro en la API de Gutendx");
            return null;
        }

        // Tomamos el primer resultado y lo guardamos
        DatosLibro datosLibro = resultadosAPI.get(0);
        return guardarLibro(datosLibro);
    }

    public Libro guardarLibro(DatosLibro datosLibro) {
        // Verificar si el libro ya existe
        if (libroRepository.existsByTituloIgnoreCase(datosLibro.getTitulo())) {
            System.out.println("⚠️ El libro ya existe en la base de datos: " + datosLibro.getTitulo());
            return libroRepository.findByTituloIgnoreCase(datosLibro.getTitulo()).orElse(null);
        }

        // Crear libro sin establecer el autor aún
        Libro libro = new Libro();
        libro.setTitulo(datosLibro.getTitulo());
        libro.setIdioma(datosLibro.getIdiomas().isEmpty() ? "N/A" : datosLibro.getIdiomas().get(0));
        libro.setNumeroDeDescargas(datosLibro.getNumeroDeDescargas());

        // Manejar el autor por separado
        if (!datosLibro.getAutores().isEmpty()) {
            DatosAutor datosAutor = datosLibro.getAutores().get(0);
            String nombreAutor = datosAutor.getNombre();
            
            Optional<Autor> autorExistente = autorRepository.findByNombreIgnoreCase(nombreAutor);
            
            if (autorExistente.isPresent()) {
                libro.setAutor(autorExistente.get());
            } else {
                // Crear y guardar nuevo autor
                Autor nuevoAutor = new Autor();
                nuevoAutor.setNombre(datosAutor.getNombre());
                nuevoAutor.setFechaDeNacimiento(datosAutor.getFechaDeNacimiento());
                nuevoAutor.setFechaDeDeceso(datosAutor.getFechaDeDeceso());
                
                Autor autorGuardado = autorRepository.save(nuevoAutor);
                libro.setAutor(autorGuardado);
            }
        }

        Libro libroGuardado = libroRepository.save(libro);
        System.out.println("✅ Libro guardado exitosamente: " + libroGuardado.getTitulo());
        return libroGuardado;
    }

    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }

    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> buscarAutoresVivosEnAno(Integer ano) {
        return autorRepository.buscarAutoresVivosEnAno(ano);
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }

    public void mostrarLibro(Libro libro) {
        System.out.println("=== INFORMACIÓN DEL LIBRO ===");
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Autor desconocido"));
        System.out.println("Idioma: " + libro.getIdioma());
        System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
        System.out.println("=============================");
    }

    public void mostrarAutor(Autor autor) {
        System.out.println("=== INFORMACIÓN DEL AUTOR ===");
        System.out.println("Nombre: " + autor.getNombre());
        System.out.println("Fecha de nacimiento: " + (autor.getFechaDeNacimiento() != null ? autor.getFechaDeNacimiento() : "Desconocida"));
        System.out.println("Fecha de deceso: " + (autor.getFechaDeDeceso() != null ? autor.getFechaDeDeceso() : "Vivo o desconocida"));
        System.out.println("==============================");
    }

    public Long contarLibrosPorIdioma(String idioma) {
        return libroRepository.countByIdioma(idioma);
    }
}