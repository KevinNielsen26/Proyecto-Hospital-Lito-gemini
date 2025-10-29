package com.example.hospital_lito.repositorio;

import com.example.hospital_lito.modelos.Enfermera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnfermeraRepository extends JpaRepository<Enfermera, Long> {
    List<Enfermera> findByActivaTrue();
    List<Enfermera> findAllByOrderByIdEnfermeraAsc();

}
