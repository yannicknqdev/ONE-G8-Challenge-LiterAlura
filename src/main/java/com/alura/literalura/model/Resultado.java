package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resultado {
    @JsonAlias("count") 
    private Integer total;
    
    @JsonAlias("results") 
    private List<DatosLibro> resultados;

    public Resultado() {}

    public Resultado(Integer total, List<DatosLibro> resultados) {
        this.total = total;
        this.resultados = resultados;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<DatosLibro> getResultados() {
        return resultados;
    }

    public void setResultados(List<DatosLibro> resultados) {
        this.resultados = resultados;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "total=" + total +
                ", resultados=" + resultados +
                '}';
    }
}