import React, { useState } from 'react';

function RetencionForm({ onRetencionCalculada }) {
    const [nombre, setNombre] = useState('');
    const [ingresoMensual, setIngresoMensual] = useState('');
    const [gratificaciones, setGratificaciones] = useState('');
    const [bonificaciones, setBonificaciones] = useState('');
    const [mesInicio, setMesInicio] = useState('');
    const [mesGratificacion, setMesGratificacion] = useState(''); // Nuevo estado para el mes de gratificación

    const handleSubmit = async (event) => {
        event.preventDefault();

        const trabajador = {
            nombre: nombre,
            ingresoMensual: parseFloat(ingresoMensual) || 0,
            gratificaciones: parseFloat(gratificaciones) || 0,
            bonificaciones: parseFloat(bonificaciones) || 0,
            mesInicio: parseInt(mesInicio) || 1,
            mesGratificacion: parseInt(mesGratificacion) || 6 // Asumimos junio si no se selecciona nada
        };

        try {
            const response = await fetch('http://localhost:8080/api/retenciones/calcular', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(trabajador),
            });

            if (response.ok) {
                const retenciones = await response.json();
                onRetencionCalculada(retenciones);
            } else {
                console.error('Error calculando retenciones');
            }
        } catch (error) {
            console.error('Error en la solicitud:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Nombre del Trabajador:</label>
                <input
                    type="text"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Ingreso Mensual:</label>
                <input
                    type="number"
                    value={ingresoMensual}
                    onChange={(e) => setIngresoMensual(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Gratificaciones:</label>
                <input
                    type="number"
                    value={gratificaciones}
                    onChange={(e) => setGratificaciones(e.target.value)}
                />
            </div>
            <div>
                <label>Mes de Gratificación:</label>
                <select
                    value={mesGratificacion}
                    onChange={(e) => setMesGratificacion(e.target.value)}
                >
                    <option value="6">Junio</option>
                    <option value="11">Noviembre</option>
                    <option value="0">No Aplicar</option>
                </select>
            </div>
            <div>
                <label>Bonificaciones:</label>
                <input
                    type="number"
                    value={bonificaciones}
                    onChange={(e) => setBonificaciones(e.target.value)}
                />
            </div>
            <div>
                <label>Mes de Inicio:</label>
                <input
                    type="number"
                    value={mesInicio}
                    onChange={(e) => setMesInicio(e.target.value)}
                    required
                />
            </div>
            <button type="submit">Calcular Retenciones</button>
        </form>
    );
}

export default RetencionForm;
