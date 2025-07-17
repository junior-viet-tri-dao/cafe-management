document.addEventListener("DOMContentLoaded", function () {
  // Format currency function
  function formatCurrency(amount) {
    if (!amount || amount === 0) return "0 đ";

    const numericAmount = amount.toString().replace(/[^\d]/g, "");
    if (!numericAmount) return "0 đ";

    return parseInt(numericAmount).toLocaleString("vi-VN") + " đ";
  }

  // Format all currency displays in payment modal
  function formatPaymentCurrency() {
    const currencyElements = document.querySelectorAll(".currency-display");

    currencyElements.forEach((element) => {
      const value =
        element.getAttribute("data-value") ||
        element.textContent.replace(/[^\d]/g, "");

      if (value && !isNaN(value) && value !== "0") {
        element.textContent = formatCurrency(value);
      }
    });
  }

  // Auto format when page loads
  formatPaymentCurrency();

  // Format when payment modal becomes visible
  const paymentModal = document.getElementById("paymentModal");
  if (paymentModal) {
    const observer = new MutationObserver(function (mutations) {
      mutations.forEach(function (mutation) {
        if (
          mutation.type === "attributes" &&
          mutation.attributeName === "class"
        ) {
          if (!paymentModal.classList.contains("hidden")) {
            setTimeout(formatPaymentCurrency, 50);
          }
        }
      });
    });

    observer.observe(paymentModal, {
      attributes: true,
      attributeFilter: ["class"],
    });
  }

  // Export function globally if needed
  window.formatPaymentCurrency = formatPaymentCurrency;
});
