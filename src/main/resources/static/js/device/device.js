// Đợi DOM load hoàn toàn
document.addEventListener("DOMContentLoaded", function () {
  console.log("Device validation loaded");

  // Format currency function - dùng chung cho list và form
  function formatCurrency(amount) {
    if (!amount || amount === 0) return "0 ₫";

    const numericAmount = amount.toString().replace(/[^\d]/g, "");
    if (!numericAmount) return "0 ₫";

    return parseInt(numericAmount).toLocaleString("vi-VN") + " ₫";
  }

  // Format all price amounts in the table (for list page)
  function formatAllPrices() {
    const priceElements = document.querySelectorAll(".purchase-price");
    const totalElements = document.querySelectorAll(".total-value");

    priceElements.forEach((element) => {
      const originalValue = element.textContent.trim();
      if (originalValue && !originalValue.includes("₫")) {
        element.textContent = formatCurrency(originalValue);
      }
    });

    totalElements.forEach((element) => {
      const originalValue = element.textContent.trim();
      if (originalValue && !originalValue.includes("₫")) {
        element.textContent = formatCurrency(originalValue);
      }
    });
  }

  // Auto hide success message after 5 seconds
  const successMessage = document.querySelector(".bg-green-50");
  if (successMessage) {
    setTimeout(function () {
      successMessage.style.transition = "opacity 0.5s ease-out";
      successMessage.style.opacity = "0";
      setTimeout(function () {
        successMessage.remove();
      }, 500);
    }, 5000);
  }

  // Observer to format prices when content changes dynamically
  const observer = new MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
      if (mutation.type === "childList") {
        const addedNodes = Array.from(mutation.addedNodes);
        addedNodes.forEach((node) => {
          if (node.nodeType === Node.ELEMENT_NODE) {
            const priceElements = node.querySelectorAll
              ? node.querySelectorAll(".purchase-price, .total-value")
              : [];
            priceElements.forEach((element) => {
              const originalValue = element.textContent.trim();
              if (!originalValue.includes("₫") && originalValue) {
                element.textContent = formatCurrency(originalValue);
              }
            });
          }
        });
      }
    });
  });

  // Start observing the table for changes (for list page)
  const tableBody = document.getElementById("deviceTableBody");
  if (tableBody) {
    observer.observe(tableBody, {
      childList: true,
      subtree: true,
    });
    // Format prices on initial load
    formatAllPrices();
  }

  // Get form elements (for create/edit pages)
  const equipmentNameInput = document.getElementById("equipmentName");
  const purchasePriceInput = document.getElementById("purchasePrice");
  const form = document.getElementById("deviceForm");
  const submitBtn = document.getElementById("submitBtn");
  const quantityInput = document.getElementById("quantity");
  const totalAmountInput = document.getElementById("totalAmount");
  const purchaseDateInput = document.getElementById("purchaseDate");

  // Set default values for create form
  if (form && quantityInput && !quantityInput.value) {
    quantityInput.value = "1";
  }

  // Set today's date if no date is set
  if (purchaseDateInput && !purchaseDateInput.value) {
    const today = new Date().toISOString().split("T")[0];
    purchaseDateInput.value = today;
  }

  // Validation functions
  function validateEquipmentName(name) {
    if (!name || name.trim().length === 0) {
      return { valid: false, message: "Tên thiết bị không được để trống" };
    }
    if (name.trim().length < 2) {
      return { valid: false, message: "Tên thiết bị phải có ít nhất 2 ký tự" };
    }
    if (name.trim().length > 100) {
      return { valid: false, message: "Tên thiết bị không được quá 100 ký tự" };
    }
    return { valid: true, message: "" };
  }

  function validatePurchasePrice(price) {
    if (!price || price.trim().length === 0) {
      return { valid: false, message: "Giá mua không được để trống" };
    }

    // Remove dots and spaces for validation
    const cleanPrice = price.replace(/[\.\s,]/g, "");
    const priceValue = parseFloat(cleanPrice);

    if (isNaN(priceValue)) {
      return { valid: false, message: "Giá mua phải là một số hợp lệ" };
    }
    if (priceValue < 1000) {
      return { valid: false, message: "Giá mua phải ít nhất 1,000 VNĐ" };
    }
    if (priceValue > 1000000000) {
      return {
        valid: false,
        message: "Giá mua không được quá 1,000,000,000 VNĐ",
      };
    }
    return { valid: true, message: "" };
  }

  function validatePurchaseDate(date) {
    if (!date) {
      return { valid: false, message: "Ngày mua không được để trống" };
    }
    const selectedDate = new Date(date);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (selectedDate > today) {
      return {
        valid: false,
        message: "Ngày mua không được lớn hơn ngày hiện tại",
      };
    }
    return { valid: true, message: "" };
  }

  // Format price display
  function formatPrice(input) {
    let value = input.value.replace(/\D/g, "");
    if (value) {
      value = parseInt(value).toLocaleString("vi-VN");
      input.value = value;
    }
  }

  // Show validation messages
  function showValidationMessage(input, isValid, message) {
    // Find the error container within the parent div
    let errorContainer = input.parentNode.querySelector(".error-container");

    // If error container doesn't exist in parent, look in parent's parent (for relative div structure)
    if (!errorContainer) {
      errorContainer =
        input.parentNode.parentNode?.querySelector(".error-container");
    }

    // Clear previous error
    if (errorContainer) {
      errorContainer.innerHTML = "";
    }

    if (!isValid && message) {
      input.style.borderColor = "#ef4444";
      input.style.backgroundColor = "#fef2f2";

      if (errorContainer) {
        errorContainer.innerHTML = `<span class="text-red-500 text-sm">${message}</span>`;
      }
    } else {
      input.style.borderColor = "#10b981";
      input.style.backgroundColor = "#f0fdf4";
    }
  }

  // Calculate total amount
  const calculateTotalAmount = () => {
    if (!quantityInput || !purchasePriceInput || !totalAmountInput) return;

    // Lấy giá trị số lượng
    const quantity = parseInt(quantityInput.value) || 0;
    // Lấy giá trị đơn giá (bỏ dấu . , ...)
    const price =
      parseInt(purchasePriceInput.value.replace(/[\.\s,]/g, "")) || 0;
    // Tính tổng tiền
    const total = quantity * price;

    if (total > 0) {
      totalAmountInput.value = total.toLocaleString("vi-VN") + " ₫";
    } else {
      totalAmountInput.value = "0 ₫";
    }
  };

  // Clean data before submission
  function cleanFormData() {
    // Clean purchase price - remove dots and commas, keep only numbers
    if (purchasePriceInput && purchasePriceInput.value) {
      const cleanPrice = purchasePriceInput.value.replace(/[\.\s,]/g, "");
      purchasePriceInput.value = cleanPrice;
    }

    // Remove totalAmount from form submission since it's calculated field
    if (totalAmountInput) {
      totalAmountInput.removeAttribute("name");
    }
  }

  // Real-time validation for equipment name
  if (equipmentNameInput) {
    equipmentNameInput.addEventListener("input", function (e) {
      const validation = validateEquipmentName(e.target.value);
      if (!validation.valid) {
        showValidationMessage(e.target, false, validation.message);
      } else {
        showValidationMessage(e.target, true, "");
      }
    });

    equipmentNameInput.addEventListener("blur", function (e) {
      const validation = validateEquipmentName(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  // Real-time validation for purchase price
  if (purchasePriceInput) {
    purchasePriceInput.addEventListener("input", function (e) {
      formatPrice(e.target);
      calculateTotalAmount(); // Tính lại tổng tiền
      const validation = validatePurchasePrice(e.target.value);
      if (!validation.valid) {
        showValidationMessage(e.target, false, validation.message);
      } else {
        showValidationMessage(e.target, true, "");
      }
    });

    purchasePriceInput.addEventListener("blur", function (e) {
      const validation = validatePurchasePrice(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  // Event listeners for quantity
  if (quantityInput) {
    quantityInput.addEventListener("input", function (e) {
      calculateTotalAmount(); // Tính lại tổng tiền
    });
  }

  // Calculate initial total on page load
  if (quantityInput && purchasePriceInput && totalAmountInput) {
    setTimeout(() => {
      calculateTotalAmount();
    }, 100);
  }

  // Form submission validation
  if (form) {
    form.addEventListener("submit", function (e) {
      let isValid = true;
      const errors = [];

      // Validate equipment name
      if (equipmentNameInput) {
        const nameValidation = validateEquipmentName(equipmentNameInput.value);
        if (!nameValidation.valid) {
          errors.push(nameValidation.message);
          showValidationMessage(
            equipmentNameInput,
            false,
            nameValidation.message
          );
          isValid = false;
        }
      }

      // Validate purchase price
      if (purchasePriceInput) {
        const priceValidation = validatePurchasePrice(purchasePriceInput.value);
        if (!priceValidation.valid) {
          errors.push(priceValidation.message);
          showValidationMessage(
            purchasePriceInput,
            false,
            priceValidation.message
          );
          isValid = false;
        }
      }

      // Validate purchase date
      if (purchaseDateInput) {
        const dateValidation = validatePurchaseDate(purchaseDateInput.value);
        if (!dateValidation.valid) {
          errors.push(dateValidation.message);
          showValidationMessage(
            purchaseDateInput,
            false,
            dateValidation.message
          );
          isValid = false;
        }
      }

      if (!isValid) {
        e.preventDefault();
        alert("Vui lòng kiểm tra lại:\n" + errors.join("\n"));
        return false;
      }

      // Clean data before submission
      cleanFormData();
    });
  }

  if (purchaseDateInput) {
    purchaseDateInput.addEventListener("blur", function (e) {
      const validation = validatePurchaseDate(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  // Make functions available globally if needed
  window.formatCurrency = formatCurrency;
});
