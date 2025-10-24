package com.example.hospital_lito.controlador;

import com.example.hospital_lito.modelos.Cama;
import com.example.hospital_lito.modelos.Enfermera;
import com.example.hospital_lito.service.AsignacionService;
import com.example.hospital_lito.service.CamaService;
import com.example.hospital_lito.service.EnfermeraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;
    private final EnfermeraService enfermeraService;
    private final CamaService camaService;

    // MOSTRAR FORMULARIO PARA ASOCIAR ENFERMERA A CAMA
    @GetMapping("/nueva")
    public String mostrarFormularioAsignacion(Model model) {
        List<Enfermera> enfermeras = enfermeraService.obtenerEnfermeraActiva();
        List<Cama> camas = camaService.obtenerTodas();

        model.addAttribute("enfermeras", enfermeras);
        model.addAttribute("camas", camas);
        model.addAttribute("titulo", "Asignar Enfermera a una cama");
        return "asignaciones/formulario";
    }

    // ASOCIAR ENFERMERA A CAMA
    @PostMapping("/guardar")
    public String guardarAsignacion(@RequestParam Long idCama, @RequestParam Long idEnfermera, RedirectAttributes flash) {
        try {
            asignacionService.asignarEnfermera(idCama, idEnfermera);
            flash.addFlashAttribute("exito", "Enfermera asignada de manera exitosa");
            return "redirect:/camas";
        } catch (IllegalStateException | IllegalArgumentException e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/asignaciones/nueva";
        }catch (Exception e) {
            flash.addFlashAttribute("error", "Error inesperado: " + e.getMessage());
            return "redirect:/asignaciones/nueva";
        }
    }

    // DESACTIVAR ASIGNACIÓN
    @PostMapping("/{id}/desactivar")
    public String desactivarAsignacion(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            asignacionService.desactivar(id);
            flash.addFlashAttribute("exito", "Asignación desactivada de manera exitosa");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/camas";
    }
}

