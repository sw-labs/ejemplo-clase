package com.bicicletas.trayectos.logica;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bicicletas.trayectos.dataAccess.TrayectosRepository;
import com.bicicletas.trayectos.dataAccess.UbicacionesRepository;
import com.bicicletas.trayectos.modelo.Trayecto;
import com.bicicletas.trayectos.modelo.Ubicacion;

// Controlador de casos de uso
// tiene métodos, uno por cada caso de uso
@Service
public class TrayectosService {

    @Autowired
    TrayectosRepository trayectos;

    @Autowired
    UbicacionesRepository ubicaciones;

    // CU001 Iniciar Trayecto
    // 1. Actor ingresa la ubicación actual
    public UUID iniciarTrayecto(Double longitud, Double latitud) 
        throws Exception
    {

        // 2. Verifica que no exista otro trayecto activo
        Trayecto trayectoActivo = trayectos.findByEnProcesoTrue();
        if (trayectoActivo != null) {
            throw new Exception("No se puede iniciar otro trayecto mientras se tiene un trayecto activo");
        }

        // 3. Determina fecha y hora 
        Date fechaActual = new Date();

        // 4. Determina un id para un nuevo trayecto
        // 5. Almacena un nuevo trayecto con el id, fecha y hora de inicio, y longitud y latitud de ubicación inicial |
        Trayecto trayecto = new Trayecto();
        trayecto.setHoraInicio(fechaActual);
        trayecto.setEnProceso(true);
        trayecto = trayectos.save(trayecto);
        System.out.println("id = " + trayecto.getId());

        // 6. Agrega una ubicación con la longitud y latitud de ubicación inicial a la trayectoria
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLongitud(longitud);
        ubicacion.setLatitud(latitud);

        trayecto.getUbicaciones().add(ubicacion);
        ubicacion.setTrayecto(trayecto);
            
        trayecto = trayectos.save(trayecto);
        ubicacion = ubicaciones.save(ubicacion);
        System.out.println("id = " + trayecto.getId());

        // 7. Retorna el id del nuevo trayecto |
        return trayecto.getId();

    }


    // CU002 Agregar ubicacion al Trayecto
    // 1. Ingresa el id del trayecto en curso
    // 4. Ingresa la longitud y la latitud de la ubicación actual
    public void agregarUbicacionATrayecto(
        UUID idTrayecto,
        Double longitud,
        Double latitud
        ) throws Exception
    {

        // 2. verifica que exista un trayecto con ese id
        Optional<Trayecto> optionalTrayecto 
            = trayectos.findById(idTrayecto);
        if (optionalTrayecto.isEmpty()) {
            throw new Exception("No existe ese trayecto");
        }

        // 3. verifica que el trayecto esté activo 
        Trayecto trayecto = optionalTrayecto.get();
        if (!trayecto.isEnProceso()) {
            throw new Exception("No se puede agregar ubicacion a un trayecto ya finalizado");
        }

        // 5. Determina fecha y hora
        Date fechaYHora = new Date();

        // 6. Agrega una nueva ubicación con fecha y hora actual 
        // y la longitud y latitud de la ubicación al trayecto en curso
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setHora(fechaYHora);
        ubicacion.setLongitud(longitud);
        ubicacion.setLatitud(latitud);

        trayecto.getUbicaciones().add(ubicacion);
        ubicacion.setTrayecto(trayecto);

        trayectos.save(trayecto);
        ubicaciones.save(ubicacion);

    }

}
