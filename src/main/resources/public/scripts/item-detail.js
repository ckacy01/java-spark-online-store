const API_BASE = 'http://localhost:4567/api/v1';

document.addEventListener('DOMContentLoaded', () => {
    const offerForm = document.getElementById('offer-form');
    const resultDiv = document.getElementById('offer-result');

    // Functions available to all handlers
    function showResult(message, type) {
        if (!resultDiv) return;
        resultDiv.textContent = message;
        resultDiv.className = 'offer-result ' + type;
        resultDiv.style.display = 'block';

        // Auto-hide success or error after 5 seconds
        if (type === 'success' || type === 'error') {
            setTimeout(() => {
                hideResult();
            }, 5000);
        }
    }

    function hideResult() {
        if (!resultDiv) return;
        resultDiv.style.display = 'none';
        // Optionally clear text and classes
        resultDiv.textContent = '';
        resultDiv.className = 'offer-result';
    }

    if (offerForm) {
        offerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            // Get form data
            const userId = document.getElementById('userId').value;
            const offerAmount = document.getElementById('offerAmount').value;
            const message = document.getElementById('message').value;
            const itemId = window.location.pathname.split('/').pop();

            // Validate
            if (!userId) {
                showResult('Please select a user profile', 'error');
                return;
            }

            if (!offerAmount || parseFloat(offerAmount) <= 0) {
                showResult('Please enter a valid offer amount', 'error');
                return;
            }

            // Show loading
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
                console.log(data);

                if (response.ok && data.success) {
                    showResult('✅ Offer submitted successfully! The page will reload...', 'success');

                    // Clear form
                    document.getElementById('offerAmount').value = '';
                    document.getElementById('message').value = '';

                    // Reload page after 2 seconds to show new offer
                    setTimeout(() => {
                        window.location.reload();
                    }, 2000);
                } else {
                    showResult('❌ ' + (data.message || 'Failed to submit offer'), 'error');
                }
            } catch (error) {
                console.error('Error submitting offer:', error);
                showResult('❌ Network error. Please try again.', 'error');
            }
        });
    }

    // Update minimum offer amount when current price changes
    const offerAmountInput = document.getElementById('offerAmount');
    if (offerAmountInput) {
        offerAmountInput.addEventListener('input', (e) => {
            const currentPrice = parseFloat(offerAmountInput.min);
            const offerValue = parseFloat(e.target.value);
            if (!isNaN(currentPrice) && offerValue < currentPrice) {
                e.target.style.borderColor = '#e74c3c';
                showResult(`⚠️ Offer must be at least $${currentPrice.tFixed(2)}`, 'error');
            } else {
                e.target.style.borderColor = '#27ae60';
                hideResult();
            }
        });
    }
});
