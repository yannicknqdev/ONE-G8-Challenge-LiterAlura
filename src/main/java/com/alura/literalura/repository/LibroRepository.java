package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    // Buscar libro por título exacto
    Optional<Libro> findByTituloIgnoreCase(String titulo);
    
    // Buscar libros por idioma
    List<Libro> findByIdiomaContainingIgnoreCase(String idioma);
    
    // Buscar libros por idioma exacto
    List<Libro> findByIdioma(String idioma);
    
    // Buscar top 10 libros más descargados
    List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();
    
    // Contar libros por idioma
    Long countByIdioma(String idioma);
    
    // Consulta personalizada para buscar por título que contenga la palabra
    @Query("SELECT l FROM Libro l WHERE l.titulo ILIKE %:titulo%")
    List<Libro> buscarPorTituloContiene(String titulo);
    
    // Verificar si existe un libro con ese título
    Boolean existsByTituloIgnoreCase(String titulo);
}