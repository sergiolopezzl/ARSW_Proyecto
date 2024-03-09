// server.js
const express = require('express');
const http = require('http');
const socketIo = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);

const PORT = process.env.PORT || 5000;

// Lista de subastas activas
let auctions = [
  { id: 1, name: 'Subasta 1', currentPrice: 100 },
  { id: 2, name: 'Subasta 2', currentPrice: 200 }
];

// Función para enviar la lista de subastas a todos los clientes conectados
function sendAuctions() {
  io.sockets.emit('auctions', auctions);
}

// Evento de conexión de Socket.IO
io.on('connection', (socket) => {
  console.log('Nuevo cliente conectado');

  // Enviar la lista de subastas al cliente recién conectado
  sendAuctions();

  // Manejar las ofertas de los clientes
  socket.on('bid', (data) => {
    const { auctionId, bidAmount } = data;
    // Aquí deberías implementar la lógica para gestionar la oferta en la subasta
    // Actualizar el precio de la subasta, etc.
    console.log(`Oferta recibida para la subasta ${auctionId}: ${bidAmount}`);
    // Por ahora, solo actualizamos el precio de la subasta directamente en la lista
    const auction = auctions.find((auction) => auction.id === auctionId);
    if (auction) {
      auction.currentPrice = bidAmount;
      sendAuctions(); // Enviar la lista actualizada a todos los clientes
    }
  });

  // Manejar la desconexión del cliente
  socket.on('disconnect', () => {
    console.log('Cliente desconectado');
  });
});

// Iniciar el servidor y escuchar en el puerto especificado
server.listen(PORT, () => {
  console.log(`Servidor Socket.IO escuchando en el puerto ${PORT}`);
});
