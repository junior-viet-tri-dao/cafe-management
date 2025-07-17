// Format currency function - dùng chung
function formatCurrency(amount) {
  if (!amount || amount === 0) return "0 ₫";

  const numericAmount = amount.toString().replace(/[^\d]/g, "");
  if (!numericAmount) return "0 ₫";

  return parseInt(numericAmount).toLocaleString("vi-VN") + " ₫";
}

// Make function available globally
window.formatCurrency = formatCurrency;
