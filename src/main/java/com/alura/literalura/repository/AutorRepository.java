package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    // Buscar autor por nombre exacto
    Optional<Autor> findByNombreIgnoreCase(String nombre);
    
    // Buscar autores que contengan parte del nombre
    List<Autor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar autores vivos en un año determinado
    @Query("SELECT a FROM Autor a WHERE (a.fechaDeNacimiento <= :ano) AND (a.fechaDeDeceso IS NULL OR a.fechaDeDeceso >= :ano)")
    List<Autor> buscarAutoresVivosEnAno(Integer ano);
    
    // Buscar autores por rango de fechas de nacimiento
    List<Autor> findByFechaDeNacimientoBetween(Integer fechaInicio, Integer fechaFin);
    
    // Verificar si existe un autor con ese nombre
    Boolean existsByNombreIgnoreCase(String nombre);
}