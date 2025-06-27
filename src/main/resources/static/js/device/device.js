// Đợi DOM load hoàn toàn
document.addEventListener("DOMContentLoaded", function () {
  console.log("Device validation loaded");

  // Get form elements
  const equipmentNameInput = document.getElementById("equipmentName");
  const purchasePriceInput = document.getElementById("purchasePrice");
  const form = document.getElementById("deviceForm");
  const submitBtn = document.getElementById("submitBtn");
  const quantityInput = document.getElementById("quantity");
  const totalAmountInput = document.getElementById("totalAmount");
  const purchaseDateInput = document.getElementById("purchaseDate");

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
      return { valid: false, message: "Giá mua phải là số" };
    }
    if (priceValue < 1000) {
      return { valid: false, message: "Giá mua phải ít nhất 1.000 VNĐ" };
    }
    if (priceValue > 1000000000) {
      return { valid: false, message: "Giá mua không được quá 1 tỷ VNĐ" };
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
        message: "Ngày mua không được là ngày trong tương lai",
      };
    }
    return { valid: true, message: "" };
  }

  // Format price display
  function formatPrice(input) {
    let value = input.value.replace(/\D/g, "");
    if (value) {
      // Convert to number and format
      const formatted = parseInt(value).toLocaleString("vi-VN");
      input.value = formatted;
    }
  }

  // Show validation messages
  function showValidationMessage(input, isValid, message) {
    const errorContainer = input.parentNode.querySelector(".error-container");

    // Clear previous error
    if (errorContainer) {
      errorContainer.innerHTML = "";
    }

    if (!isValid && message) {
      input.classList.add("border-red-500", "bg-red-50");
      input.classList.remove("border-green-500", "bg-green-50");

      if (errorContainer) {
        const errorDiv = document.createElement("div");
        errorDiv.className = "text-red-500 text-sm mt-1 flex items-center";
        errorDiv.innerHTML = `
          <svg class="w-4 h-4 mr-1" fill="currentColor" viewBox="0 0 20 20">
            <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
          </svg>
          ${message}
        `;
        errorContainer.appendChild(errorDiv);
      }
    } else if (isValid) {
      input.classList.add("border-green-500", "bg-green-50");
      input.classList.remove("border-red-500", "bg-red-50");
    } else {
      input.classList.remove(
        "border-red-500",
        "bg-red-50",
        "border-green-500",
        "bg-green-50"
      );
    }
  }

  // Real-time validation for equipment name
  if (equipmentNameInput) {
    equipmentNameInput.addEventListener("input", function (e) {
      const validation = validateEquipmentName(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
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
      const validation = validatePurchasePrice(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });

    purchasePriceInput.addEventListener("blur", function (e) {
      const validation = validatePurchasePrice(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  const calculateTotalAmount = () => {
    if (!quantityInput || !purchasePriceInput || !totalAmountInput) return;
    // Lấy giá trị số lượng
    const quantity = parseInt(quantityInput.value) || 0;
    // Lấy giá trị đơn giá (bỏ dấu . , ...)
    const price =
      parseInt(purchasePriceInput.value.replace(/[\.\s,]/g, "")) || 0;
    // Tính tổng tiền
    const total = quantity * price;
    // Hiển thị tổng tiền đã format
    totalAmountInput.value =
      total > 0 ? total.toLocaleString("vi-VN") + " VNĐ" : "";
  };

  if (quantityInput && purchasePriceInput && totalAmountInput) {
    quantityInput.addEventListener("input", calculateTotalAmount);
    purchasePriceInput.addEventListener("input", calculateTotalAmount);
  }

  // Form submission validation
  if (form) {
    form.addEventListener("submit", function (e) {
      let isValid = true;
      const errors = [];

      // Validate equipment name
      const nameValidation = validateEquipmentName(equipmentNameInput.value);
      if (!nameValidation.valid) {
        errors.push("Tên thiết bị: " + nameValidation.message);
        showValidationMessage(
          equipmentNameInput,
          false,
          nameValidation.message
        );
        isValid = false;
      }

      // Validate purchase price
      const priceValidation = validatePurchasePrice(purchasePriceInput.value);
      if (!priceValidation.valid) {
        errors.push("Giá mua: " + priceValidation.message);
        showValidationMessage(
          purchasePriceInput,
          false,
          priceValidation.message
        );
        isValid = false;
      }

      // Validate purchase date
      const dateValidation = validatePurchaseDate(purchaseDateInput.value);
      if (!dateValidation.valid) {
        errors.push("Ngày mua: " + dateValidation.message);
        showValidationMessage(purchaseDateInput, false, dateValidation.message);
        isValid = false;
      }

      if (!isValid) {
        e.preventDefault();
        // Không cần alert nữa, chỉ focus vào trường lỗi đầu tiên
        const firstErrorField = form.querySelector(".border-red-500");
        if (firstErrorField) {
          firstErrorField.focus();
          firstErrorField.scrollIntoView({
            behavior: "smooth",
            block: "center",
          });
        }
        return false;
      }

      // Convert formatted price back to number for form submission
      const cleanPrice = purchasePriceInput.value.replace(/[\.\s,]/g, "");
      purchasePriceInput.value = cleanPrice;

      // Disable submit button to prevent double submission
      if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.textContent = "Đang thêm...";
        submitBtn.classList.add("opacity-50", "cursor-not-allowed");
      }
    });
  }

  if (purchaseDateInput) {
    purchaseDateInput.addEventListener("blur", function (e) {
      const validation = validatePurchaseDate(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }
});
