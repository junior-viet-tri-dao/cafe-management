let availableStock = 15;
let currentUnit = "thùng";

// Update available quantity when product changes
function updateAvailableQuantity() {
  const select = document.getElementById("productId"); // Đúng id
  const selectedOption = select.options[select.selectedIndex];

  if (selectedOption.value) {
    availableStock = parseInt(selectedOption.dataset.available) || 0;
    currentUnit = selectedOption.dataset.unit || "";

    document.getElementById("stockQuantity").textContent = availableStock;
    document.getElementById("stockUnit").textContent = currentUnit;
    document.getElementById("unitDisplay").textContent = currentUnit;

    // Update quantity input max value
    const quantityInput = document.getElementById("quantity");
    quantityInput.max = availableStock;

    // Reset quantity if it exceeds available stock
    if (parseInt(quantityInput.value) > availableStock) {
      quantityInput.value = Math.min(availableStock, 1);
    }

    document.getElementById("availableStock").style.display = "";
    validateQuantity();
  } else {
    document.getElementById("availableStock").style.display = "none";
  }
}

// Validate quantity
function validateQuantity() {
  const quantityInput = document.getElementById("quantity");
  const quantityError = document.getElementById("quantityError");
  const submitBtn = document.getElementById("submitBtn");
  const quantity = parseInt(quantityInput.value) || 0;

  if (quantity > availableStock) {
    quantityError.classList.remove("hidden");
    quantityInput.classList.add(
      "border-red-500",
      "focus:border-red-500",
      "focus:ring-red-500"
    );
    quantityInput.classList.remove(
      "border-gray-300",
      "focus:border-orange-500",
      "focus:ring-orange-500"
    );
    submitBtn.disabled = true;
    submitBtn.classList.add("opacity-50", "cursor-not-allowed");
  } else {
    quantityError.classList.add("hidden");
    quantityInput.classList.remove(
      "border-red-500",
      "focus:border-red-500",
      "focus:ring-red-500"
    );
    quantityInput.classList.add(
      "border-gray-300",
      "focus:border-orange-500",
      "focus:ring-orange-500"
    );
    submitBtn.disabled = false;
    submitBtn.classList.remove("opacity-50", "cursor-not-allowed");
  }
}

// Increase quantity
function increaseQuantity() {
  const quantityInput = document.getElementById("quantity");
  let currentValue = parseInt(quantityInput.value) || 0;
  if (currentValue < availableStock) {
    quantityInput.value = currentValue + 1;
    validateQuantity();
  }
}

// Decrease quantity
function decreaseQuantity() {
  const quantityInput = document.getElementById("quantity");
  let currentValue = parseInt(quantityInput.value) || 0;
  if (currentValue > 1) {
    quantityInput.value = currentValue - 1;
    validateQuantity();
  }
}

// Initialize
document.addEventListener("DOMContentLoaded", function () {
  updateAvailableQuantity();

  // Add event listener for quantity input
  document
    .getElementById("quantity")
    .addEventListener("input", validateQuantity);
});
