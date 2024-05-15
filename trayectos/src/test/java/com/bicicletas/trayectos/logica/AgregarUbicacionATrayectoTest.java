package com.bicicletas.trayectos.logica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bicicletas.trayectos.dataAccess.TrayectosRepository;
import com.bicicletas.trayectos.dataAccess.UbicacionesRepository;
import com.bicicletas.trayectos.modelo.Trayecto;
import com.bicicletas.trayectos.modelo.Ubicacion;

import jakarta.transaction.Transactional;

@SpringBootTest
public class AgregarUbicacionATrayectoTest {

    @Autowired
    TrayectosService servicio;

    @Autowired
    TrayectosRepository trayectos;

    @Autowired
    UbicacionesRepository ubicaciones;

    @BeforeEach
    void borraBaseDatos() {
        ubicaciones.deleteAll();
        trayectos.deleteAll();
    }

    @Test
    @Transactional
    void grabarSinError() {

        try {

            // Arrange -- preparar

            // agrega un trayecto activo (sin finalizar)
            Trayecto trayecto = new Trayecto();
            trayecto.setEnProceso(true);
            trayecto = trayectos.save(trayecto);
        

            // Act -- actuar

            // agrega una ubicación a un trayecto activo
            servicio.agregarUbicacionATrayecto(
                trayecto.getId(), 
                27.0, 
                42.0);
    
            // Assert -- revisar el estado

            // revisa los datos en los objetos de la base de datos
            trayecto = trayectos.findById(trayecto.getId()).get();
            Ubicacion nuevaUbicacion = trayecto.getUbicaciones().get(0);
            
            assertEquals(27.0, nuevaUbicacion.getLongitud());
            assertEquals(42.0, nuevaUbicacion.getLatitud());

        } catch (Exception e) {
            fail("No debio lanzar excepcion");
        }

    }

    @Test
    void grabarEnUnTrayectoQueNoExiste() {

        try {

            // Arrange -- preparar        

            // Act -- actuar

            // agrega una ubicación a un trayecto que no existe
            UUID id = UUID.randomUUID();
            servicio.agregarUbicacionATrayecto(
                id, 
                27.0, 
                42.0);
    
            // Assert -- revisar el estado

            fail("Debio generar excepción");

        } catch (Exception e) {
            // ok
        }

    }

    @Test
    void grabarEnUnTrayectoYaFinalizado() {

        try {

            // Arrange -- preparar

            // crea un trayecto finalizado
            Trayecto trayecto = new Trayecto();
            trayecto.setEnProceso(false);
            trayecto = trayectos.save(trayecto);


            // Act -- actuar

            // agrega una ubicación al trayecto finalizado
            servicio.agregarUbicacionATrayecto(
                trayecto.getId(), 
                27.0, 
                42.0);
    
            // Assert -- revisar el estado

            fail("debio generar excepción");

        } catch (Exception e) {
            // OK
        }

    }

}
