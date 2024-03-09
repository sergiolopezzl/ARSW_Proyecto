import React, { useState } from 'react';

function AuctionRoom() {
    const [currentBid, setCurrentBid] = useState(100);
    const [bidder, setBidder] = useState('');
    const [newBid, setNewBid] = useState('');

    const handleBidChange = (event) => {
        setNewBid(event.target.value);
    };

    const placeBid = () => {
        if (parseInt(newBid) > currentBid) {
            setCurrentBid(parseInt(newBid));
            setBidder('New Bidder');
            setNewBid('');
        } else {
            alert('Your bid must be higher than the current bid.');
        }
    };

    return (
        <div>
            <h1>Auction Room</h1>
            <p>Current Bid: ${currentBid}</p>
            <p>Current Bidder: {bidder || 'None'}</p>
            <input
                type="number"
                placeholder="Place your bid"
                value={newBid}
                onChange={handleBidChange}
            />
            <button onClick={placeBid}>Place Bid</button>
        </div>
    );
}

export default AuctionRoom;
