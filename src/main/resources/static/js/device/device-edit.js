document.addEventListener("DOMContentLoaded", function () {
  console.log("Device edit form loaded");

  // Format currency function
  function formatCurrency(amount) {
    if (!amount || amount === 0) return "0 ₫";
    
    const numericAmount = amount.toString().replace(/[^\d]/g, "");
    if (!numericAmount) return "0 ₫";
    
    return parseInt(numericAmount).toLocaleString("vi-VN") + " ₫";
  }

  // Format salary input
  const priceInput = document.getElementById("purchasePrice");
  if (priceInput) {
    // Display formatted value on load
    if (priceInput.value) {
      const formattedValue = formatCurrency(priceInput.value);
      priceInput.setAttribute("title", formattedValue);
    }

    priceInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      if (value) {
        e.target.value = value;
        const formatted = formatCurrency(value);
        e.target.setAttribute("title", formatted);
      }
    });

    priceInput.addEventListener("blur", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      if (value) {
        e.target.value = value;
        const formatted = formatCurrency(value);
        e.target.setAttribute("title", formatted);
      }
    });
  }

  // Calculate total amount
  const quantityInput = document.getElementById("quantity");
  const totalInput = document.getElementById("totalAmount");

  function calculateTotal() {
    if (priceInput && quantityInput && totalInput) {
      const price = parseInt(priceInput.value.replace(/\D/g, "")) || 0;
      const quantity = parseInt(quantityInput.value) || 0;
      const total = price * quantity;
      totalInput.value = total.toLocaleString("vi-VN");
    }
  }

  if (quantityInput) {
    quantityInput.addEventListener("input", calculateTotal);
  }

  if (priceInput) {
    priceInput.addEventListener("input", calculateTotal);
  }

  // Form validation before submit
  const form = document.querySelector("form");
  if (form) {
    form.addEventListener("submit", function (e) {
      const requiredFields = ["equipmentName", "purchasePrice", "quantity"];
      let isValid = true;
      const errors = [];

      requiredFields.forEach((fieldId) => {
        const field = document.getElementById(fieldId);
        if (field && !field.value.trim()) {
          const label = field.previousElementSibling?.textContent?.replace("*", "").trim() || fieldId;
          errors.push(`Trường ${label} là bắt buộc`);
          isValid = false;
        }
      });

      // Validate price
      if (priceInput && priceInput.value) {
        const price = parseInt(priceInput.value.replace(/\D/g, ""));
        if (isNaN(price) || price < 1000) {
          errors.push("Giá mua phải ít nhất 1,000 VNĐ");
          isValid = false;
        }
      }

      if (!isValid) {
        e.preventDefault();
        alert("Vui lòng kiểm tra lại:\n" + errors.join("\n"));
        return false;
      }
    });
  }
});