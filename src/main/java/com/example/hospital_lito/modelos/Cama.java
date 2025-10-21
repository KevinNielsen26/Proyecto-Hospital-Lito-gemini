package com.example.hospital_lito.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Camas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cama")
    private Integer idCama;

    @Column(name = "id_categoria", nullable = false)
    private String idCategoria;

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @Column(name = "estado", nullable = false)
    private String estado = "libre";

    @Column(name = "idhc")
    private Integer idhc;

    @ManyToOne
    @JoinColumn(name = "id_categoria", insertable = false, updatable = false)
    private Categoria categoria;

    @Transient
    private Integer cantidadEnfermeras;

}
