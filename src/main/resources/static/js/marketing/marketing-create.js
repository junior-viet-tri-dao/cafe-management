document.addEventListener("DOMContentLoaded", function () {
  console.log("Marketing create form loaded - using form submission");

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
      "description",
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

  // Set ngày mặc định
  function setDefaultDates() {
    const today = new Date();
    const todayString = today.toISOString().split("T")[0];

    const startDateInput = document.querySelector('input[name="startDate"]');
    const endDateInput = document.querySelector('input[name="endDate"]');

    if (!startDateInput.value) {
      startDateInput.value = todayString;
    }

    if (!endDateInput.value) {
      const nextWeek = new Date(today);
      nextWeek.setDate(today.getDate() + 7);
      endDateInput.value = nextWeek.toISOString().split("T")[0];
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
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (startDate < today) {
      showError("startDate", "Ngày bắt đầu không được nhỏ hơn hôm nay");
      return false;
    }

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
    } else if (discountValue < 0.1) {
      showError("discountValue", "Giá trị giảm giá phải từ 0.1% trở lên");
      return false;
    } else if (discountValue > 100) {
      showError("discountValue", "Giá trị giảm giá không được vượt quá 100%");
      return false;
    } else {
      hideError("discountValue");
      return true;
    }
  }

  function validateDescription() {
    const description = document.querySelector(
      'textarea[name="description"]'
    ).value;

    if (description && description.length > 500) {
      showError("description", "Mô tả không được vượt quá 500 ký tự");
      return false;
    } else {
      hideError("description");
      return true;
    }
  }

  // Validation form trước khi submit
  function validateForm() {
    clearAllErrors();

    const isNameValid = validatePromotionName();
    const isDateValid = validateDateRange();
    const isDiscountValid = validateDiscountValue();
    const isDescriptionValid = validateDescription();

    return isNameValid && isDateValid && isDiscountValid && isDescriptionValid;
  }

  // Event listeners
  const startDateInput = document.querySelector('input[name="startDate"]');
  const endDateInput = document.querySelector('input[name="endDate"]');
  const discountInput = document.querySelector('input[name="discountValue"]');
  const nameInput = document.querySelector('input[name="promotionName"]');
  const descriptionInput = document.querySelector(
    'textarea[name="description"]'
  );

  // Set default dates on load
  setDefaultDates();

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
      setTimeout(validateDateRange, 100);
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

  // Validate description
  if (descriptionInput) {
    descriptionInput.addEventListener("input", validateDescription);
  }

  // Gán functions vào global scope để HTML có thể gọi
  window.validateForm = validateForm;

  console.log("Marketing create form loaded successfully");
});
