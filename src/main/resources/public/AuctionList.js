import React from 'react';

function AuctionList({ auctions }) {
  return (
    <div>
      <h2>Subastas Activas</h2>
      <ul>
        {auctions.map((auction) => (
          <li key={auction.id}>
            {auction.name} - Precio actual: {auction.currentPrice}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AuctionList;
