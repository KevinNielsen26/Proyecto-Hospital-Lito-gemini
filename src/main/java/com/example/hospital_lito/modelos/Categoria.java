package com.example.hospital_lito.modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Categorias")
@Getter
@Setter
public class Categoria {
    @Id
    @Column(name = "id_categoria")
    private String idCategoria;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "max_cant_enfermeras")
    private int max_cant_enfermeras;

}