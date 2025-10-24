package com.example.hospital_lito.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="Asignaciones")
@Getter
@Setter
public class Asignacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_asignacion")
    private Long idAsignacion;

    @ManyToOne
    @JoinColumn (name="id_cama",nullable=false)
    private Cama cama;

    @ManyToOne
    @JoinColumn (name="id_enfermera",nullable = false)
    private Enfermera enfermera;

    @Column(name="fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name="activa")
    private Boolean activa;

}
