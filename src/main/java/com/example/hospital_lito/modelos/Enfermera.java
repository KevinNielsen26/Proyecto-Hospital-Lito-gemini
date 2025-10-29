package com.example.hospital_lito.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Enfermeras" )
@Getter
@Setter
public class Enfermera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enfermera")
    private Long idEnfermera;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Apellido")
    private String apellido;

    @Column(name = "Legajo", unique = true)
    private String legajo;

    @Column(name = "Activa")
    private Boolean activa = true;

    @Transient
    private Integer cantidadCamasAsignadas;

}