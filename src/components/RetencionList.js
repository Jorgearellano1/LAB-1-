import React, { useState, useEffect } from 'react';

function RetencionList({ trabajadorId }) {
    const [retenciones, setRetenciones] = useState([]);

    useEffect(() => {
        if (trabajadorId) {
            fetchRetenciones(trabajadorId);
        }
    }, [trabajadorId]);

    const fetchRetenciones = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/retenciones/${id}`); // Asegúrate de que esta URL coincide con tu backend
            if (response.ok) {
                const data = await response.json();
                setRetenciones(data);
            } else {
                console.error('Error fetching retenciones:', response.statusText);
            }
        } catch (error) {
            console.error('Error fetching retenciones:', error);
        }
    };

    return (
        <div>
            <h2>Retenciones</h2>
            <table>
                <thead>
                <tr>
                    <th>Mes</th>
                    <th>Retención Mensual</th>
                </tr>
                </thead>
                <tbody>
                {retenciones.map((retencion) => (
                    <tr key={retencion.id}>
                        <td>{retencion.mes}</td>
                        <td>{retencion.retencionMensual}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default RetencionList;
