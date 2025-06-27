document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("productImportForm");
  const importDateInput = document.getElementById("importDate");
  const productSelect = document.getElementById("productId");
  const priceInput = document.getElementById("productPrice");
  const quantityInput = document.getElementById("quantity");
  const totalPriceInput = document.getElementById("totalPrice");

  // Tạo phần tử hiển thị lỗi nếu chưa có
  let errorDiv = document.createElement("div");
  errorDiv.id = "importDateError";
  errorDiv.style.color = "red";
  errorDiv.style.fontSize = "0.9em";
  errorDiv.style.marginTop = "4px";
  importDateInput.parentNode.appendChild(errorDiv);

  form.addEventListener("submit", function (e) {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const selectedDate = new Date(importDateInput.value);

    if (selectedDate > today) {
      errorDiv.textContent = "Ngày nhập không được là ngày trong tương lai";
      importDateInput.classList.add("border-red-500");
      e.preventDefault();
    } else {
      errorDiv.textContent = "";
      importDateInput.classList.remove("border-red-500");
    }
  });

  importDateInput.addEventListener("input", function () {
    errorDiv.textContent = "";
    importDateInput.classList.remove("border-red-500");
  });

  function formatMoney(amount) {
    if (!amount) return "";
    return Number(amount).toLocaleString("vi-VN") + " ₫";
  }

  function updateTotalPrice() {
    if (!productSelect || !quantityInput || !totalPriceInput) return;
    const selected = productSelect.options[productSelect.selectedIndex];
    const price = Number(selected.getAttribute("data-price")) || 0;
    const qty = Number(quantityInput.value) || 0;
    totalPriceInput.value = price && qty ? formatMoney(price * qty) : "";
  }

  if (productSelect && priceInput) {
    productSelect.addEventListener("change", function () {
      const selected = productSelect.options[productSelect.selectedIndex];
      const price = selected.getAttribute("data-price");
      priceInput.value = price ? formatMoney(price) : "";
      updateTotalPrice();
    });
    // Hiển thị đơn giá nếu đã chọn sẵn
    const selected = productSelect.options[productSelect.selectedIndex];
    const price = selected.getAttribute("data-price");
    priceInput.value = price ? formatMoney(price) : "";
    updateTotalPrice();
  }

  if (quantityInput) {
    quantityInput.addEventListener("input", updateTotalPrice);
  }
});
