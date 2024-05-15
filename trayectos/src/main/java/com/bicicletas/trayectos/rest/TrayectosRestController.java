package com.bicicletas.trayectos.rest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bicicletas.trayectos.dataAccess.TrayectosRepository;
import com.bicicletas.trayectos.logica.TrayectosService;
import com.bicicletas.trayectos.modelo.Trayecto;
import com.bicicletas.trayectos.modelo.Ubicacion;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



// controlador de solicitudes REST
// recibe solicitudes GET, POST, DELETE desde la web
@RestController
@RequestMapping("/api")
public class TrayectosRestController {

    @Autowired
    TrayectosRepository trayectos;

    @Autowired
    TrayectosService servicioTrayectos;

    // ----------------
  
    // GET /trayectos
    // Retornar todos los trayectos

    // Necesita un DTO (mensaje) con los campos que queremos retornar
    @Data
    @NoArgsConstructor
    static class TrayectoDTO {
        UUID id;
        Date horaInicio;
        Date horaFin;
        boolean enProceso;
    }

    @GetMapping("/trayectos")
    public List<Trayecto> retornaTodosLosTrayectos() 
        throws Exception 
    {
        return trayectos.findAll();
        // throw new Exception("Funcion no implementada");
    }

    // ----------------
    
    // GET /trayectos/{id}
    // Retornar el trayecto con el id = {id}
    @GetMapping("/trayectos/{id}")
    public Trayecto retornaTrayectoPorId(@PathVariable String id) 
        throws Exception 
    {
        // generamos un UUID a partir del parámetro
        UUID trayectoID = UUID.fromString(id);

        // buscamos en la base de datos
        Optional<Trayecto> resultadoTrayecto = trayectos.findById(trayectoID);
        
        // generamos una excepción si no se encuentra
        if ( resultadoTrayecto.isEmpty())
            throw new Exception("no se encontró el trayecto");

        // retornamos el trayecto
        return resultadoTrayecto.get();

    }

    // ----------------

    // POST /trayectos
    // Agrega un nuevo trayecto

    // requiere un DTO (un mensaje) con longitud y latitud
    @Data
    @NoArgsConstructor
    static class AgregaTrayectoRequest {
        Double longitud;
        Double latitud;
    }

    @PostMapping("/trayectos")   
    public UUID agregaTrayecto(@RequestBody AgregaTrayectoRequest solicitud) 
        throws Exception
    {
        return servicioTrayectos.iniciarTrayecto(
            solicitud.getLongitud(), 
            solicitud.getLatitud()
        );    
    }

    // ----------------

    // GET /trayectos/{id}/ubicaciones
    // Retorna las ubicaciones del trayecto con el id = {id}
    @GetMapping("/trayectos/{id}/ubicaciones")
    public List<Ubicacion> retornaUbicacionesPorTrayecto(@RequestParam String id)
        throws Exception 
    {
        UUID idTrayecto = UUID.fromString(id);
        Optional<Trayecto> resultadoTrayecto = trayectos.findById(idTrayecto);

        if (resultadoTrayecto.isEmpty())
            throw new Exception("Trayecto no existe");

        return resultadoTrayecto.get().getUbicaciones();
    }
    

    // ----------------

    // POST /trayectos/{id}/ubicaciones
    // Agrege una ubicacion al trayecto con id = {id}

    // requiere un DTO (un mensaje) con el id del trayecto, longitud y latitud
    @Data
    @NoArgsConstructor
    static class AgregaUbicacionATrayectoRequest {
        Double longitud;
        Double latitud;
    }

    @PostMapping("/trayectos/{id}/ubicaciones")
    public UUID agregaUbicacionATrayecto(
        @PathVariable String id,
        @RequestBody AgregaUbicacionATrayectoRequest solicitud
        ) throws Exception 
    {

        UUID trayectoId = UUID.fromString(id);
        return servicioTrayectos.agregarUbicacionATrayecto(
            trayectoId, 
            solicitud.getLongitud(), 
            solicitud.getLatitud()
        );

    }



}
