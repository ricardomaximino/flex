// FastFood Application JavaScript
// Application state
let cart = [];
let currentCustomization = {};
let orderCounter = 1001;

// Initialize the app
document.addEventListener('DOMContentLoaded', function() {
    handleMenuEvents();
    updateCartDisplay();
});

// Render menu items
function handleMenuEvents() {
    addListenerToMenuCategory('combos');
    addListenerToMenuCategory('food');
    addListenerToMenuCategory('drinks');
    document.getElementById('customizationCancelBtn').addEventListener('click', closeCustomizationModal);
    document.getElementById('customizationSubmitBtn').addEventListener('click', addCustomizedItem);
}

function addListenerToMenuCategory(category) {
    const items = menuData[category];
    items.forEach(function(product) {
        let displayId = 'qty-' + product.id;

        // add event listener to minus button
        let minusButtonId = 'qty-' + product.id + '-minus';
        let minusButton = document.getElementById(minusButtonId)
        if(minusButton){
            minusButton.addEventListener("click", function() {
                changeQuantity(displayId, -1);
            });
        }

        // add event listener to plus button
        let qtyElement = document.getElementById(displayId);
        let plusButtonId = 'qty-' + product.id + '-plus';
        let plusButton = document.getElementById(plusButtonId)
        if(plusButton){
            plusButton.addEventListener("click", function() {
                changeQuantity(displayId, 1);
            });
        }

        // add event listener to add to cart button
        let addToCartButtonId = 'add-' + product.id + '-to-cart';
        let addToCartButton = document.getElementById(addToCartButtonId)
        if(addToCartButton){
            addToCartButton.addEventListener("click", function() {
                customizeItem(product.id, category);
            });
        }

    });

}

// Quantity control
function changeQuantity(itemId, change) {
    const qtyElement = document.getElementById(itemId);
    let currentQty = parseInt(qtyElement.textContent);
    const newQty = Math.max(1, currentQty + change);
    qtyElement.textContent = newQty;
}

// Customize item
function customizeItem(itemId, category) {
    const item = menuData[category].find(i => i.id === itemId);
    const quantity = parseInt(document.getElementById(`qty-${itemId}`).textContent);

    currentCustomization = {
        item: item,
        quantity: quantity,
        customizations: {}
    };

    if (item.customizations && item.customizations.length > 0) {
        showCustomizationModal(item);
    } else {
        addToCart(item, quantity, {});
    }
}

// Show customization modal
function showCustomizationModal(item) {
    // clear the options
    customizationOptions.forEach(option => {
        const parent = document.getElementById(option.id);
        parent.style.display = "none";
        // Reset radio buttons
          const radios = parent.querySelectorAll('input[type="radio"]');
          radios.forEach(radio => {
            radio.checked = radio.defaultChecked;
          });

          // Reset checkboxes
          const checkboxes = parent.querySelectorAll('input[type="checkbox"]');
          checkboxes.forEach(checkbox => {
            checkbox.checked = checkbox.defaultChecked;
          });
    });

    // display the desired options
        customizationOptions.forEach(option => {
            let currentCustomizationElement = document.getElementById(option.id);
            if(currentCustomizationElement && item.customizations.includes(option.id)){
                currentCustomizationElement.style.display = "block";
            }
        });

    const modalTitle = document.querySelector('#customizeModal .modal-title');
    modalTitle.textContent = `Customize ${item.name}`;
    const modal = new bootstrap.Modal(document.getElementById('customizeModal'));
    modal.show();
}

// Add customized item to cart
function addCustomizedItem() {
    const customizations = {};
    const checkboxes = document.querySelectorAll('#customizationOptions input[type="checkbox"]:checked');
    const radios = document.querySelectorAll('#customizationOptions input[type="radio"]:checked');

    [...checkboxes, ...radios].forEach(input => {
        const type = input.name;
        if (!customizations[type]) customizations[type] = [];
        customizations[type].push({
            name: input.value,
            price: parseFloat(input.dataset.price)
        });
    });

    addToCart(currentCustomization.item, currentCustomization.quantity, customizations);

    closeCustomizationModal();

    // Reset quantity to 1
    document.getElementById(`qty-${currentCustomization.item.id}`).textContent = '1';
}

