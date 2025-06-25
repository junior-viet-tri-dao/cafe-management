document.addEventListener("DOMContentLoaded", function () {
  console.log("Marketing edit form loaded - using form submission");

  // Validation ngày
  function validateDates() {
    const startDate = document.querySelector('input[name="startDate"]').value;
    const endDate = document.querySelector('input[name="endDate"]').value;

    if (startDate && endDate) {
      const start = new Date(startDate);
      const end = new Date(endDate);

      if (end <= start) {
        const newEndDate = new Date(start);
        newEndDate.setDate(start.getDate() + 1);
        document.querySelector('input[name="endDate"]').value = newEndDate
          .toISOString()
          .split("T")[0];
      }
    }
  }

  // Validation form trước khi submit
  function validateForm() {
    const startDate = new Date(
      document.querySelector('input[name="startDate"]').value
    );
    const endDate = new Date(
      document.querySelector('input[name="endDate"]').value
    );
    const promotionName = document
      .querySelector('input[name="promotionName"]')
      .value.trim();
    const discountValue = parseFloat(
      document.querySelector('input[name="discountValue"]').value
    );

    if (!promotionName || promotionName.length < 3) {
      alert("Tên khuyến mãi phải có ít nhất 3 ký tự!");
      return false;
    }

    if (endDate <= startDate) {
      alert("Ngày kết thúc phải sau ngày bắt đầu!");
      return false;
    }

    if (!discountValue || discountValue <= 0 || discountValue > 100) {
      alert("Giá trị giảm giá phải từ 0.1% đến 100%!");
      return false;
    }

    return true;
  }

  // Event listeners
  const startDateInput = document.querySelector('input[name="startDate"]');
  const endDateInput = document.querySelector('input[name="endDate"]');
  const discountInput = document.querySelector('input[name="discountValue"]');

  // Auto-adjust end date khi start date thay đổi
  if (startDateInput) {
    startDateInput.addEventListener("change", validateDates);
  }

  if (endDateInput) {
    endDateInput.addEventListener("change", validateDates);
  }

  // Giới hạn giá trị discount
  if (discountInput) {
    discountInput.addEventListener("input", function () {
      const value = parseFloat(this.value);
      if (value < 0.1) {
        this.value = 0.1;
      } else if (value > 100) {
        this.value = 100;
      }
    });
  }

  // Gán functions vào global scope để HTML có thể gọi
  window.validateDates = validateDates;
  window.validateForm = validateForm;
});
