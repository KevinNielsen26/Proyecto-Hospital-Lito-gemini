package com.example.hospital_lito.repositorio;

import com.example.hospital_lito.modelos.Cama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamaRepository extends JpaRepository<Cama, Long> {
    List<Cama> findAllByOrderByIdCamaAsc();
    // Buscar camas por estado
    List<Cama> findByEstadoOrderByIdCamaAsc(String estado);

    // Buscar camas por categoría
    List<Cama> findByCategoria_IdCategoriaOrderByIdCamaAsc(String idCategoria);

    // Query personalizada con JPQL
    @Query("SELECT c FROM Cama c WHERE c.categoria.idCategoria = :idCategoria AND c.estado = :estado")
    List<Cama> buscarPorCategoriaYEstado(
            @Param("idCategoria") String idCategoria,
            @Param("estado") String estado
    );

    @Query(value = "SELECT * FROM v_resumen_camas", nativeQuery = true)
    List<Object[]> obtenerResumenCamas();


}
