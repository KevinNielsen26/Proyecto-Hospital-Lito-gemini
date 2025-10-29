package com.example.hospital_lito.service;


import com.example.hospital_lito.modelos.Enfermera;
import com.example.hospital_lito.repositorio.AsignacionRepository;
import com.example.hospital_lito.repositorio.EnfermeraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnfermeraService {

    private final EnfermeraRepository enfermeraRepo;
    private final AsignacionRepository asignacionRepo;

    public List<Enfermera> obtenerTodasLasEnfermeras(){
        return enfermeraRepo.findAllByOrderByIdEnfermeraAsc();
    }

    public List<Enfermera> obtenerEnfermeraActiva(){
        return enfermeraRepo.findByActivaTrue();
    }

    public Optional<Enfermera> obtenerEnfermeaPorId(Long id){
        return enfermeraRepo.findById(id);
    }

    @Transactional
    public Enfermera agregarEnfermera(Enfermera enfermera){
        if(enfermera.getNombre() == null || enfermera.getNombre().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if(enfermera.getApellido() == null || enfermera.getApellido().isEmpty()){
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if(enfermera.getLegajo() == null || enfermera.getLegajo().isEmpty()){
            throw new IllegalArgumentException("El legajo es obligatorio");
        }
        if(enfermera.getActiva() == null){
            enfermera.setActiva(true);
        }
        try {
            return enfermeraRepo.save(enfermera);
        } catch (DataIntegrityViolationException e) {
            // Captura el error de duplicidad de legajo
            throw new IllegalArgumentException("Ya existe una enfermera con ese número de legajo");
        }
    }

    @Transactional
    public void eliminarEnfermera(Long id) {
        Enfermera enfermera = enfermeraRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enfermera no encontrada"));

        // Verificar si tiene asignaciones activas
        Long asignacionesActivas = asignacionRepo.contarAsignacionesActivasEnfermera(id);
        if (asignacionesActivas > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar la enfermera porque tiene " + asignacionesActivas +
                            " cama(s) asignada(s). Por favor, primero desasigne todas sus camas."
            );
        }

        enfermeraRepo.delete(enfermera);
    }

    @Transactional
    public Enfermera cambiarEstado(Long id, Boolean nuevoEstado) {
        Enfermera enfermera = enfermeraRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enfermera no encontrada"));

        // Si se está desactivando, verificar que no tenga asignaciones activas
        if (!nuevoEstado) {
            Long asignacionesActivas = asignacionRepo.contarAsignacionesActivasEnfermera(id);
            if (asignacionesActivas > 0) {
                throw new IllegalStateException(
                        "No se puede desactivar la enfermera porque tiene " + asignacionesActivas +
                                " cama(s) asignada(s). Por favor, primero desasigne todas sus camas."
                );
            }
        }

        enfermera.setActiva(nuevoEstado);
        return enfermeraRepo.save(enfermera);
    }
}
