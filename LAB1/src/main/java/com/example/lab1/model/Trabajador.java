package com.example.lab1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private BigDecimal ingresoMensual;
    private BigDecimal gratificaciones;
    private BigDecimal bonificaciones;
    private Integer mesInicio;
    private Integer mesGratificacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getIngresoMensual() {
        return ingresoMensual;
    }

    public void setIngresoMensual(BigDecimal ingresoMensual) {
        this.ingresoMensual = ingresoMensual;
    }

    public BigDecimal getGratificaciones() {
        return gratificaciones;
    }

    public void setGratificaciones(BigDecimal gratificaciones) {
        this.gratificaciones = gratificaciones;
    }

    public BigDecimal getBonificaciones() {
        return bonificaciones;
    }

    public void setBonificaciones(BigDecimal bonificaciones) {
        this.bonificaciones = bonificaciones;
    }

    public Integer getMesInicio() {
        return mesInicio;
    }

    public void setMesInicio(Integer mesInicio) {
        this.mesInicio = mesInicio;
    }


    public Integer getMesGratificacion() {  // Nuevo método get
        return mesGratificacion;
    }

    public void setMesGratificacion(Integer mesGratificacion) {  // Nuevo método set
        this.mesGratificacion = mesGratificacion;
    }
}


