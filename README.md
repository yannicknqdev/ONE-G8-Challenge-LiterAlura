# 📚 LiterAlura - Catálogo de Libros

![Java](https://img.shields.io/badge/Java-24-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-blue.svg)
![Maven](https://img.shields.io/badge/Maven-Build-red.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## 📋 Descripción

**LiterAlura** es una aplicación de consola interactiva desarrollada en Java que permite gestionar un catálogo personal de libros. La aplicación consume la API de **Gutendx** (Project Gutenberg) para buscar libros, los almacena en una base de datos PostgreSQL y ofrece diversas funcionalidades para consultar y analizar la información.

## ✨ Características Principales

### 🔍 Funcionalidades de Búsqueda
- **Búsqueda por título**: Busca libros en la API de Gutendx y los guarda automáticamente
- **Búsqueda por autor**: Encuentra libros por nombre del autor
- **Filtrado por idioma**: Lista libros según el idioma (inglés, español, francés, alemán, portugués)

### 📊 Análisis y Estadísticas
- **Top 10 libros más descargados**: Ranking por popularidad
- **Estadísticas por idioma**: Cantidad de libros por idioma
- **Autores vivos en determinado año**: Consulta histórica de autores
- **Estadísticas generales**: Resumen completo del catálogo

### 💾 Gestión de Datos
- **Persistencia en PostgreSQL**: Almacenamiento seguro y eficiente
- **Relaciones entre entidades**: Manejo de libros y autores
- **Prevención de duplicados**: Evita guardar el mismo libro dos veces
- **Consultas avanzadas**: Derived queries y consultas personalizadas

## 🛠️ Tecnologías Utilizadas

- **Java 24**: Lenguaje principal
- **Spring Boot 3.5.4**: Framework de aplicación
- **Spring Data JPA**: Persistencia de datos
- **PostgreSQL**: Base de datos
- **Jackson 2.16.2**: Procesamiento JSON
- **Maven**: Gestión de dependencias
- **Hibernate**: ORM para manejo de entidades

## 🏗️ Arquitectura

```
src/
├── main/java/com/alura/literalura/
│   ├── model/
│   │   ├── Autor.java              # Entidad JPA Autor
│   │   ├── Libro.java              # Entidad JPA Libro
│   │   ├── DatosAutor.java         # DTO para API JSON
│   │   ├── DatosLibro.java         # DTO para API JSON
│   │   └── Resultado.java          # DTO para respuesta API
│   ├── repository/
│   │   ├── AutorRepository.java    # Consultas de autores
│   │   └── LibroRepository.java    # Consultas de libros
│   ├── service/
│   │   ├── ConsumoAPI.java         # Cliente HTTP
│   │   ├── ConvierteDatos.java     # Conversión JSON
│   │   ├── IConvierteDatos.java    # Interfaz conversión
│   │   └── LibroService.java       # Lógica de negocio
│   ├── principal/
│   │   └── Principal.java          # Interfaz de usuario
│   └── LiteraluraApplication.java  # Clase principal
└── resources/
    └── application.properties      # Configuración
```

## 🚀 Instalación y Configuración

### Prerrequisitos
- **Java 24** o superior
- **PostgreSQL 16** o superior
- **Maven 3.6** o superior

### 1. Clonar el repositorio
```bash
git clone https://github.com/yannicknqdev/ONE-G8-Challenge-LiterAlura.git
cd ONE-G8-Challenge-LiterAlura
```

### 2. Configurar PostgreSQL
```sql
-- Crear base de datos
CREATE DATABASE literalura;

-- Crear usuario (opcional)
CREATE USER literalura_user WITH PASSWORD 'alura123';
GRANT ALL PRIVILEGES ON DATABASE literalura TO literalura_user;
```

### 3. Configurar application.properties
```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.format-sql=true

# Logging Configuration
logging.level.root=WARN
logging.level.org.hibernate=ERROR
```

### 4. Ejecutar la aplicación
```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# O compilar JAR y ejecutar
./mvnw clean package
java -jar target/literalura-0.0.1-SNAPSHOT.jar
```

## 📱 Uso de la Aplicación

### Menú Principal
```
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
```

### Ejemplos de Uso

#### 🔍 Buscar un libro
1. Selecciona opción `1`
2. Ingresa el título: `"Pride and Prejudice"`
3. La aplicación buscará en la API y guardará automáticamente

#### 📊 Ver estadísticas por idioma
1. Selecciona opción `5`
2. Elige idioma: `en` (inglés)
3. Ve la cantidad y lista de libros en ese idioma

#### 🗓️ Autores vivos en un año específico
1. Selecciona opción `4`
2. Ingresa año: `1800`
3. Ve qué autores estaban vivos en ese año

## 🗃️ Modelo de Datos

### Entidad Libro
```java
@Entity
@Table(name = "libros")
public class Libro {
    @Id @GeneratedValue
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String titulo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Autor autor;
    
    private String idioma;
    private Double numeroDeDescargas;
}
```

### Entidad Autor
```java
@Entity
@Table(name = "autores")
public class Autor {
    @Id @GeneratedValue
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nombre;
    
    private Integer fechaDeNacimiento;
    private Integer fechaDeDeceso;
    
    @OneToMany(mappedBy = "autor")
    private List<Libro> libros;
}
```

## 🔧 Funcionalidades Técnicas

### Derived Queries
```java
// Buscar libros por idioma
List<Libro> findByIdioma(String idioma);

// Contar libros por idioma
Long countByIdioma(String idioma);

// Top 10 más descargados
List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();
```

### Consultas Personalizadas
```java
@Query("SELECT a FROM Autor a WHERE (a.fechaDeNacimiento <= :ano) " +
       "AND (a.fechaDeDeceso IS NULL OR a.fechaDeDeceso >= :ano)")
List<Autor> buscarAutoresVivosEnAno(Integer ano);
```

### Procesamiento con Streams
```java
// Ordenar libros por descargas
libros.stream()
    .sorted((l1, l2) -> Double.compare(l2.getNumeroDeDescargas(), l1.getNumeroDeDescargas()))
    .limit(10)
    .forEach(this::mostrarLibro);
```

## 🌐 API Utilizada

**Gutendx API** - Project Gutenberg
- **URL Base**: `https://gutendx.com/`
- **Búsqueda**: `https://gutendx.com/books/?search=titulo`
- **Documentación**: [gutendx.com](https://gutendx.com/)

## 🧪 Testing

### Casos de Prueba Recomendados

1. **Búsqueda exitosa**: `"Moby Dick"`
2. **Búsqueda sin resultados**: `"LibroInexistente"`
3. **Autor histórico**: Año `1800` (Jane Austen)
4. **Idiomas**: `en`, `es`, `fr`, `de`, `pt`
5. **Años límite**: `-100`, `3000`

## 🤝 Contribución

1. Fork el proyecto
2. Crea tu rama de características (`git checkout -b feature/NuevaFuncionalidad`)
3. Commit tus cambios (`git commit -m 'Añadir nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/NuevaFuncionalidad`)
5. Abre un Pull Request

## 🙏 Agradecimientos

- **Alura** - Por el desafío de programación
- **Project Gutenberg** - Por la API de libros gratuita
- **Spring Team** - Por el excelente framework

---

⭐ **¡Dale una estrella si te gustó el proyecto!** ⭐