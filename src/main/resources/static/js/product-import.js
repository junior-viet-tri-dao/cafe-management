// Calculate total price
function calculateTotalPrice() {
  const quantity = parseInt(document.getElementById("quantity").value) || 0;
  const unitPriceValue = document
    .getElementById("unitPrice")
    .value.replace(/[\.\s]/g, "");
  const unitPrice = parseInt(unitPriceValue) || 0;

  if (quantity > 0 && unitPrice > 0) {
    const total = quantity * unitPrice;
    document.getElementById("totalPrice").value =
      total.toLocaleString("vi-VN") + " VNĐ";
  } else {
    document.getElementById("totalPrice").value = "";
  }
}

// Format price display
function formatPrice(input) {
  let value = input.value.replace(/\D/g, "");
  if (value) {
    const formatted = parseInt(value).toLocaleString("vi-VN");
    input.value = formatted;
  }
  calculateTotalPrice();
}

// Increase quantity
function increaseQuantity() {
  const quantityInput = document.getElementById("quantity");
  let currentValue = parseInt(quantityInput.value) || 0;
  quantityInput.value = currentValue + 1;
  calculateTotalPrice();
}

// Decrease quantity
function decreaseQuantity() {
  const quantityInput = document.getElementById("quantity");
  let currentValue = parseInt(quantityInput.value) || 0;
  if (currentValue > 1) {
    quantityInput.value = currentValue - 1;
    calculateTotalPrice();
  }
}

// Initialize event listeners
document.addEventListener("DOMContentLoaded", function () {
  const quantityInput = document.getElementById("quantity");
  const unitPriceInput = document.getElementById("unitPrice");

  // Format unit price on input
  unitPriceInput.addEventListener("input", function () {
    formatPrice(this);
  });

  // Calculate total on quantity change
  quantityInput.addEventListener("input", function () {
    calculateTotalPrice();
  });

  // Initial calculation
  calculateTotalPrice();
});
