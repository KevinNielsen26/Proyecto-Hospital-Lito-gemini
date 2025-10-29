package com.example.hospital_lito.service;

import com.example.hospital_lito.modelos.Asignacion;
import com.example.hospital_lito.modelos.Cama;
import com.example.hospital_lito.modelos.Enfermera;
import com.example.hospital_lito.modelos.Asignacion;
import com.example.hospital_lito.repositorio.AsignacionRepository;
import com.example.hospital_lito.repositorio.CamaRepository;
import com.example.hospital_lito.repositorio.EnfermeraRepository;
import jakarta.persistence.PrePersist;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsignacionService {
    private final AsignacionRepository asignacionRepo;
    private final EnfermeraRepository enfermeraRepo;
    private final CamaRepository camaRepo;

    private static final int MAX_CAMAS_POR_ENFERMERA = 8;

    public List<Asignacion> obtenerActivas() {
        return asignacionRepo.findAll().stream()
                .filter(Asignacion::getActiva)
                .toList();
    }

    public List<Asignacion> obtenerCamasPorEnfermera (Long idEnfermera){
        return asignacionRepo.findByEnfermera_IdEnfermeraAndActivaTrue(idEnfermera);
    }

    public List<Asignacion> obtenerEnfermerasPorCama (Long idCama){
        return asignacionRepo.findByCama_IdCamaAndActivaTrue(idCama);
    }

    @Transactional
    public Asignacion asignarEnfermera(Long idCama, Long idEnfermera){
        Enfermera enfermera = enfermeraRepo.findById(idEnfermera)
                .orElseThrow(() -> new IllegalArgumentException("La enfermera no existe"));

        if(!enfermera.getActiva()){
            throw new IllegalArgumentException("La enfermera no esta activa");
        }

        //verifica que la cama exista
        Cama cama = camaRepo.findById(idCama)
                .orElseThrow(() -> new IllegalArgumentException("La cama no existe"));

        // Verificar si ya existe una asignación activa entre esta enfermera y cama
        List<Asignacion> asignacionesExistentes = asignacionRepo.findByCama_IdCamaAndActivaTrue(idCama);
        boolean yaAsignada = asignacionesExistentes.stream()
                .anyMatch(a -> a.getEnfermera().getIdEnfermera().equals(idEnfermera));

        if (yaAsignada) {
            throw new IllegalStateException(
                    String.format("La enfermera %s %s ya está asignada a la cama %s. " +
                                    "No es posible realizar la misma asignación dos veces.",
                            enfermera.getNombre(), enfermera.getApellido(), cama.getUbicacion())
            );
        }

        //Enfermera no puede tener mas de 8 camas
        Long camasEnfermeras = asignacionRepo.contarAsignacionesActivasEnfermera(idEnfermera);
            if (camasEnfermeras >= MAX_CAMAS_POR_ENFERMERA) {
                throw new IllegalStateException(
                        String.format("La enfermera ya tiene %d camas asignadas. El maximo permitido son %d camas",
                                camasEnfermeras, MAX_CAMAS_POR_ENFERMERA)
                );
            }

        //Camas no pueden tener mas enfermeras que lo permitido
        Long enfermerasCama = asignacionRepo.contarAsignacionesActivasCama(idCama);
        Integer maxEnfermeras = cama.getMaxEnfermerasAsignadas() != null ?
                                cama.getMaxEnfermerasAsignadas() :
                                cama.getCategoria().getMax_cant_enfermeras();

        if(enfermerasCama >= maxEnfermeras){
            throw new IllegalStateException(
                    String.format("La cama ya tiene %d enfermeras asignadas. El maximo permitido son %d enfermeras"
                            , enfermerasCama, maxEnfermeras, cama.getCategoria().getDescripcion())
            );
        }

        //Crear la asignacion
            Asignacion asignacion = new Asignacion();
            asignacion.setCama(cama);
            asignacion.setEnfermera(enfermera);
            asignacion.setActiva(true);
            asignacion.setFechaAsignacion(LocalDateTime.now());
            // La fecha se setea automáticamente con @PrePersist
            return asignacionRepo.save(asignacion);
    }


    @Transactional
    public void desactivar(Integer idAsignacion){
        Asignacion asignacion = asignacionRepo.findById(idAsignacion)
                .orElseThrow(() -> new IllegalArgumentException ("LA ASIGNACION NO EXISTE"));
        asignacion.setActiva(false);
        asignacionRepo.save(asignacion);
    }
}

