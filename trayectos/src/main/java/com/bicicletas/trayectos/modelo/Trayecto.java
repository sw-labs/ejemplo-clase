package com.bicicletas.trayectos.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trayecto {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    Date horaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    Date horaFin;

    Long duracion;

    boolean enProceso = false;

    @OneToMany(mappedBy = "trayecto", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Ubicacion> ubicaciones = new ArrayList<>();

}
