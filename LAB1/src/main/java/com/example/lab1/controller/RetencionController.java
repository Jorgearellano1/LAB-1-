package com.example.lab1.controller;
import com.example.lab1.model.Retencion;
import com.example.lab1.model.Trabajador;
import com.example.lab1.service.RetencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retenciones")
public class RetencionController {

    @Autowired
    private RetencionService retencionService;

    @PostMapping("/calcular")
    public List<Retencion> calcularRetenciones(@RequestBody Trabajador trabajador) {
        return retencionService.calcularRetenciones(trabajador);
    }

    @GetMapping("/{trabajadorId}")
    public List<Retencion> obtenerRetencionesPorTrabajador(@PathVariable Long trabajadorId) {
        return retencionService.obtenerRetencionesPorTrabajador(trabajadorId);
    }
}


