package com.example.hospital_lito.controlador;

import com.example.hospital_lito.modelos.Asignacion;
import com.example.hospital_lito.modelos.Cama;
import com.example.hospital_lito.modelos.Categoria;
import com.example.hospital_lito.service.AsignacionService;
import com.example.hospital_lito.service.CamaService;
import com.example.hospital_lito.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/camas")
@RequiredArgsConstructor
public class CamaController {

    private final CamaService camaService;
    private final CategoriaService categoriaService;
    private final AsignacionService asignacionService;

    @GetMapping
    public String ListarCamas(Model model) {
        List<Cama> camas = camaService.obtenerTodas();
        model.addAttribute("camas", camas);
        model.addAttribute("titulo", "Lista de camas");
        return "camas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioCama(Model model){
        Cama cama = new Cama();
        List<Categoria> categorias = categoriaService.obtenerTodas();

        model.addAttribute("cama", cama);
        model.addAttribute("categorias", categorias);
        model.addAttribute("titulo", "Nueva cama");

        return "camas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCama(@ModelAttribute Cama cama, RedirectAttributes flash){
        try{
            camaService.agregarCama(cama);
            flash.addFlashAttribute("exito", "Cama agregada con exito");
        }catch (IllegalArgumentException e){
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/camas";
    }

    @GetMapping("/{id}/asignar-paciente")
    public String mostrarFormularioPaciente(@PathVariable Long id, Model model){
        Cama cama = camaService.obtenerCamaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cama no encontrada"));

        if (!cama.getEstado().equals("libre")){
            throw new IllegalStateException("La cama no esta libre");
        }

        model.addAttribute("cama", cama);
        model.addAttribute("titulo", "Asignar Paciente");

        return("camas/asignar-paciente");
    }

    @PostMapping("/{id}/asignar-paciente")
    public String asignarPaciente(@PathVariable Long id, @RequestParam Integer idhc, RedirectAttributes flash){
        try{
            camaService.asignarPaciente(id,idhc);
            flash.addFlashAttribute("exito", "Paciente asignado de forma exitosa");
        }catch (Exception e){
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/camas";
    }

    @PostMapping("/{id}/liberar")
    public String liberarCama(@PathVariable Long id, RedirectAttributes flash){
        try{
            camaService.liberarCama(id);
            flash.addFlashAttribute("exito", "Cama liberada de manera exitosa");
        }catch(Exception e){
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/camas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarCama(@PathVariable Long id, RedirectAttributes flash){
        try{
            camaService.eliminarCama(id);
            flash.addFlashAttribute("exito", "Cama eliminada exitosamente");
        }catch(IllegalStateException e){
            flash.addFlashAttribute("error", e.getMessage());
        }catch(IllegalArgumentException e){
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/camas";
    }

    @GetMapping("/{id}/enfermeras")
    public String verEnfermerasDeCama(@PathVariable Long id, Model model){
        Cama cama = camaService.obtenerCamaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cama no encontrada"));

        List<Asignacion> asignaciones = asignacionService.obtenerEnfermerasPorCama(id);

        model.addAttribute("cama", cama);
        model.addAttribute("asignaciones", asignaciones);
        model.addAttribute("titulo", "Enfermeras asignadas a " + cama.getUbicacion());

        return "camas/enfermeras";
    }

}