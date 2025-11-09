// FastFood Application JavaScript
// Application state
let cart = [];
let currentCustomization = {};
let orderCounter = 1001;
const apiUrl = 'http://localhost:8081/api/order'

// Initialize the app
document.addEventListener('DOMContentLoaded', function() {
    handleAddEventListener();
    updateCartDisplay();
});

// Render menu items
function handleAddEventListener() {
    // Navbar
    document.getElementById('navBarCheckoutBtn').addEventListener('click', toggleCart);

    // Combos Tab
    addListenerToMenuCategory('combos');

    // Food Tab
    addListenerToMenuCategory('food');

    // Drinks Tab
    addListenerToMenuCategory('drinks');

    // Cart Section
    document.getElementById('cartCheckoutBtn').addEventListener('click', proceedToCheckout);
    document.getElementById('cartBackToMenuBtn').addEventListener('click', backToMenu);

    // Confirmation Sections
    document.getElementById('submitOrderBtn').addEventListener('click', submitOrder);
    document.getElementById('backToCartBtn').addEventListener('click', backToCart);

    // Customization Modal
    addEventListenerToCustomizationModel();
}

function addEventListenerToCustomizationModel() {
    const parent = document.getElementById('customizationOptions');
    const buttons = parent.querySelectorAll('button');

    buttons.forEach(button => {
        // minus
        const minusSuffix ='-minus';
        if (button.id?.endsWith(minusSuffix)) {
            button.addEventListener("click", function() {
                const displayId = button.id.replace(minusSuffix, '');
                changeQuantityToggle(displayId, -1);
            });
        }

        // plus
        const plusSuffix = '-plus';
        if (button.id?.endsWith(plusSuffix)) {
            button.addEventListener("click", function() {
                const displayId = button.id.replace(plusSuffix, '');
                changeQuantityToggle(displayId, 1);
            });
        }
    });

    document.getElementById('customizationCancelBtn').addEventListener('click', closeCustomizationModal);
    document.getElementById('customizationSubmitBtn').addEventListener('click', addCustomizedItem);
}

function addListenerToMenuCategory(category) {
    const parent = document.getElementById(category);
    // buttons
    const buttons = parent.querySelectorAll('button');

    buttons.forEach(button => {
        // minus
        const minusSuffix ='-minus';
        if (button.id?.endsWith(minusSuffix)) {
            button.addEventListener("click", function() {
                const displayId = button.id.replace(minusSuffix, '');
                changeQuantityNoLessThanOne(displayId, -1);
            });
        }

        // plus
        const plusSuffix = '-plus';
        if (button.id?.endsWith(plusSuffix)) {
            button.addEventListener("click", function() {
                const displayId = button.id.replace(plusSuffix, '');
                changeQuantityNoLessThanOne(displayId, 1);
            });
        }

        // to-cart
        const toCartSuffix = '-to-cart';
        if (button.id?.endsWith(toCartSuffix)) {
            button.addEventListener("click", function() {
                const productId = button.id.replace(/^add-/, '').replace(new RegExp(toCartSuffix + '$'), '');
                customizeItem(productId, category);
            });
        }
    });
}

// Quantity control
function changeQuantityToggle(itemId, change) {
    changeQuantityNoLessThanZero(itemId, change)
    const hiddenInput = document.getElementById(itemId.replace('qty-', ''));
    const minusButton = document.getElementById(itemId + '-minus');
    const plusButton = document.getElementById(itemId + '-plus');
    const displayElement = document.getElementById(itemId);
    let currentQty = parseInt(displayElement.textContent);
    hiddenInput.value = displayElement.textContent
    if(currentQty == 0) {
        plusButton.disabled = false;
        minusButton.disabled = true;
    } else {
        plusButton.disabled = true;
        minusButton.disabled = false;
    }
}

// Quantity control no less than one
function changeQuantityNoLessThanOne(itemId, change) {
    const qtyElement = document.getElementById(itemId);
    let currentQty = parseInt(qtyElement.textContent);
    const newQty = Math.max(1, currentQty + change);
    qtyElement.textContent = newQty;
}

// Quantity control no less than zero
function changeQuantityNoLessThanZero(itemId, change) {
    const qtyElement = document.getElementById(itemId);
    let currentQty = parseInt(qtyElement.textContent);
    const newQty = Math.max(0, currentQty + change);
    qtyElement.textContent = newQty;
}

