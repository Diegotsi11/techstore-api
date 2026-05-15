package cl.techstore.api.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String categoria;
    // Omitimos el campo "activo" porque el cliente de la API no necesita saber si está borrado lógicamente o no
}