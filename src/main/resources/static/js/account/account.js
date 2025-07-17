document.addEventListener("DOMContentLoaded", function () {
  console.log("Account page loaded");

  // Format currency function
  function formatCurrency(amount) {
    if (!amount || amount === 0) return "0 ₫";

    // Remove any non-digit characters
    const numericAmount = amount.toString().replace(/[^\d]/g, "");

    if (!numericAmount) return "0 ₫";

    // Format with Vietnamese locale
    return parseInt(numericAmount).toLocaleString("vi-VN") + " ₫";
  }

  // Format salary display
  function formatSalaryDisplay() {
    const salaryElement = document.getElementById("salaryDisplay");

    if (salaryElement) {
      const originalValue = salaryElement.value;

      // Only format if not already formatted
      if (originalValue && !originalValue.includes("₫")) {
        salaryElement.value = formatCurrency(originalValue);
      }
    }
  }

  // Toggle password visibility
  window.togglePassword = function () {
    const passwordField = document.getElementById("passwordField");
    const eyeIcon = document.getElementById("eyeIcon");

    if (passwordField.getAttribute("type") === "password") {
      // Hiển thị thông báo thay vì mật khẩu thực
      passwordField.setAttribute("type", "text");
      passwordField.value = "Mật khẩu đã được mã hóa bảo mật";
      passwordField.style.color = "#6b7280"; // Gray color

      // Đổi icon thành "ẩn"
      eyeIcon.innerHTML = `
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
              d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.878 9.878L3 3m6.878 6.878L21 21"></path>
      `;
    } else {
      // Ẩn lại
      passwordField.setAttribute("type", "password");
      passwordField.value = "••••••••";
      passwordField.style.color = ""; // Reset color

      // Đổi icon thành "hiển thị"
      eyeIcon.innerHTML = `
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
              d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
              d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
      `;
    }
  };

  // Format salary when page loads
  formatSalaryDisplay();
});
