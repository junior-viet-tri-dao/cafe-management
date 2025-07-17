document.addEventListener("DOMContentLoaded", function () {
  const addExpenseBtn = document.getElementById("addExpenseBtn");
  const fromDateInput = document.getElementById("fromDate");
  const toDateInput = document.getElementById("toDate");

  addExpenseBtn.addEventListener("click", function () {
    window.location.href = "/expense/create";
  });

  // Set min date cho date inputs
  fromDateInput.addEventListener("change", function () {
    toDateInput.min = this.value;
  });

  // Enter để submit form
  [fromDateInput, toDateInput].forEach((input) => {
    input.addEventListener("keypress", function (e) {
      if (e.key === "Enter") {
        input.form.submit();
      }
    });
  });

  // Format tiền tệ cho các phần tử có class 'money-value'
  document.querySelectorAll(".money-value").forEach(function (el) {
    let value = el.textContent.replace(/\D/g, ""); // Lấy số, bỏ ký tự khác
    if (value) {
      el.textContent = formatMoneyVND(value);
    } else {
      el.textContent = "0 đ";
    }
  });

  document.querySelectorAll(".date-value").forEach(function (el) {
    let value = el.textContent.trim();
    el.textContent = formatDateDMY(value);
  });
});
