document.addEventListener("DOMContentLoaded", function () {
  console.log("Marketing edit form loaded - using form submission");

  // Utility functions để hiển thị/ẩn lỗi
  function showError(fieldName, message) {
    const errorElement = document.getElementById(fieldName + "-error");
    const inputElement = document.querySelector(
      `input[name="${fieldName}"], select[name="${fieldName}"], textarea[name="${fieldName}"]`
    );

    if (errorElement) {
      errorElement.textContent = message;
      errorElement.classList.remove("hidden");
    }

    if (inputElement) {
      inputElement.classList.add("border-red-500");
      inputElement.classList.remove("border-gray-400");
    }
  }

  function hideError(fieldName) {
    const errorElement = document.getElementById(fieldName + "-error");
    const inputElement = document.querySelector(
      `input[name="${fieldName}"], select[name="${fieldName}"], textarea[name="${fieldName}"]`
    );

    if (errorElement) {
      errorElement.classList.add("hidden");
    }

    if (inputElement) {
      inputElement.classList.remove("border-red-500");
      inputElement.classList.add("border-gray-400");
    }
  }

  function clearAllErrors() {
    const errorFields = [
      "promotionName",
      "startDate",
      "endDate",
      "discountValue",
      "status",
    ];
    errorFields.forEach((field) => hideError(field));
  }

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

  // Validation real-time cho từng field
  function validatePromotionName() {
    const promotionName = document
      .querySelector('input[name="promotionName"]')
      .value.trim();

    if (!promotionName) {
      showError("promotionName", "Tên khuyến mãi không được để trống");
      return false;
    } else if (promotionName.length < 3) {
      showError("promotionName", "Tên khuyến mãi phải có ít nhất 3 ký tự");
      return false;
    } else if (promotionName.length > 100) {
      showError(
        "promotionName",
        "Tên khuyến mãi không được vượt quá 100 ký tự"
      );
      return false;
    } else {
      hideError("promotionName");
      return true;
    }
  }

  function validateDateRange() {
    const startDate = new Date(
      document.querySelector('input[name="startDate"]').value
    );
    const endDate = new Date(
      document.querySelector('input[name="endDate"]').value
    );

    if (endDate <= startDate) {
      showError("endDate", "Ngày kết thúc phải sau ngày bắt đầu");
      return false;
    } else {
      hideError("startDate");
      hideError("endDate");
      return true;
    }
  }

  function validateDiscountValue() {
    const discountValue = parseFloat(
      document.querySelector('input[name="discountValue"]').value
    );

    if (!discountValue || isNaN(discountValue)) {
      showError("discountValue", "Giá trị giảm giá không được để trống");
      return false;
    } else if (discountValue <= 0) {
      showError("discountValue", "Giá trị giảm giá phải lớn hơn 0");
      return false;
    } else if (discountValue > 100) {
      showError("discountValue", "Giá trị giảm giá không được vượt quá 100%");
      return false;
    } else {
      hideError("discountValue");
      return true;
    }
  }

  // Validation form trước khi submit
  function validateForm() {
    clearAllErrors();

    const isNameValid = validatePromotionName();
    const isDateValid = validateDateRange();
    const isDiscountValid = validateDiscountValue();

    return isNameValid && isDateValid && isDiscountValid;
  }

  // Event listeners
  const startDateInput = document.querySelector('input[name="startDate"]');
  const endDateInput = document.querySelector('input[name="endDate"]');
  const discountInput = document.querySelector('input[name="discountValue"]');
  const nameInput = document.querySelector('input[name="promotionName"]');

  // Real-time validation
  if (nameInput) {
    nameInput.addEventListener("blur", validatePromotionName);
    nameInput.addEventListener("input", function () {
      if (this.value.trim().length >= 3) {
        validatePromotionName();
      }
    });
  }

  // Auto-adjust end date khi start date thay đổi
  if (startDateInput) {
    startDateInput.addEventListener("change", function () {
      validateDates();
      setTimeout(validateDateRange, 100); // Delay để đảm bảo date được update
    });
  }

  if (endDateInput) {
    endDateInput.addEventListener("change", validateDateRange);
  }

  // Giới hạn giá trị discount và validate
  if (discountInput) {
    discountInput.addEventListener("input", function () {
      const value = parseFloat(this.value);
      if (value < 0.1 && value > 0) {
        this.value = 0.1;
      } else if (value > 100) {
        this.value = 100;
      }
    });

    discountInput.addEventListener("blur", validateDiscountValue);
  }

  // Gán functions vào global scope để HTML có thể gọi
  window.validateDates = validateDates;
  window.validateForm = validateForm;
});
