package com.alura.literalura.principal;

import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.service.LibroService;

import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private LibroService libroService = new LibroService();

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
        System.out.println("\n📖 BÚSQUEDA DE LIBRO POR TÍTULO");
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var tituloLibro = teclado.nextLine().trim();
        
        if (tituloLibro.isEmpty()) {
            System.out.println("❌ Error: El título no puede estar vacío.\n");
            return;
        }

        try {
            System.out.println("🔍 Buscando en la API de Gutendx...\n");
            List<DatosLibro> libros = libroService.buscarLibrosPorTitulo(tituloLibro);
            
            if (libros.isEmpty()) {
                System.out.println("📭 No se encontraron libros con el título: \"" + tituloLibro + "\"\n");
            } else {
                System.out.println("✅ Se encontraron " + libros.size() + " libro(s):\n");
                libros.stream()
                    .limit(5) // mostrar máximo 5 resultados
                    .forEach(libro -> {
                        libroService.mostrarInformacionLibro(libro);
                        System.out.println();
                    });
                
                if (libros.size() > 5) {
                    System.out.println("📋 Mostrando los primeros 5 resultados de " + libros.size() + " encontrados.\n");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar el libro: " + e.getMessage() + "\n");
        }
    }

    private void listarLibrosRegistrados() {
        System.out.println("\n📚 LIBROS REGISTRADOS");
        System.out.println("Esta funcionalidad se implementará con la base de datos.\n");
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n👤 AUTORES REGISTRADOS");
        System.out.println("Esta funcionalidad se implementará con la base de datos.\n");
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
            
            System.out.println("Esta funcionalidad se implementará con la base de datos.\n");
        } catch (Exception e) {
            System.out.println("❌ Error: Por favor ingrese un año válido.\n");
            teclado.nextLine(); // limpiar buffer
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n🌍 LIBROS POR IDIOMA");
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
            System.out.println("🔍 Buscando libros en " + getNombreIdioma(idioma) + "...\n");
            
            // Por ahora buscaremos algunos libros populares y los filtraremos por idioma
            List<DatosLibro> todosLibros = libroService.obtenerTodosLosLibros();
            List<DatosLibro> librosPorIdioma = libroService.filtrarPorIdioma(todosLibros, idioma);
            
            if (librosPorIdioma.isEmpty()) {
                System.out.println("📭 No se encontraron libros en " + getNombreIdioma(idioma) + ".\n");
            } else {
                System.out.println("✅ Libros en " + getNombreIdioma(idioma) + ":\n");
                librosPorIdioma.stream()
                    .limit(10)
                    .forEach(libro -> {
                        System.out.println("📖 " + libro.getTitulo());
                        System.out.println("👤 " + libroService.obtenerNombresAutores(libro));
                        System.out.println("📊 Descargas: " + libro.getNumeroDeDescargas());
                        System.out.println("------------------------");
                    });
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