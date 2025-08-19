package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Resultado(
        @JsonAlias("count") Integer total,
        @JsonAlias("results") List<DatosLibro> resultados
) {
}