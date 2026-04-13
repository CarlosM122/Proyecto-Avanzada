package uq.sistemagestionsolicitudes.controller;

import uq.sistemagestionsolicitudes.dto.ResumenIAResponse;
import uq.sistemagestionsolicitudes.dto.SugerenciaIAResponse;
import uq.sistemagestionsolicitudes.service.IAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ia")
public class IAController {

    @Autowired
    private IAService iaService;

    @PostMapping("/clasificar")
    public SugerenciaIAResponse clasificar(@RequestBody String descripcion) {
        return iaService.sugerirClasificacion(descripcion);
    }

    @GetMapping("/resumen/{id}")
    public ResumenIAResponse resumen(@PathVariable Long id) {
        return iaService.generarResumen(id);
    }
}