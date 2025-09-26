// FastFood Application JavaScript
// Application state
let cart = [];
let currentCustomization = {};
let orderCounter = 1001;

// Menu data
const menuData = {
    combos: [
        {
            id: 'combo1',
            name: 'Big Bite Combo',
            price: 12.99,
            description: 'Double cheeseburger, large fries, and medium drink',
            image: 'https://images.unsplash.com/photo-1553979459-d2229ba7433a?w=400&h=300&fit=crop',
            customizations: ['size', 'extras', 'removals']
        },
        {
            id: 'combo2',
            name: 'Chicken Deluxe Combo',
            price: 11.99,
            description: 'Crispy chicken burger, fries, and drink',
            image: 'https://images.unsplash.com/photo-1606755962773-d324e9a13086?w=400&h=300&fit=crop',
            customizations: ['size', 'extras', 'removals']
        },
        {
            id: 'combo3',
            name: 'Fish Supreme Combo',
            price: 13.99,
            description: 'Fish fillet, fries, coleslaw, and drink',
            image: 'https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=400&h=300&fit=crop',
            customizations: ['size', 'extras', 'removals']
        },
        {
            id: 'combo4',
            name: 'Veggie Paradise Combo',
            price: 10.99,
            description: 'Plant-based burger, sweet potato fries, and drink',
            image: 'https://images.unsplash.com/photo-1520072959219-c595dc870360?w=400&h=300&fit=crop',
            customizations: ['size', 'extras', 'removals']
        },
        {
            id: 'combo5',
            name: 'BBQ Bacon Combo',
            price: 14.99,
            description: 'BBQ bacon burger, onion rings, and drink',
            image: 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400&h=300&fit=crop',
            customizations: ['size', 'extras', 'removals']
        },
        {
            id: 'combo6',
            name: 'Family Feast Combo',
            price: 24.99,
            description: '4 burgers, 2 large fries, 4 drinks',
            image: 'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=400&h=300&fit=crop',
            customizations: ['extras', 'removals']
        }
    ],
    food: [
        {
            id: 'food1',
            name: 'Classic Cheeseburger',
            price: 8.99,
            description: 'Beef patty with cheese, lettuce, tomato, and special sauce',
            image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&h=300&fit=crop',
            customizations: ['extras', 'removals']
        },
        {
            id: 'food2',
            name: 'Crispy Chicken Sandwich',
            price: 7.99,
            description: 'Breaded chicken breast with mayo and pickles',
            image: 'https://images.unsplash.com/photo-1606755456206-b25206be902b?w=400&h=300&fit=crop',
            customizations: ['extras', 'removals']
        },
        {
            id: 'food3',
            name: 'French Fries',
            price: 3.99,
            description: 'Golden crispy seasoned fries',
            image: 'https://images.unsplash.com/photo-1576107232684-1279f390859f?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'food4',
            name: 'Chicken Nuggets',
            price: 5.99,
            description: '6 piece crispy chicken nuggets',
            image: 'https://images.unsplash.com/photo-1562967916-eb82221dfb92?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'food5',
            name: 'Spicy Wings',
            price: 9.99,
            description: '8 buffalo wings with ranch dip',
            image: 'https://images.unsplash.com/photo-1608039755401-742074f0548d?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'food6',
            name: 'Fish & Chips',
            price: 11.99,
            description: 'Beer-battered cod with thick-cut chips',
            image: 'https://images.unsplash.com/photo-1544982503-9f984c14501a?w=400&h=300&fit=crop',
            customizations: ['extras', 'removals']
        },
        {
            id: 'food7',
            name: 'Onion Rings',
            price: 4.99,
            description: 'Crispy beer-battered onion rings',
            image: 'https://images.unsplash.com/photo-1639024471283-03518883512d?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'food8',
            name: 'Caesar Salad',
            price: 6.99,
            description: 'Fresh romaine lettuce with caesar dressing',
            image: 'https://images.unsplash.com/photo-1512852939750-1305098529bf?w=400&h=300&fit=crop',
            customizations: ['extras', 'removals']
        },
        {
            id: 'food9',
            name: 'Hot Dog',
            price: 4.99,
            description: 'All-beef hot dog with mustard and ketchup',
            image: 'https://images.unsplash.com/photo-1612392062798-2fa8ec40df87?w=400&h=300&fit=crop',
            customizations: ['extras', 'removals']
        },
        {
            id: 'food10',
            name: 'Mozzarella Sticks',
            price: 6.99,
            description: '6 crispy mozzarella sticks with marinara',
            image: 'https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        }
    ],
    drinks: [
        {
            id: 'drink1',
            name: 'Coca Cola',
            price: 2.99,
            description: 'Classic refreshing cola',
            image: 'https://images.unsplash.com/photo-1581006852262-e4307cf6283a?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'drink2',
            name: 'Sprite',
            price: 2.99,
            description: 'Lemon-lime soda',
            image: 'https://images.unsplash.com/photo-1625772452859-1c03d5bf1137?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'drink3',
            name: 'Orange Juice',
            price: 3.49,
            description: 'Fresh squeezed orange juice',
            image: 'https://images.unsplash.com/photo-1613478223719-2ab802602423?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'drink4',
            name: 'Iced Coffee',
            price: 4.99,
            description: 'Cold brew coffee with ice',
            image: 'https://images.unsplash.com/photo-1517701604599-bb29b565090c?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'drink5',
            name: 'Vanilla Milkshake',
            price: 5.99,
            description: 'Creamy vanilla milkshake',
            image: 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'drink6',
            name: 'Chocolate Milkshake',
            price: 5.99,
            description: 'Rich chocolate milkshake',
            image: 'https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'drink7',
            name: 'Strawberry Milkshake',
            price: 5.99,
            description: 'Fresh strawberry milkshake',
            image: 'https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'drink8',
            name: 'Lemonade',
            price: 3.99,
            description: 'Fresh squeezed lemonade',
            image: 'https://images.unsplash.com/photo-1523371683702-bf38ae2cad34?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'drink9',
            name: 'Hot Coffee',
            price: 2.99,
            description: 'Freshly brewed coffee',
            image: 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        },
        {
            id: 'drink10',
            name: 'Energy Drink',
            price: 4.99,
            description: 'Blue energy boost drink',
            image: 'https://images.unsplash.com/photo-1527960471264-932f39eb5846?w=400&h=300&fit=crop',
            customizations: ['size']
        },
        {
            id: 'drink11',
            name: 'Water Bottle',
            price: 1.99,
            description: 'Pure spring water',
            image: 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=400&h=300&fit=crop',
            customizations: []
        },
        {
            id: 'drink12',
            name: 'Iced Tea',
            price: 3.49,
            description: 'Sweet iced tea',
            image: 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400&h=300&fit=crop',
            customizations: ['size', 'extras']
        }
    ]
};

// Customization options
const customizationOptions = [
    {
        id: 'size',
        name: 'Size',
        type: 'radio',
        options: [
            { name: 'Small', price: 0 },
            { name: 'Medium', price: 1.50 },
            { name: 'Large', price: 3.00 }
        ]
    },
    {
        id: 'extras',
        name: 'Add Extras',
        type: 'checkbox',
        options: [
            { name: 'Extra Cheese', price: 1.00 },
            { name: 'Bacon', price: 2.00 },
            { name: 'Avocado', price: 1.50 },
            { name: 'Extra Sauce', price: 0.50 }
        ]
    },
    {
        id: 'removals',
        name: 'Remove Items',
        type: 'checkbox',
        options: [
            { name: 'No Onions', price: 0 },
            { name: 'No Pickles', price: 0 },
            { name: 'No Lettuce', price: 0 },
            { name: 'No Tomato', price: 0 }
        ]
    }
];

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