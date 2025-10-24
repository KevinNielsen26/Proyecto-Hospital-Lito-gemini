package com.example.hospital_lito.service;


import com.example.hospital_lito.modelos.Categoria;
import com.example.hospital_lito.repositorio.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepo;

    public List<Categoria> obtenerTodas(){
        return categoriaRepo.findAll();
    }

    public Optional<Categoria> obtenerPorId(String id){
        return categoriaRepo.findById(id);
    }
}
