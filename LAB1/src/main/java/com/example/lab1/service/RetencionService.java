package com.example.lab1.service;

import com.example.lab1.model.Retencion;
import com.example.lab1.model.Trabajador;
import com.example.lab1.repisotory.RetencionRepository;
import com.example.lab1.repisotory.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RetencionService {

    @Autowired
    private RetencionRepository retencionRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    public BigDecimal calcularRemuneracionBrutaAnual(Trabajador trabajador) {
        BigDecimal ingresoMensual = trabajador.getIngresoMensual();
        BigDecimal gratificaciones = trabajador.getGratificaciones() != null ? trabajador.getGratificaciones() : BigDecimal.ZERO;
        BigDecimal bonificaciones = trabajador.getBonificaciones() != null ? trabajador.getBonificaciones() : BigDecimal.ZERO;

        BigDecimal remuneracionBrutaAnual = ingresoMensual.multiply(new BigDecimal(12))
                .add(gratificaciones)
                .add(bonificaciones);

        return remuneracionBrutaAnual;
    }

    public BigDecimal calcularRemuneracionNetaAnual(BigDecimal remuneracionBrutaAnual) {
        BigDecimal sieteUIT = new BigDecimal(4600 * 7);
        BigDecimal remuneracionNetaAnual = remuneracionBrutaAnual.subtract(sieteUIT);
        return remuneracionNetaAnual.compareTo(BigDecimal.ZERO) > 0 ? remuneracionNetaAnual : BigDecimal.ZERO;
    }

    public BigDecimal calcularImpuestoAnualProyectado(BigDecimal remuneracionNetaAnual) {
        BigDecimal impuestoAnual = BigDecimal.ZERO;
        if (remuneracionNetaAnual.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal tasa15 = new BigDecimal("0.15");
            BigDecimal tasa21 = new BigDecimal("0.21");
            BigDecimal tasa30 = new BigDecimal("0.30");

            if (remuneracionNetaAnual.compareTo(new BigDecimal(27600)) <= 0) {
                impuestoAnual = remuneracionNetaAnual.multiply(tasa15);
            } else if (remuneracionNetaAnual.compareTo(new BigDecimal(62100)) <= 0) {
                impuestoAnual = remuneracionNetaAnual.subtract(new BigDecimal(27600)).multiply(tasa21).add(new BigDecimal(4140)); // 27600 * 0.15
            } else {
                impuestoAnual = remuneracionNetaAnual.subtract(new BigDecimal(62100)).multiply(tasa30).add(new BigDecimal(13371)); // (62100-27600) * 0.21 + 4140
            }
        }
        return impuestoAnual;
    }

    public List<Retencion> calcularRetenciones(Trabajador trabajador) {
        Trabajador trabajadorGuardado = trabajadorRepository.save(trabajador);

        BigDecimal remuneracionBrutaAnual = trabajador.getIngresoMensual().multiply(new BigDecimal(12));
        BigDecimal remuneracionNetaAnual = calcularRemuneracionNetaAnual(remuneracionBrutaAnual);
        BigDecimal impuestoAnual = calcularImpuestoAnualProyectado(remuneracionNetaAnual);

        BigDecimal retencionMensualBase = impuestoAnual.divide(new BigDecimal(12), BigDecimal.ROUND_HALF_UP);

        List<Retencion> retenciones = new ArrayList<>();

        for (int mes = trabajador.getMesInicio(); mes <= 12; mes++) {
            BigDecimal retencionMensual = retencionMensualBase;

            if (mes == trabajador.getMesGratificacion()) {
                BigDecimal ingresoConGratificacion = trabajador.getIngresoMensual().add(trabajador.getGratificaciones());
                BigDecimal remuneracionBrutaAnualConGratificacion = trabajador.getIngresoMensual().multiply(new BigDecimal(12)).add(trabajador.getGratificaciones());
                BigDecimal remuneracionNetaAnualConGratificacion = calcularRemuneracionNetaAnual(remuneracionBrutaAnualConGratificacion);
                BigDecimal impuestoConGratificacion = calcularImpuestoAnualProyectado(remuneracionNetaAnualConGratificacion);

                BigDecimal retencionAdicional = impuestoConGratificacion.subtract(impuestoAnual).divide(new BigDecimal(7), BigDecimal.ROUND_HALF_UP);

                retencionMensual = retencionMensualBase.add(retencionAdicional);
            }

            Retencion retencion = new Retencion();
            retencion.setTrabajadorId(trabajadorGuardado.getId());
            retencion.setRetencionMensual(retencionMensual);
            retencion.setMes(mes);
            retenciones.add(retencion);
        }

        return retencionRepository.saveAll(retenciones);
    }


    public BigDecimal calcularRetencionAdicional(Trabajador trabajador, BigDecimal montoAdicional) {
        BigDecimal remuneracionBrutaAnualConAdicional = calcularRemuneracionBrutaAnual(trabajador).add(montoAdicional);
        BigDecimal remuneracionNetaAnualConAdicional = calcularRemuneracionNetaAnual(remuneracionBrutaAnualConAdicional);
        BigDecimal impuestoAnualConAdicional = calcularImpuestoAnualProyectado(remuneracionNetaAnualConAdicional);

        BigDecimal impuestoAdicional = impuestoAnualConAdicional.subtract(calcularImpuestoAnualProyectado(calcularRemuneracionNetaAnual(calcularRemuneracionBrutaAnual(trabajador))));

        return impuestoAdicional.divide(new BigDecimal(12 - trabajador.getMesInicio() + 1), BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calcularRetencionesAcumuladas(Long trabajadorId, int mesHasta) {
        List<Retencion> retenciones = retencionRepository.findByTrabajadorId(trabajadorId);
        return retenciones.stream()
                .filter(retencion -> retencion.getMes() <= mesHasta)
                .map(Retencion::getRetencionMensual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Retencion> obtenerRetencionesPorTrabajador(Long trabajadorId) {
        return retencionRepository.findByTrabajadorId(trabajadorId);
    }
}
