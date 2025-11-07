const WS_URL = 'ws://localhost:4567/ws/auction';
let socket;

// ================== WebSocket Setup ==================
function initWebSocket() {
    socket = new WebSocket(WS_URL);

    socket.onopen = () => {
        console.log("✅ WebSocket connected to auction feed (INDEX)");
    };

    socket.onmessage = (event) => {
        const msg = JSON.parse(event.data);
        handleWebSocketMessage(msg);
    };

    socket.onclose = () => {
        console.warn("⚠️ WebSocket closed. Reconnecting in 3 seconds...");
        setTimeout(initWebSocket, 3000);
    };

    socket.onerror = (error) => {
        console.error("❌ WebSocket error:", error);
    };
}

// ================== Handle WebSocket Messages ==================
function handleWebSocketMessage(msg) {
    const { type, data } = msg;

    switch (type) {
        case "CONNECTED":
            console.log("🔌 Connected to auction feed");
            break;

        case "NEW_ITEMADDED":
            console.log("🆕 New item added:", data);
            addNewItemToGrid(data.item);
            break;

        case "ITEM_UPDATED":
            console.log("🔄 Item updated:", data);
            updateExistingItem(data.itemId, data.item);
            break;
        case "NEW_OFFER":
            updateItemByOffer(data.itemId, data.offer)
            break;

        default:
            break;
    }
}

// ================== Add New Item to Grid ==================
function addNewItemToGrid(item) {
    const grid = document.querySelector('.items-grid');
    const emptyState = document.querySelector('.empty-state');
    if (emptyState) {
        emptyState.remove();
        const main = document.querySelector('main');
        const newGrid = document.createElement('div');
        newGrid.className = 'items-grid';
        main.appendChild(newGrid);
        return addNewItemToGrid(item); // Llamar recursivamente
    }

    if (!grid) return;
    const itemCard = createItemCard(item);
    grid.insertAdjacentHTML('afterbegin', itemCard);
    const firstCard = grid.firstElementChild;
    firstCard.style.animation = 'slideIn 0.5s ease-out';
    showNotification(`🆕 New item available: ${item.name}`);
}

// ==== UPDATE ITEM BY OFFER
function updateItemByOffer(itemId, offerData){
    const itemCard = document.querySelector(`.item-card[data-item-id="${itemId}"]`);
    if (!itemCard) {
        console.log("Item not found in current view, skipping update");
        return;
    }

    const currentPriceBadge = itemCard.querySelector('.badge-current');
    if (currentPriceBadge && offerData.offerAmount !== undefined) {
        currentPriceBadge.textContent = `Current: ${offerData.offerAmount} (Updated)`;
        currentPriceBadge.style.backgroundColor = 'red'; // Cambio visible
        currentPriceBadge.style.animation = 'pulse 0.5s ease';
        setTimeout(() => {
            currentPriceBadge.style.animation = '';
            currentPriceBadge.style.backgroundColor = '';
            currentPriceBadge.textContent = `Current: ${offerData.offerAmount}`; // Restaurar texto original
        }, 1000);
    } else {
        console.warn("currentPriceBadge not found or currentPrice undefined");
    }

    // Get the totalOffers element
    const offersElement = itemCard.querySelector('.stat-value');
    if (!offersElement) {
        console.warn("Offers element not found for itemId:", data.itemId);
        return;
    }

    // Get current totalOffers, increment, and update
    let totalOffers = parseInt(offersElement.textContent) || 0; // Fallback to 0 if NaN
    totalOffers += 1;
    offersElement.textContent = totalOffers;

}


// ===== UPDATE AN EXISTING ITEM ====

