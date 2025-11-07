const API_BASE = 'http://localhost:4567/api/v1';
const WS_URL = 'ws://localhost:4567/ws/auction';
let socket;
let resultDiv; // Global reference

// ================== WebSocket Setup ==================
function initWebSocket() {
    socket = new WebSocket(WS_URL);

    socket.onopen = () => {
        console.log("WebSocket connected.");
        const itemId = window.location.pathname.split("/").pop();
        socket.send(JSON.stringify({
            action: "WATCH_ITEM",
            itemId: parseInt(itemId, 10)
        }));
    };

    socket.onmessage = (event) => {
        handleWebSocketMessage(JSON.parse(event.data));
    };

    socket.onclose = () => {
        console.warn("WebSocket closed. Reconnecting in 3 seconds...");
        setTimeout(initWebSocket, 3000);
    };

    socket.onerror = (error) => {
        console.error("WebSocket error:", error);
    };
}

// ================== Handle Incoming WebSocket Messages ==================
function handleWebSocketMessage(msg) {
    const { type, message, data } = msg;
    console.log("WebSocket event:", type, message, data);

    switch (type) {
        case "CONNECTED":
        case "WATCHING":
            console.log("WS:", message);
            break;

        case "PRICE_UPDATE":
            updatePrice(data.newPrice);
            showResult(`Price updated: ${data.newPrice.toFixed(2)}`, 'info');
            break;

        case "NEW_OFFER":
            showResult("A new offer has been placed for this item.", 'info');
            if (data && data.offer) {
                addOfferToList(data.offer);
                incrementTotalOffers();
            }
            break;

        case "OFFER_STATUS_CHANGE":
            const statusText = data.status.toUpperCase();
            showResult(`Offer status updated: ${statusText}`, 'info');
            updateOfferStatus(data.offerId, data.status);
            break;

        case "ITEM_SOLD":
            showResult("This item has been sold.", 'success');
            disableOfferForm();
            break;

        case "OUTBID":
            showResult("Your offer has been outbid.", 'warning');
            break;

        default:
            console.log("Unrecognized WebSocket event:", msg);
            break;
    }
}

// ================== UI Update Helpers ==================
function showResult(message, type) {
    if (!resultDiv) {
        resultDiv = document.getElementById('offer-result');
    }
    if (!resultDiv) return;

    resultDiv.textContent = message;
    resultDiv.className = 'offer-result ' + type;
    resultDiv.style.display = 'block';

    if (type === 'success' || type === 'error') {
        setTimeout(() => hideResult(), 5000);
    }
}

function hideResult() {
    if (!resultDiv) return;
    resultDiv.style.display = 'none';
    resultDiv.textContent = '';
    resultDiv.className = 'offer-result';
}

function updatePrice(newPrice) {
    const priceElement = document.querySelector('.price-box.highlight .price');
    if (priceElement) {
        priceElement.textContent = `$${newPrice.toFixed(2)}`;
    }

    const offerInput = document.getElementById('offerAmount');
    if (offerInput) {
        offerInput.min = newPrice;
        offerInput.placeholder = `Must be higher than $${newPrice.toFixed(2)}`;
    }
}

function disableOfferForm() {
    const formSection = document.querySelector('.offer-form-section');
    if (formSection) {
        formSection.innerHTML = `
            <h3>This item has been sold</h3>
            <p>No more offers can be placed.</p>
        `;
    }
    const availabilityEl = document.querySelector('.availability');
    if (availabilityEl) {
        availabilityEl.classList.remove('availability', 'available');
        availabilityEl.classList.add('availability');
        availabilityEl.classList.add('sold');
        availabilityEl.innerHTML = `<span>✗ SOLD</span>`;
    }
}

// Add a new offer to the top of the offers list dynamically
function addOfferToList(offer) {
    console.log("Adding offer to list:", offer);

    const offersList = document.querySelector('.offers-list');

    if (!offersList) {
        const offersSection = document.querySelector('.offers-section');
        if (!offersSection) return;
        const emptyState = offersSection.querySelector('.empty-state');
        if (emptyState) {
            emptyState.remove();
        }
        const newList = document.createElement('div');
        newList.className = 'offers-list';
        offersSection.appendChild(newList);
        return addOfferToList(offer);
    }

    const normalizedStatus = (offer.status || 'PENDING').toUpperCase();

    const offerCard = document.createElement('div');
    offerCard.className = `offer-card status-${normalizedStatus.toLowerCase()}`;
    offerCard.setAttribute('data-offer-id', offer.id || offer.offerId || '');

    const dateStr = offer.createdAt || new Date().toLocaleString();

    offerCard.innerHTML = `
        <div class="offer-header">
            <span class="offer-user">${offer.username || 'Unknown user'}</span>
            <span class="offer-amount">${parseFloat(offer.offerAmount).toFixed(2)}</span>
        </div>
        <div class="offer-body">
            ${offer.message ? `<p class="offer-message">💬 ${offer.message}</p>` : ''}
            <small class="offer-date">${dateStr}</small>
        </div>
        <div class="offer-footer">
            <span class="offer-status status-badge-${normalizedStatus.toLowerCase()}">
                ${formatStatusBadge(normalizedStatus)}
            </span>
        </div>
    `;

    offersList.prepend(offerCard);
}