// Close the modal in accessibility-compliant way
function closeCustomizationModal() {
    const modalElement = document.getElementById('customizeModal');
    const focusedElement = modalElement.querySelector(':focus');
    if (focusedElement) {
      focusedElement.blur();
    }
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
}

// Add item to cart
function addToCart(item, quantity, customizations) {
    const cartItem = {
        id: `${item.id}-${Date.now()}`,
        item: item,
        quantity: quantity,
        customizations: customizations,
        price: calculateItemPrice(item, customizations)
    };

    cart.push(cartItem);
    updateCartDisplay();
    showToast(`${item.name} added to cart!`);
}

// Calculate item price with customizations
function calculateItemPrice(item, customizations) {
    let price = item.price;
    Object.values(customizations).forEach(custArray => {
        custArray.forEach(cust => {
            price += cust.price;
        });
    });
    return price;
}

// Update cart display
function updateCartDisplay() {
    const cartCount = document.getElementById('cartCount');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = totalItems;

    const checkoutBtn = document.getElementById('checkoutBtn');
    checkoutBtn.disabled = cart.length === 0;

    updateCartItems();
    updateOrderSummary();
}

// Update cart items
function updateCartItems() {
    const cartItemsContainer = document.getElementById('cartItems');

    if (cart.length === 0) {
        cartItemsContainer.innerHTML = `
            <div class="text-center text-muted py-5">
                <i class="fas fa-shopping-cart fa-3x mb-3"></i>
                <p>Your cart is empty</p>
            </div>
        `;
        return;
    }

    cartItemsContainer.innerHTML = cart.map(cartItem => {
        const customizationsHtml = Object.entries(cartItem.customizations).map(([type, items]) => {
            return items.map(item => `<span class="badge bg-secondary me-1">${item.name}</span>`).join('');
        }).join('');

        return `
            <div class="cart-item">
                <div class="row align-items-center">
                    <div class="col-md-2">
                        <img src="${cartItem.item.image}" class="img-fluid rounded" alt="${cartItem.item.name}">
                    </div>
                    <div class="col-md-4">
                        <h6 class="mb-1">${cartItem.item.name}</h6>
                        <small class="text-muted">${cartItem.item.description}</small>
                        <div class="mt-2">${customizationsHtml}</div>
                    </div>
                    <div class="col-md-2">
                        <div class="quantity-control">
                            <button class="quantity-btn btn-sm" onclick="updateCartItemQuantity('${cartItem.id}', -1)">-</button>
                            <span class="mx-2">${cartItem.quantity}</span>
                            <button class="quantity-btn btn-sm" onclick="updateCartItemQuantity('${cartItem.id}', 1)">+</button>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <strong>$${(cartItem.price * cartItem.quantity).toFixed(2)}</strong>
                    </div>
                    <div class="col-md-2 text-end">
                        <button class="btn btn-outline-danger btn-sm" onclick="removeFromCart('${cartItem.id}')">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

// Update cart item quantity
function updateCartItemQuantity(cartItemId, change) {
    const cartItem = cart.find(item => item.id === cartItemId);
    if (cartItem) {
        cartItem.quantity = Math.max(1, cartItem.quantity + change);
        updateCartDisplay();
    }
}

// Remove item from cart
function removeFromCart(cartItemId) {
    cart = cart.filter(item => item.id !== cartItemId);
    updateCartDisplay();
    showToast('Item removed from cart');
}

// Update order summary
function updateOrderSummary() {
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const tax = subtotal * 0.08;
    const total = subtotal + tax;

    document.getElementById('subtotal').textContent = `$${subtotal.toFixed(2)}`;
    document.getElementById('tax').textContent = `$${tax.toFixed(2)}`;
    document.getElementById('total').textContent = `$${total.toFixed(2)}`;
}

// Toggle cart view
function toggleCart() {
    const menuSection = document.getElementById('menuSection');
    const cartSection = document.getElementById('cartSection');

    if (cartSection.style.display === 'none') {
        menuSection.style.display = 'none';
        cartSection.style.display = 'block';
        updateStep(2);
    } else {
        cartSection.style.display = 'none';
        menuSection.style.display = 'block';
        updateStep(1);
    }
}

// Back to menu
function backToMenu() {
    document.getElementById('cartSection').style.display = 'none';
    document.getElementById('menuSection').style.display = 'block';
    updateStep(1);
}

// Proceed to checkout
function proceedToCheckout() {
    document.getElementById('cartSection').style.display = 'none';
    document.getElementById('confirmationSection').style.display = 'block';
    updateStep(3);
    populateConfirmationDetails();
}

// Back to cart
function backToCart() {
    document.getElementById('confirmationSection').style.display = 'none';
    document.getElementById('cartSection').style.display = 'block';
    updateStep(2);
}

// Populate confirmation details
function populateConfirmationDetails() {
    const confirmationDetails = document.getElementById('confirmationDetails');
    const subtotal = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const tax = subtotal * 0.08;
    const total = subtotal + tax;

    let html = `
        <div class="row">
            <div class="col-lg-8">
                <h5 class="mb-3">Order Items</h5>
    `;

    cart.forEach(cartItem => {
        const customizationsHtml = Object.entries(cartItem.customizations).map(([type, items]) => {
            return items.map(item => `<span class="badge bg-light text-dark me-1">${item.name}</span>`).join('');
        }).join('');

        html += `
            <div class="d-flex justify-content-between align-items-center border-bottom py-2">
                <div>
                    <strong>${cartItem.item.name}</strong> x ${cartItem.quantity}
                    <div class="mt-1">${customizationsHtml}</div>
                </div>
                <div class="fw-bold">$${(cartItem.price * cartItem.quantity).toFixed(2)}</div>
            </div>
        `;
    });

    html += `
            </div>
            <div class="col-lg-4">
                <div class="bg-light p-3 rounded">
                    <h5 class="mb-3">Order Total</h5>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Subtotal:</span>
                        <span>$${subtotal.toFixed(2)}</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Tax (8%):</span>
                        <span>$${tax.toFixed(2)}</span>
                    </div>
                    <hr>
                    <div class="d-flex justify-content-between fw-bold">
                        <span>Total:</span>
                        <span>$${total.toFixed(2)}</span>
                    </div>
                </div>
            </div>
        </div>
    `;

    confirmationDetails.innerHTML = html;
}

// Submit order
function submitOrder() {
    const orderNumber = `FB${orderCounter++}`;
    document.getElementById('orderNumber').textContent = orderNumber;

    document.getElementById('confirmationSection').style.display = 'none';
    document.getElementById('successSection').style.display = 'block';

    // Clear cart
    cart = [];
    updateCartDisplay();
    updateStep(4);
}

// Start new order
function startNewOrder() {
    document.getElementById('successSection').style.display = 'none';
    document.getElementById('menuSection').style.display = 'block';
    updateStep(1);
}

// Update step indicator
function updateStep(step) {
    // Reset all steps
    for (let i = 1; i <= 3; i++) {
        const stepElement = document.getElementById(`step${i}`);
        stepElement.classList.remove('active', 'completed');
        if (i < step) {
            stepElement.classList.add('completed');
        } else if (i === step) {
            stepElement.classList.add('active');
        }
    }
}

// Show toast notification
function showToast(message) {
    // Create toast element
    const toastContainer = document.getElementById('toastContainer') || createToastContainer();

    const toast = document.createElement('div');
    toast.className = 'toast show';
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="toast-header">
            <i class="fas fa-check-circle text-success me-2"></i>
            <strong class="me-auto">FastBite</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
        </div>
        <div class="toast-body">
            ${message}
        </div>
    `;

    toastContainer.appendChild(toast);

    // Auto remove after 3 seconds
    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 3000);
}

// Create toast container if it doesn't exist
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    document.body.appendChild(container);
    return container;
}