package com.example.hospital_lito.controlador; // <-- CORRECCIÓN 1: Paquete ajustado a tu proyecto.

import com.example.hospital_lito.modelos.Asignacion;
import com.example.hospital_lito.modelos.Enfermera;
import com.example.hospital_lito.service.AsignacionService;
import com.example.hospital_lito.service.EnfermeraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/enfermeras")
@RequiredArgsConstructor
public class EnfermeraController {

    // Servicios que este controlador va a necesitar.
    private final EnfermeraService enfermeraService;
    private final AsignacionService asignacionService;

    /**
     * REQUISITO 4: Visualizar todas las enfermeras.
     */
    @GetMapping
    public String listarEnfermeras(Model model) {
        // Llama al servicio para obtener la lista de todas las enfermeras.
        List<Enfermera> enfermeras = enfermeraService.obtenerTodasLasEnfermeras();

        // Agregamos la lista y un título al modelo para que la vista HTML los pueda usar.
        model.addAttribute("enfermeras", enfermeras);
        model.addAttribute("titulo", "Lista de Enfermeras");

        // Devuelve el archivo que se encuentra en /templates/enfermeras/lista.html
        return "enfermeras/lista";
    }

    /**
     * REQUISITO 6: Agregar una enfermera (Parte 1 - Mostrar el formulario).
     * Se activa con una petición GET a "/enfermeras/nueva".
     */
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaEnfermera(Model model) {
        // Pasamos un objeto Enfermera nuevo y vacío para que el formulario lo pueda llenar
        model.addAttribute("enfermera", new Enfermera());
        model.addAttribute("titulo", "Nueva Enfermera");


        return "enfermeras/formulario";
    }

    /**
     * REQUISITO 6: Agregar una enfermera (Parte 2 - Procesar y guardar los datos).
     * Se activa con una petición POST desde el formulario a "/enfermeras/guardar".
     */
    @PostMapping("/guardar")
    public String guardarEnfermera(@ModelAttribute Enfermera enfermera, RedirectAttributes flash) {
        try {
            // Llama al método del servicio para guardar la enfermera
            enfermeraService.agregarEnfermera(enfermera);

            // Si todo sale bien, mandamos un mensaje de éxito a la siguiente pantalla.
            flash.addFlashAttribute("exito", "Enfermera agregada de manera exitosa");
        } catch (IllegalArgumentException e) {
            // Si el servicio lanza un error (ej: campo vacío), lo capturamos y mostramos.
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        // Redirigimos al usuario a la lista de enfermeras para que vea el resultado.
        return "redirect:/enfermeras";
    }

    /**
     * REQUISITO 5: Visualizar las camas que atiende una enfermera.
     * Se activa con GET a, por ejemplo, "/enfermeras/3/camas"
     * @PathVariable extrae el ID (el número 3 en el ejemplo) de la URL
     */
    @GetMapping("/{id}/camas")
    public String verCamasDeEnfermera(@PathVariable Long id, Model model) {
        // Buscamos a la enfermera por su ID. Si no existe, lanza una excepción
        Enfermera enfermera = enfermeraService.obtenerEnfermeaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Enfermera no encontrada con ID: " + id));

        // Usamos el servicio de asignaciones para obtener la lista de camas asociadas a esa enfermera.
        List<Asignacion> asignaciones = asignacionService.obtenerCamasPorEnfermera(id);

        // Pasamos todos los datos necesarios a la vista.
        model.addAttribute("enfermera", enfermera);
        model.addAttribute("asignaciones", asignaciones);
        model.addAttribute("titulo", "Camas atendidas por " + enfermera.getNombre() + " " + enfermera.getApellido());

        // Devuelve el archivo en /templates/enfermeras/camas.html
        return "enfermeras/camas";
    }

    /**
     * Eliminar una enfermera
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarEnfermera(@PathVariable Long id, RedirectAttributes flash) {
        try {
            enfermeraService.eliminarEnfermera(id);
            flash.addFlashAttribute("exito", "Enfermera eliminada exitosamente");
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/enfermeras";
    }

    /**
     * Cambiar estado de una enfermera (Activar/Desactivar)
     */
    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstadoEnfermera(@PathVariable Long id,
                                         @RequestParam Boolean nuevoEstado,
                                         RedirectAttributes flash) {
        try {
            enfermeraService.cambiarEstado(id, nuevoEstado);
            String mensaje = nuevoEstado ? "Enfermera activada exitosamente" : "Enfermera desactivada exitosamente";
            flash.addFlashAttribute("exito", mensaje);
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/enfermeras";
    }

}