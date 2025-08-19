package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    
    @Autowired
    private LibroService libroService;

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ===============================================
                    🏛️  LITERALURA - CATÁLOGO DE LIBROS 📚
                    ===============================================
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros más descargados
                    7 - Buscar autor por nombre
                    8 - Estadísticas generales
                    
                    0 - Salir
                    ===============================================
                    Elije la opción a través de su número: 
                    """;
            
            System.out.println(menu);
            
            try {
                opcion = teclado.nextInt();
                teclado.nextLine(); // consumir el salto de línea
                
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        top10LibrosMasDescargados();
                        break;
                    case 7:
                        buscarAutorPorNombre();
                        break;
                    case 8:
                        mostrarEstadisticas();
                        break;
                    case 0:
                        System.out.println("\n¡Gracias por usar LiterAlura! 👋\n");
                        break;
                    default:
                        System.out.println("\n❌ Opción no válida. Por favor, ingrese un número del 0 al 8.\n");
                }
            } catch (Exception e) {
                System.out.println("\n❌ Error: Por favor ingrese un número válido.\n");
                teclado.nextLine(); // limpiar el buffer
                opcion = -1;
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("\n📖 BÚSQUEDA Y REGISTRO DE LIBRO POR TÍTULO");
        System.out.println("Ingrese el nombre del libro que desea buscar y guardar:");
        var tituloLibro = teclado.nextLine().trim();
        
        if (tituloLibro.isEmpty()) {
            System.out.println("❌ Error: El título no puede estar vacío.\n");
            return;
        }

        try {
            System.out.println("🔍 Buscando libro: \"" + tituloLibro + "\"...\n");
            Libro libro = libroService.buscarYGuardarLibro(tituloLibro);
            
            if (libro != null) {
                libroService.mostrarLibro(libro);
                System.out.println();
            } else {
                System.out.println("📭 No se encontró el libro: \"" + tituloLibro + "\"\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar el libro: " + e.getMessage() + "\n");
        }
    }

    private void listarLibrosRegistrados() {
        System.out.println("\n📚 LIBROS REGISTRADOS EN LA BASE DE DATOS");
        
        try {
            List<Libro> libros = libroService.listarTodosLosLibros();
            
            if (libros.isEmpty()) {
                System.out.println("📭 No hay libros registrados en la base de datos.\n");
                System.out.println("💡 Tip: Use la opción 1 para buscar y guardar libros desde la API.\n");
            } else {
                System.out.println("✅ Total de libros registrados: " + libros.size() + "\n");
                libros.forEach(libro -> {
                    System.out.println("📖 " + libro.getTitulo());
                    System.out.println("👤 " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Autor desconocido"));
                    System.out.println("🌍 " + libro.getIdioma());
                    System.out.println("📊 " + String.format("%.0f", libro.getNumeroDeDescargas()) + " descargas");
                    System.out.println("------------------------");
                });
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar libros: " + e.getMessage() + "\n");
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n👤 AUTORES REGISTRADOS EN LA BASE DE DATOS");
        
        try {
            List<Autor> autores = libroService.listarTodosLosAutores();
            
            if (autores.isEmpty()) {
                System.out.println("📭 No hay autores registrados en la base de datos.\n");
                System.out.println("💡 Tip: Los autores se registran automáticamente al guardar libros.\n");
            } else {
                System.out.println("✅ Total de autores registrados: " + autores.size() + "\n");
                autores.forEach(autor -> {
                    System.out.println("👤 " + autor.getNombre());
                    System.out.println("📅 Nacimiento: " + (autor.getFechaDeNacimiento() != null ? autor.getFechaDeNacimiento() : "Desconocido"));
                    System.out.println("⚰️ Deceso: " + (autor.getFechaDeDeceso() != null ? autor.getFechaDeDeceso() : "Vivo o desconocido"));
                    System.out.println("------------------------");
                });
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar autores: " + e.getMessage() + "\n");
        }
    }

    private void listarAutoresVivosEnAno() {
        System.out.println("\n🗓️ AUTORES VIVOS EN DETERMINADO AÑO");
        System.out.println("Ingrese el año:");
        
        try {
            int ano = teclado.nextInt();
            teclado.nextLine(); // consumir salto de línea
            
            if (ano < 0 || ano > 2024) {
                System.out.println("❌ Error: Por favor ingrese un año válido.\n");
                return;
            }
            
            List<Autor> autoresVivos = libroService.buscarAutoresVivosEnAno(ano);
            
            if (autoresVivos.isEmpty()) {
                System.out.println("📭 No se encontraron autores vivos en el año " + ano + ".\n");
                System.out.println("💡 Tip: Registre más libros para obtener más información de autores.\n");
            } else {
                System.out.println("✅ Autores vivos en " + ano + ":\n");
                autoresVivos.forEach(autor -> {
                    System.out.println("👤 " + autor.getNombre());
                    System.out.println("📅 Nacimiento: " + (autor.getFechaDeNacimiento() != null ? autor.getFechaDeNacimiento() : "Desconocido"));
                    System.out.println("⚰️ Deceso: " + (autor.getFechaDeDeceso() != null ? autor.getFechaDeDeceso() : "Vivo o desconocido"));
                    System.out.println("------------------------");
                });
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("❌ Error: Por favor ingrese un año válido.\n");
            teclado.nextLine(); // limpiar buffer
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n🌍 LIBROS POR IDIOMA (BASE DE DATOS)");
        System.out.println("Seleccione el idioma:");
        System.out.println("en - Inglés");
        System.out.println("es - Español");  
        System.out.println("fr - Francés");
        System.out.println("de - Alemán");
        System.out.println("pt - Portugués");
        
        var idioma = teclado.nextLine().trim().toLowerCase();
        
        if (idioma.isEmpty()) {
            System.out.println("❌ Error: Debe ingresar un código de idioma.\n");
            return;
        }

        try {
            System.out.println("🔍 Buscando libros en " + getNombreIdioma(idioma) + " en la base de datos...\n");
            
            List<Libro> librosPorIdioma = libroService.listarLibrosPorIdioma(idioma);
            Long totalLibros = libroService.contarLibrosPorIdioma(idioma);
            
            if (librosPorIdioma.isEmpty()) {
                System.out.println("📭 No se encontraron libros en " + getNombreIdioma(idioma) + " en la base de datos.\n");
                System.out.println("💡 Tip: Registre libros usando la opción 1 para verlos aquí.\n");
            } else {
                System.out.println("✅ Libros en " + getNombreIdioma(idioma) + ": " + totalLibros + " libro(s)\n");
                librosPorIdioma.forEach(libro -> {
                    System.out.println("📖 " + libro.getTitulo());
                    System.out.println("👤 " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Autor desconocido"));
                    System.out.println("📊 Descargas: " + String.format("%.0f", libro.getNumeroDeDescargas()));
                    System.out.println("------------------------");
                });
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("❌ Error al filtrar libros: " + e.getMessage() + "\n");
        }
    }

    private void top10LibrosMasDescargados() {
        System.out.println("\n🏆 TOP 10 LIBROS MÁS DESCARGADOS");
        
        try {
            System.out.println("🔍 Obteniendo libros populares...\n");
            List<DatosLibro> libros = libroService.obtenerTodosLosLibros();
            List<DatosLibro> top10 = libroService.ordenarPorDescargas(libros);
            
            if (top10.isEmpty()) {
                System.out.println("📭 No se encontraron libros.\n");
                return;
            }
            
            System.out.println("🏆 TOP 10 LIBROS MÁS DESCARGADOS:\n");
            top10.stream()
                .limit(10)
                .forEach(libro -> {
                    System.out.println("📖 " + libro.getTitulo());
                    System.out.println("👤 " + libroService.obtenerNombresAutores(libro));
                    System.out.println("📊 " + String.format("%.0f", libro.getNumeroDeDescargas()) + " descargas");
                    System.out.println("🌍 " + String.join(", ", libro.getIdiomas()));
                    System.out.println("------------------------");
                });
        } catch (Exception e) {
            System.out.println("❌ Error al obtener top 10: " + e.getMessage() + "\n");
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("\n👤 BÚSQUEDA DE AUTOR POR NOMBRE");
        System.out.println("Ingrese el nombre del autor:");
        var nombreAutor = teclado.nextLine().trim();
        
        if (nombreAutor.isEmpty()) {
            System.out.println("❌ Error: El nombre no puede estar vacío.\n");
            return;
        }

        try {
            System.out.println("🔍 Buscando autor: " + nombreAutor + "...\n");
            List<DatosLibro> libros = libroService.buscarLibrosPorTitulo(nombreAutor);
            
            if (libros.isEmpty()) {
                System.out.println("📭 No se encontraron libros del autor: \"" + nombreAutor + "\"\n");
            } else {
                System.out.println("✅ Libros encontrados del autor:\n");
                libros.forEach(libro -> {
                    System.out.println("📖 " + libro.getTitulo());
                    System.out.println("👤 " + libroService.obtenerNombresAutores(libro));
                    System.out.println("📊 " + String.format("%.0f", libro.getNumeroDeDescargas()) + " descargas");
                    System.out.println("------------------------");
                });
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar autor: " + e.getMessage() + "\n");
        }
    }

    private void mostrarEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS GENERALES");
        
        try {
            System.out.println("🔍 Calculando estadísticas...\n");
            List<DatosLibro> libros = libroService.obtenerTodosLosLibros();
            libroService.mostrarEstadisticas(libros);
        } catch (Exception e) {
            System.out.println("❌ Error al calcular estadísticas: " + e.getMessage() + "\n");
        }
    }

    private String getNombreIdioma(String codigo) {
        return switch (codigo) {
            case "en" -> "inglés";
            case "es" -> "español";
            case "fr" -> "francés";
            case "de" -> "alemán";
            case "pt" -> "portugués";
            default -> codigo;
        };
    }
}