package com.bicicletas.trayectos.rest;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class Ejemplo {

    List<String> nombres = new ArrayList<>();

    // GET /api/ejemplo
    @GetMapping("/api/ejemplo")
    public List<String> getMethodName() {
        return nombres;
    }

    @GetMapping("/api/ejemplo/{id}")
    public String getMethodName(@PathVariable int id) {
        return nombres.get(id);
    }
    

    // ----

    // POST /api/ejemplo
    // mensaje
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class AgregarNombreRequest {
        String nombre;
    }
    
    @PostMapping("/api/ejemplo")
    public int agregarNombre(@RequestBody AgregarNombreRequest solicitud) {
        nombres.add(solicitud.getNombre());
        return nombres.size();
    }
    

    // ---

    // PUT
    // --> Update
    @PutMapping("/api/ejemplo/{id}")
    public int putMethodName(@PathVariable int id, @RequestBody AgregarNombreRequest solicitud) {
        nombres.set(id, solicitud.getNombre());
        return id;
    }

    // DELETE
    @DeleteMapping("/api/ejemplo/{numero}")
    public int borrar(@PathVariable int numero) {
        nombres.remove(numero);
        return numero;
    }


}
