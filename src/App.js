import React, { useState } from 'react';
import RetencionForm from './components/RetencionForm';
import RetencionList from './components/RetencionList';
import './App.css';

function App() {
  const [retenciones, setRetenciones] = useState([]);
  const [trabajadorId, setTrabajadorId] = useState(null);

  const handleRetencionCalculada = (nuevasRetenciones) => {
    setRetenciones(nuevasRetenciones);
    if (nuevasRetenciones.length > 0) {
      setTrabajadorId(nuevasRetenciones[0].trabajadorId);
    }
  };

  return (
      <div className="App">
        <h1>Calculadora de Retenciones</h1>
        <RetencionForm onRetencionCalculada={handleRetencionCalculada} />
        {trabajadorId && <RetencionList trabajadorId={trabajadorId} />}
      </div>
  );
}

export default App;