function incrementTotalOffers() {
    const totalEl = document.querySelector('.offers-section h3');
    if (totalEl) {
        const match = totalEl.textContent.match(/\((\d+)/);
        if (match) {
            const current = parseInt(match[1], 10);
            totalEl.textContent = totalEl.textContent.replace(/\((\d+)/, `(${current + 1}`);
        } else {
            totalEl.textContent = totalEl.textContent.replace('Offers History', 'Offers History (1 total)');
        }
    }

    const priceBoxes = document.querySelectorAll('.price-box');
    priceBoxes.forEach(box => {
        const label = box.querySelector('label');
        if (label && label.textContent.trim() === 'Total Offers:') {
            const priceSpan = box.querySelector('.price');
            if (priceSpan) {
                const currentTotal = parseInt(priceSpan.textContent) || 0;
                priceSpan.textContent = currentTotal + 1;
            }
        }
    });
}

// Update status badge dynamically
function updateOfferStatus(offerId, status) {
    console.log("Updating offer status:", offerId, status);

    const normalizedStatus = status.toUpperCase();

    const target = document.querySelector(`.offer-card[data-offer-id="${offerId}"]`);
    if (target) {
        target.className = `offer-card status-${normalizedStatus.toLowerCase()}`;

        const statusBadge = target.querySelector('.offer-status');
        if (statusBadge) {
            statusBadge.textContent = formatStatusBadge(normalizedStatus);
            statusBadge.className = `offer-status status-badge-${normalizedStatus.toLowerCase()}`;
        }
    }
}

// Helper to format status badge with emojis
function formatStatusBadge(status) {
    switch (status.toUpperCase()) {
        case 'PENDING': return '🟡 PENDING';
        case 'ACCEPTED': return '✅ ACCEPTED';
        case 'REJECTED': return '❌ REJECTED';
        case 'OUTBID': return '⬆️ OUTBID';
        default: return status;
    }
}

function formatStatus(status) {
    return status.toUpperCase();
}

// ================== Form Logic ==================
document.addEventListener('DOMContentLoaded', () => {
    const offerForm = document.getElementById('offer-form');
    resultDiv = document.getElementById('offer-result');

    if (offerForm) {
        offerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const userId = document.getElementById('userId').value;
            const offerAmount = document.getElementById('offerAmount').value;
            const message = document.getElementById('message').value;
            const itemId = window.location.pathname.split('/').pop();

            if (!userId) {
                showResult('Please select a user profile.', 'error');
                return;
            }

            if (!offerAmount || parseFloat(offerAmount) <= 0) {
                showResult('Please enter a valid offer amount.', 'error');
                return;
            }

            showResult('Submitting your offer...', 'loading');

            try {
                const response = await fetch(`${API_BASE}/offers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        itemId: parseInt(itemId, 10),
                        userId: parseInt(userId, 10),
                        offerAmount: parseFloat(offerAmount),
                        message: message.trim() || null
                    })
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    showResult('Offer submitted successfully.', 'success');
                    document.getElementById('offerAmount').value = '';
                    document.getElementById('message').value = '';
                } else {
                    showResult('Error: ' + (data.message || 'Failed to submit offer.'), 'error');
                }
            } catch (error) {
                console.error('Error submitting offer:', error);
                showResult('Network error. Please try again.', 'error');
            }
        });
    }

    const offerAmountInput = document.getElementById('offerAmount');
    if (offerAmountInput) {
        offerAmountInput.addEventListener('input', (e) => {
            const currentPrice = parseFloat(offerAmountInput.min);
            const offerValue = parseFloat(e.target.value);
            if (!isNaN(currentPrice) && offerValue < currentPrice) {
                e.target.style.borderColor = '#e74c3c';
                showResult(`Offer must be at least $${currentPrice.toFixed(2)}.`, 'error');
            } else {
                e.target.style.borderColor = '#27ae60';
                hideResult();
            }
        });
    }

    // Start WebSocket connection when page loads
    initWebSocket();

    // Fix existing offer cards to have lowercase status classes
    document.querySelectorAll('.offer-card').forEach(card => {
        const classes = card.className.split(' ');
        const statusClass = classes.find(c => c.startsWith('status-'));

        if (statusClass) {
            const status = statusClass.replace('status-', '').toLowerCase();
            card.className = `offer-card status-${status}`;

            const badge = card.querySelector('.offer-status');
            if (badge) {
                badge.className = `offer-status status-badge-${status}`;
            }
        }
    });
});