function updateExistingItem(itemId, itemData) {
    const itemCard = document.querySelector(`.item-card[data-item-id="${itemId}"]`);
    if (!itemCard) {
        console.log("Item not found in current view, skipping update");
        return;
    }

    const currentPriceBadge = itemCard.querySelector('.badge-current');
    if (currentPriceBadge && itemData.currentPrice !== undefined) {
        currentPriceBadge.textContent = `Current: ${itemData.currentPrice} (Updated)`;
        currentPriceBadge.style.backgroundColor = 'red'; // Cambio visible
        currentPriceBadge.style.animation = 'pulse 0.5s ease';
        setTimeout(() => {
            currentPriceBadge.style.animation = '';
            currentPriceBadge.style.backgroundColor = '';
            currentPriceBadge.textContent = `Current: ${itemData.currentPrice}`; // Restaurar texto original
        }, 1000);
    } else {
        console.warn("currentPriceBadge not found or currentPrice undefined");
    }

    const currentName = itemCard.querySelector('.item-name');
    currentPriceBadge.style.backgroundColor = 'red';
    currentName.style.animation = 'pulse 0.5s ease'
    setTimeout(() => {
        currentName.style.animation = '';
        currentName.style.background = '';
        currentName.textContent = `${itemData.name}`;
        }, 1000);

    const statsDiv = itemCard.querySelector('.item-stats');
    if (statsDiv) {
        if (itemData.totalOffers > 0 || currentT) {
            console.log("Updating stats with offers:", itemData.totalOffers);
            statsDiv.innerHTML = `
                <div class="stat">
                    <span class="stat-label">Offers:</span>
                    <span class="stat-value">${itemData.totalOffers}</span>
                </div>
                <div class="stat">
                    <span class="stat-label">Highest:</span>
                    <span class="stat-value">${itemData.highestOffer || itemData.currentPrice}</span>
                </div>
            `;
        } else {
            console.log("Updating stats to no offers");
            statsDiv.innerHTML = `
                <div class="stat no-offers">
                    <span>No offers yet - Be the first! (Updated)</span>
                </div>
            `;
            statsDiv.style.backgroundColor = 'yellow'; // Cambio visible
            setTimeout(() => {
                statsDiv.style.backgroundColor = '';
                statsDiv.innerHTML = `
                    <div class="stat no-offers">
                        <span>No offers yet - Be the first!</span>
                    </div>
                `;
            }, 1000);
        }
    } else {
        console.warn("statsDiv not found");
    }

    // Highlight card
    console.log("Highlighting card");
    itemCard.style.transition = 'background 0.5s';
    itemCard.style.background = '#222';
    setTimeout(() => itemCard.style.background = '', 500);

    // Forzar re-renderización
    itemCard.style.display = 'none';
    itemCard.offsetHeight; // Forzar re-render
    itemCard.style.display = '';
}



// ================== Filter Items by Price ==================
function filterItemsByPrice() {
    const minPrice = parseFloat(document.getElementById('min-price').value) || 0;
    const maxPrice = parseFloat(document.getElementById('max-price').value) || Infinity;

    document.querySelectorAll('.item-card').forEach(card => {
        const currentPriceText = card.querySelector('.badge-current')?.textContent || '';
        const priceMatch = currentPriceText.match(/\d+(\.\d+)?/);
        const price = priceMatch ? parseFloat(priceMatch[0]) : 0;

        if (price >= minPrice && price <= maxPrice) {
            card.style.display = '';
        } else {
            card.style.display = 'none';
        }
    });

    console.log("it works");
}


// ================== Create Item Card HTML ==================
function createItemCard(item) {
    const hasOffers = item.totalOffers > 0;

    return `
        <div class="item-card" data-item-id="${item.id}">
            <div class="item-header">
                <h3>${item.name}</h3>
                <div class="price-badges">
                    <span class="badge badge-original">Original: ${item.originalPrice}</span>
                    <span class="badge badge-current">Current: ${item.currentPrice}</span>
                </div>
            </div>

            <p class="item-description">${item.description}</p>

            <div class="item-stats">
                ${hasOffers ? `
                    <div class="stat">
                        <span class="stat-label">Offers:</span>
                        <span class="stat-value">${item.totalOffers}</span>
                    </div>
                    <div class="stat">
                        <span class="stat-label">Highest:</span>
                        <span class="stat-value">${item.highestOffer || item.currentPrice}</span>
                    </div>
                ` : `
                    <div class="stat no-offers">
                        <span>No offers yet - Be the first!</span>
                    </div>
                `}
            </div>

            <div class="item-actions">
                <a href="/item/${item.id}" class="btn btn-primary">View Details & Bid</a>
            </div>
        </div>
    `;
}

// ================== Notification System ==================
function showNotification(message) {
    // Remover notificación anterior si existe
    const existing = document.querySelector('.toast-notification');
    if (existing) existing.remove();

    // Crear nueva notificación
    const toast = document.createElement('div');
    toast.className = 'toast-notification';
    toast.textContent = message;
    document.body.appendChild(toast);

    // Mostrar
    setTimeout(() => toast.classList.add('show'), 100);

    // Ocultar y remover
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// ================== Initialize ==================
document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname.includes('/item/')) {
        console.log("🚫 Item detail page detected, skipping index.js");
        return;
    }

    console.log("🚀 Initializing auction feed...");

    // Agregar data-item-id a las tarjetas existentes
    document.querySelectorAll('.item-card').forEach((card, index) => {
        const link = card.querySelector('a[href^="/item/"]');
        if (link) {
            const itemId = link.getAttribute('href').split('/').pop();
            card.setAttribute('data-item-id', itemId);
        }
    });

    // Conectar WebSocket
    initWebSocket();

    // ================== Price Filter Event Listeners ==================
    document.getElementById('apply-filters').addEventListener('click', filterItemsByPrice);
    document.getElementById('min-price').addEventListener('input', filterItemsByPrice);
    document.getElementById('max-price').addEventListener('input', filterItemsByPrice);
});