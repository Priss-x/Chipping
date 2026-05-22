package com.chipping.notificaciones.controller;


import com.chipping.notificaciones.dto.NotificacionDTO;
import com.chipping.notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService service;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody NotificacionDTO notificacionDTO){
        return ResponseEntity.ok(service.guardar(notificacionDTO.getTipo(), notificacionDTO.getMensaje()));
    }

    @PostMapping("/stock/{id}")
    public void revisar(@PathVariable Long id) {
        service.notificarStockBajo(id);
    }


}
