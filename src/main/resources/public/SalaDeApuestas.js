import React, { useState } from 'react';

function SalaDeApuestas() {
    const [nombre, setNombre] = useState('');
    const [cantidadApostada, setCantidadApostada] = useState(0);

    const handleSubmit = (event) => {
        event.preventDefault();
        // Aquí puedes manejar la lógica de la apuesta
        console.log(`El usuario ${nombre} apostó ${cantidadApostada} unidades.`);
    };

    return (
        <div>
            <h1>Sala de Apuestas</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Nombre:
                    <input
                        type="text"
                        value={nombre}
                        onChange={(e) => setNombre(e.target.value)}
                    />
                </label>
                <br />
                <label>
                    Cantidad a Apostar:
                    <input
                        type="number"
                        value={cantidadApostada}
                        onChange={(e) => setCantidadApostada(e.target.value)}
                    />
                </label>
                <br />
                <button type="submit">Apostar</button>
            </form>
        </div>
    );
}

export default SalaDeApuestas;


