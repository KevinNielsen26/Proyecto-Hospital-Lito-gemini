package com.example.hospital_lito.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Camas")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Getter       No es necesario, @Data ya lo incluye
//@Setter       No es necesario, @Data ya lo incluye
public class Cama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cama")
    private Long idCama;

    @Column(name = "ubicacion", nullable = false)
    private String ubicacion;

    @Column(name = "estado", nullable = false)
    private String estado = "libre";

    @Column(name = "idhc")
    private Integer idhc;

    @ManyToOne()
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "max_enfermeras_asignadas")
    private Integer maxEnfermerasAsignadas;

    @Transient
    private Integer cantidadEnfermeras;

}
