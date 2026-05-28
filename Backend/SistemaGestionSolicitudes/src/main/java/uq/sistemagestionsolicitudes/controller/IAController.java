package uq.sistemagestionsolicitudes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uq.sistemagestionsolicitudes.dto.ResumenIAResponse;
import uq.sistemagestionsolicitudes.dto.SugerenciaIAResponse;
import uq.sistemagestionsolicitudes.service.IAService;

@RestController
@RequestMapping("/ia")
@RequiredArgsConstructor
public class IAController {

    private final IAService iaService;

    @PostMapping("/clasificar")
    public SugerenciaIAResponse clasificar(
            @RequestBody String descripcion
    ) {
        return iaService.sugerirClasificacion(descripcion);
    }

    @GetMapping("/resumen/{id}")
    public ResumenIAResponse resumen(
            @PathVariable Long id
    ) {
        return iaService.generarResumen(id);
    }
}