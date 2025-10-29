package com.example.hospital_lito.repositorio;

import com.example.hospital_lito.modelos.Cama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamaRepository extends JpaRepository<Cama, Long> {
    List<Cama> findAllByOrderByIdCamaAsc();
    // Buscar camas por estado
    List<Cama> findByEstadoOrderByIdCamaAsc(String estado);

    // Buscar camas por categoría
    List<Cama> findByCategoria_IdCategoriaOrderByIdCamaAsc(String idCategoria);
}