// Customize item
function customizeItem(itemId, category) {
    const quantity = parseInt(document.getElementById(`qty-${itemId}`).textContent);
    const customizations = document.getElementById(itemId).dataset.customizations?.split(',');

    currentCustomization = {
        itemId: itemId,
        quantity: quantity,
        customizations: {}
    };

    if (customizations && customizations.length > 0) {
        showCustomizationModal(itemId, customizations);
    } else {
        addToCart(itemId, quantity, {});
    }
}

// Show customization modal
function showCustomizationModal(itemId, customizations) {
    // clear the options
    const customizationOptions = document.getElementById('customizeModal').dataset.availableCustomizations.split(',');
    customizationOptions.forEach(option => {
        const parent = document.getElementById(option);
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

          // Reset Ingredients
          const ingredientsParent = document.getElementById('customizationOptions');
          const buttons = ingredientsParent.querySelectorAll('button');

          buttons.forEach(button => {
              // minus
              const minusSuffix ='-minus';
              if (button.id?.endsWith(minusSuffix)) {
                  const displayId = button.id.replace(minusSuffix, '');
                  changeQuantityNoLessThanOne(displayId, -1000);
                  button.disabled = false;
                  document.getElementById(displayId + '-plus').disabled = true;
                  document.getElementById(displayId.replace('qty-', '')).value = '1';
              }
          });
    });

    // display the desired options
        customizationOptions.forEach(option => {
            let currentCustomizationElement = document.getElementById(option);
            if(currentCustomizationElement && customizations.includes(option)){
                currentCustomizationElement.style.display = "block";
            }
        });

    const productName = document.getElementById(itemId).dataset.name;
    const image = document.getElementById(itemId).dataset.image;
    document.getElementById('modalProductImage').src = image;
    document.getElementById('modalProductImage').alt = productName;

    const modalTitle = document.querySelector('#customizeModal .modal-title');
    modalTitle.textContent = modalTitle.dataset.title + ' ' + productName;
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
    const ingredients = document.querySelectorAll('#customizationOptions input[type="hidden"]');
    ingredients.forEach(input => {
        if(input.value == '0') {
            if(!customizations[input.type]) customizations[input.type] = [];
            customizations[input.type].push({
                name: input.dataset.no + ' ' + input.dataset.name,
                price: parseFloat(input.dataset.price)
            });
        }
    });

    addToCart(currentCustomization.itemId, currentCustomization.quantity, customizations);

    closeCustomizationModal();

    // Reset quantity to 1
    document.getElementById(`qty-${currentCustomization.itemId}`).textContent = '1';
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
function addToCart(itemId, quantity, customizations) {
    const name = document.getElementById(itemId).dataset.name;
    const description = document.getElementById(itemId).dataset.description;
    const image = document.getElementById(itemId).dataset.image;
    const productPrice = parseFloat(document.getElementById(itemId).dataset.price);
    const price = calculateItemPrice(productPrice, customizations);

    const cartItem = {
        id: `${itemId}-${Date.now()}`,
        itemId: itemId,
        name: name,
        description: description,
        image: image,
        quantity: quantity,
        customizations: customizations,
        price: price
    };

    cart.push(cartItem);
    updateCartDisplay();
    showToast(`${name} added to cart!`);
}

// Calculate item price with customizations
function calculateItemPrice(productPrice, customizations) {
    Object.values(customizations).forEach(custArray => {
        custArray.forEach(cust => {
            productPrice += cust.price;
        });
    });
    return productPrice;
}

// Update cart display
function updateCartDisplay() {
    const cartCount = document.getElementById('cartCount');
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = totalItems;

    const checkoutBtn = document.getElementById('cartCheckoutBtn');
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
                        <img src="${cartItem.image}" class="img-fluid rounded" alt="${cartItem.name}">
                    </div>
                    <div class="col-md-4">
                        <h6 class="mb-1">${cartItem.name}</h6>
                        <small class="text-muted">${cartItem.description}</small>
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
    const tax = subtotal * 0.10;
    const total = subtotal;

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
    const tax = subtotal * 0.10;
    const total = subtotal;

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
                    <strong>${cartItem.name}</strong> x ${cartItem.quantity}
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
                        <span>Tax (10%):</span>
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
    // First, save cart to session via API
        fetch('/api/save-cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
            },
            body: JSON.stringify(cart)
        })
        .then(response => response.json())
        .then(data => {
            // Then redirect using simple form or window.location
            window.location.assign('/select-payment');
        })
        .catch(error => console.error('Error:', error));
}

// Start new order
function startNewOrder() {
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