// Đợi DOM load hoàn toàn
document.addEventListener("DOMContentLoaded", function () {
  console.log("Device validation loaded");

  // Get form elements
  const deviceNameInput = document.getElementById("deviceName");
  const purchaseDateInput = document.getElementById("purchaseDate");
  const quantityInput = document.getElementById("quantity");
  const unitPriceInput = document.getElementById("unitPrice");
  const totalPriceInput = document.getElementById("totalPrice");
  const form = document.getElementById("deviceForm");

  // Validation functions
  function validateDeviceName(name) {
    if (name.trim().length < 2) {
      return { valid: false, message: "Tên thiết bị phải có ít nhất 2 ký tự" };
    }
    if (name.trim().length > 100) {
      return { valid: false, message: "Tên thiết bị không được quá 100 ký tự" };
    }
    return { valid: true, message: "Tên thiết bị hợp lệ" };
  }

  function validatePurchaseDate(date) {
    if (!date) {
      return { valid: false, message: "Ngày mua là bắt buộc" };
    }

    const selectedDate = new Date(date);
    const today = new Date();
    today.setHours(23, 59, 59, 999); // Set to end of today

    if (selectedDate > today) {
      return { valid: false, message: "Ngày mua không được là ngày tương lai" };
    }

    // Check if date is too old (more than 50 years ago)
    const fiftyYearsAgo = new Date();
    fiftyYearsAgo.setFullYear(fiftyYearsAgo.getFullYear() - 50);

    if (selectedDate < fiftyYearsAgo) {
      return { valid: false, message: "Ngày mua không hợp lệ" };
    }

    return { valid: true, message: "Ngày mua hợp lệ" };
  }

  function validateQuantity(quantity) {
    const qty = parseInt(quantity);
    if (isNaN(qty) || qty < 1) {
      return { valid: false, message: "Số lượng phải là số nguyên dương" };
    }
    if (qty > 10000) {
      return { valid: false, message: "Số lượng không được quá 10.000" };
    }
    return { valid: true, message: "Số lượng hợp lệ" };
  }

  function validateUnitPrice(price) {
    // Remove dots and spaces for validation
    const cleanPrice = price.replace(/[\.\s]/g, "");
    const priceValue = parseInt(cleanPrice);

    if (isNaN(priceValue) || priceValue < 1000) {
      return { valid: false, message: "Đơn giá phải ít nhất 1.000 VNĐ" };
    }
    if (priceValue > 1000000000) {
      return { valid: false, message: "Đơn giá không được quá 1 tỷ VNĐ" };
    }
    return { valid: true, message: "Đơn giá hợp lệ" };
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

  // Calculate total price
  function calculateTotalPrice() {
    const quantity = parseInt(quantityInput.value) || 0;
    const unitPriceValue = unitPriceInput.value.replace(/[\.\s]/g, "");
    const unitPrice = parseInt(unitPriceValue) || 0;

    if (quantity > 0 && unitPrice > 0) {
      const total = quantity * unitPrice;
      totalPriceInput.value = total.toLocaleString("vi-VN") + " VNĐ";
    } else {
      totalPriceInput.value = "";
    }
  }

  // Show validation messages
  function showValidationMessage(input, isValid, message) {
    // Remove existing error message
    const existingError = input.parentNode.querySelector(".error-message");
    if (existingError) {
      existingError.remove();
    }

    if (!isValid) {
      input.style.borderColor = "#ef4444";
      input.style.backgroundColor = "#fef2f2";

      const errorDiv = document.createElement("div");
      errorDiv.className = "error-message text-red-500 text-sm mt-1";
      errorDiv.textContent = message;
      input.parentNode.appendChild(errorDiv);
    } else {
      input.style.borderColor = "#10b981";
      input.style.backgroundColor = "#f0fdf4";
    }
  }

  // Real-time validation
  if (deviceNameInput) {
    deviceNameInput.addEventListener("blur", function (e) {
      const validation = validateDeviceName(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });

    deviceNameInput.addEventListener("input", function (e) {
      // Reset styles when typing
      e.target.style.borderColor = "#d1d5db";
      e.target.style.backgroundColor = "#ffffff";
      const existingError = e.target.parentNode.querySelector(".error-message");
      if (existingError) {
        existingError.remove();
      }
    });
  }

  if (purchaseDateInput) {
    purchaseDateInput.addEventListener("blur", function (e) {
      const validation = validatePurchaseDate(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  if (quantityInput) {
    quantityInput.addEventListener("blur", function (e) {
      const validation = validateQuantity(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });

    quantityInput.addEventListener("input", function (e) {
      calculateTotalPrice();
    });
  }

  if (unitPriceInput) {
    unitPriceInput.addEventListener("input", function (e) {
      formatPrice(e.target);
    });

    unitPriceInput.addEventListener("blur", function (e) {
      const validation = validateUnitPrice(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  // Form submission validation
  if (form) {
    form.addEventListener("submit", function (e) {
      let isValid = true;
      const errors = [];

      // Validate all fields
      const nameValidation = validateDeviceName(deviceNameInput.value);
      if (!nameValidation.valid) {
        errors.push(nameValidation.message);
        showValidationMessage(deviceNameInput, false, nameValidation.message);
        isValid = false;
      }

      const dateValidation = validatePurchaseDate(purchaseDateInput.value);
      if (!dateValidation.valid) {
        errors.push(dateValidation.message);
        showValidationMessage(purchaseDateInput, false, dateValidation.message);
        isValid = false;
      }

      const quantityValidation = validateQuantity(quantityInput.value);
      if (!quantityValidation.valid) {
        errors.push(quantityValidation.message);
        showValidationMessage(quantityInput, false, quantityValidation.message);
        isValid = false;
      }

      const priceValidation = validateUnitPrice(unitPriceInput.value);
      if (!priceValidation.valid) {
        errors.push(priceValidation.message);
        showValidationMessage(unitPriceInput, false, priceValidation.message);
        isValid = false;
      }

      if (!isValid) {
        e.preventDefault();
        alert("Vui lòng kiểm tra lại:\n" + errors.join("\n"));
        return false;
      }

      // Convert formatted price back to number for form submission
      const cleanPrice = unitPriceInput.value.replace(/[\.\s]/g, "");
      unitPriceInput.value = cleanPrice;
    });
  }

  // Set default date to today
  if (purchaseDateInput) {
    const today = new Date().toISOString().split("T")[0];
    purchaseDateInput.value = today;
  }
});
