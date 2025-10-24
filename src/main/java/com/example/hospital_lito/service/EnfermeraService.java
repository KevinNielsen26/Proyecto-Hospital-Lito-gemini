package com.example.hospital_lito.service;


import com.example.hospital_lito.modelos.Enfermera;
import com.example.hospital_lito.repositorio.EnfermeraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnfermeraService {

    private final EnfermeraRepository enfermeraRepo;

    public List<Enfermera> obtenerTodasLasEnfermeras(){
        return enfermeraRepo.findAll();
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

        return enfermeraRepo.save(enfermera);

    }
}
