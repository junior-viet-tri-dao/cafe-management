
function formatMoney(amount) {
  return amount.toLocaleString("vi-VN");
}

document.addEventListener("DOMContentLoaded", function () {
  // Set ngày hiện tại cho input date
  const importDate = document.getElementById("importDate");
  if (importDate) {
    const today = new Date().toISOString().split("T")[0];
    importDate.value = today;
  }

  // Khi chọn thiết bị, tự động fill đơn giá
  const equipmentSelect = document.getElementById("equipmentSelect");
  if (equipmentSelect) {
    equipmentSelect.addEventListener("change", function () {
      const selectedOption =
        equipmentSelect.options[equipmentSelect.selectedIndex];
      const price = selectedOption.getAttribute("data-price");
      document.getElementById("purchasePrice").value = price
        ? formatMoney(Number(price))
        : "";
      updateTotalPrice();
    });
  }

  // Khi thay đổi số lượng, cập nhật tổng tiền
  const quantityInput = document.getElementById("quantity");
  if (quantityInput) {
    quantityInput.addEventListener("input", updateTotalPrice);
  }

  function updateTotalPrice() {
    const selectedOption =
      equipmentSelect.options[equipmentSelect.selectedIndex];
    const price = Number(selectedOption.getAttribute("data-price")) || 0;
    const qty = parseInt(quantityInput.value) || 0;
    document.getElementById("totalPrice").value = formatMoney(price * qty);
  }

  // Khởi tạo tổng tiền lần đầu
  updateTotalPrice();

  // Xử lý submit (demo)
  const form = document.getElementById("deviceImportForm");
  if (form) {
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      alert("Đã nhập thiết bị (BE xử lý sau)");
    });
  }
});
