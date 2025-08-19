package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosAutor {
    @JsonAlias("name") 
    private String nombre;
    
    @JsonAlias("birth_year") 
    private Integer fechaDeNacimiento;
    
    @JsonAlias("death_year") 
    private Integer fechaDeDeceso;

    public DatosAutor() {}

    public DatosAutor(String nombre, Integer fechaDeNacimiento, Integer fechaDeDeceso) {
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.fechaDeDeceso = fechaDeDeceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeDeceso() {
        return fechaDeDeceso;
    }

    public void setFechaDeDeceso(Integer fechaDeDeceso) {
        this.fechaDeDeceso = fechaDeDeceso;
    }

    @Override
    public String toString() {
        return "DatosAutor{" +
                "nombre='" + nombre + '\'' +
                ", fechaDeNacimiento=" + fechaDeNacimiento +
                ", fechaDeDeceso=" + fechaDeDeceso +
                '}';
    }
}