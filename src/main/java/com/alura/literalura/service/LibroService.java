package com.alura.literalura.service;

import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.model.Resultado;

import java.util.List;
import java.util.stream.Collectors;

public class LibroService {
    private ConsumoAPI consumoAPI;
    private ConvierteDatos conversor;

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
}