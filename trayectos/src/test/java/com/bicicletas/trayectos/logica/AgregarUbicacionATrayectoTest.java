package com.bicicletas.trayectos.logica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    @Transactional
    void grabarSinError() {

        // Arrange -- preparar

        Trayecto trayecto = new Trayecto();
        trayecto.setEnProceso(true);
        trayecto = trayectos.save(trayecto);

        try {

            // Act -- actuar
            servicio.agregarUbicacionATrayecto(
                trayecto.getId(), 
                27.0, 
                42.0);
    
            // Assert -- revisar el estado

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

    }

    @Test
    void grabarEnUnTrayectoYaFinalizado() {

    }

}
