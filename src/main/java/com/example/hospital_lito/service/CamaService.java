package com.example.hospital_lito.service;

import com.example.hospital_lito.modelos.Cama;
import com.example.hospital_lito.modelos.Categoria;
import com.example.hospital_lito.repositorio.AsignacionRepository;
import com.example.hospital_lito.repositorio.CamaRepository;
import com.example.hospital_lito.repositorio.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CamaService {

    private final CamaRepository camaRepo;
    private final CategoriaRepository categoriaRepo;
    private final AsignacionRepository asignacionRepo;

    public List<Cama> obtenerTodas(){
        // 1. Obtiene todas las camas
        List<Cama> camas = camaRepo.findAllByOrderByIdCamaAsc();

        // --- INICIO DE MODIFICACIÓN ---
        // 2. Itera sobre cada cama para calcular sus enfermeras activas
        for (Cama cama : camas) {
            // Usa el método del repositorio para contar
            Long conteo = asignacionRepo.contarAsignacionesActivasCama(cama.getIdCama());
            // Asigna el valor al campo transitorio
            cama.setCantidadEnfermeras(conteo.intValue());
        }
        // 3. Devuelve la lista de camas CON el conteo
        return camas;
    }

    public Optional<Cama> obtenerCamaPorId(Long id){
        return camaRepo.findById(id);
    }

    public List<Cama> obtenerCamaPorEstado(String estado){
        return camaRepo.findByEstadoOrderByIdCamaAsc(estado);
    }

    @Transactional
    public Cama agregarCama(Cama cama){

        if(cama.getCategoria() == null || cama.getCategoria().getIdCategoria() == null){
            throw new IllegalArgumentException("El categoria es obligatorio");
        }

        Categoria categoria = categoriaRepo.findById(cama.getCategoria().getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));

        cama.setCategoria(categoria);

        if(cama.getUbicacion() == null || cama.getUbicacion().trim().isEmpty()){
            throw new IllegalArgumentException("Ubicacion no encontrada");
        }

        if(cama.getEstado() == null){
            cama.setEstado("libre");
        }

        if(cama.getEstado().equals("libre") && cama.getIdhc() != null){
            throw new IllegalArgumentException("La cama esta libre, no puede tener IDHC");
        }

        if(cama.getEstado().equals("ocupada") && cama.getIdhc() == null){
            throw new IllegalArgumentException("La cama esta ocupada, debe tener IDHC");
        }


        return camaRepo.save(cama);
    }

    //asignacion de cama a un paciente
    @Transactional
        public Cama asignarPaciente(Long idCama, Integer idhc){

        //si no se encuentra la cama por id, lanza excepcion
        Cama cama = camaRepo.findById(idCama)
                .orElseThrow(() -> new IllegalArgumentException("La cama no existe"));

        //si la cama no esta libre
        if(!cama.getEstado().equals("libre")){
            throw new IllegalArgumentException("La cama no esta libre");
        }

        if(idhc == null || idhc <= 0){
            throw new IllegalArgumentException("El IDHC es invalido");
        }

        cama.setEstado("ocupada");
        cama.setIdhc(idhc);

        return camaRepo.save(cama);
    }

    @Transactional
    public Cama liberarCama(Long idCama){
        Cama cama = camaRepo.findById(idCama)
                .orElseThrow(() -> new IllegalArgumentException("La cama no existe"));

        cama.setEstado("libre");
        cama.setIdhc(null);

        return camaRepo.save(cama);
    }

    @Transactional
    public void eliminarCama(Long id) {
        Cama cama = camaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cama no encontrada"));

        // Verificar si está ocupada
        if ("ocupada".equals(cama.getEstado())) {
            throw new IllegalStateException(
                    "No se puede eliminar la cama porque está ocupada por un paciente. " +
                            "Por favor, primero libere la cama."
            );
        }

        // Verificar si tiene asignaciones activas
        Long asignacionesActivas = asignacionRepo.contarAsignacionesActivasCama(id);
        if (asignacionesActivas > 0) {
            throw new IllegalStateException(
                    "No se puede eliminar la cama porque tiene " + asignacionesActivas +
                            " enfermera(s) asignada(s). Por favor, primero desasigne todas las enfermeras."
            );
        }

        camaRepo.delete(cama);
    }

}
