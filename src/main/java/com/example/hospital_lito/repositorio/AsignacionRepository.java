package com.example.hospital_lito.repositorio;

import com.example.hospital_lito.modelos.Asignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {
    // Buscar asignaciones activas de una enfermera
    List<Asignacion> findByEnfermera_IdEnfermeraAndActivaTrue(Long idEnfermera);

    // Buscar asignaciones activas de una cama
    List<Asignacion> findByCama_IdCamaAndActivaTrue(Long idCama);

    // Contar asignaciones activas de una enfermera
    @Query("SELECT COUNT(a) FROM Asignacion a WHERE a.enfermera.idEnfermera = :idEnfermera AND a.activa = true")
    Long contarAsignacionesActivasEnfermera(@Param("idEnfermera") Long idEnfermera);

    // Contar asignaciones activas de una cama
    @Query("SELECT COUNT(a) FROM Asignacion a WHERE a.cama.idCama = :idCama AND a.activa = true")
    Long contarAsignacionesActivasCama(@Param("idCama") Long idCama);
}
