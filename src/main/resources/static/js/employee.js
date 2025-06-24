// Đợi DOM load hoàn toàn
document.addEventListener("DOMContentLoaded", function () {
  console.log("Employee validation loaded");

  // Update salary when position changes
  function updateSalary(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const salary = selectedOption.getAttribute("data-salary");
    const salaryInput = document.getElementById("salary");

    if (salaryInput) {
      if (salary && salary !== "") {
        salaryInput.value = salary;
        // Format display
        const formattedSalary = parseInt(salary).toLocaleString("vi-VN");
        salaryInput.setAttribute("title", formattedSalary + " ₫");
      } else {
        salaryInput.value = "";
        salaryInput.setAttribute("title", "");
      }
    }
  }

  // Make updateSalary available globally
  window.updateSalary = updateSalary;

  // Format salary display on input
  const salaryInput = document.getElementById("salary");
  if (salaryInput) {
    salaryInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      if (value) {
        const formatted = parseInt(value).toLocaleString("vi-VN");
        e.target.setAttribute("title", formatted + " ₫");
      }
    });
  }

  // Auto-generate username from full name
  const fullNameInput = document.getElementById("fullName");
  if (fullNameInput) {
    fullNameInput.addEventListener("input", function (e) {
      const fullName = e.target.value;
      const username = fullName
        .toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .replace(/đ/g, "d")
        .replace(/[^a-z0-9\s]/g, "")
        .replace(/\s+/g, "")
        .substring(0, 20); // Giới hạn độ dài username

      const usernameInput = document.getElementById("username");
      if (usernameInput) {
        usernameInput.value = username;
      }
    });
  }

  // Phone number validation - simplified version
  function validatePhoneNumber(phoneNumber) {
    const cleanPhone = phoneNumber.replace(/[\s\-\(\)]/g, "");
    // Kiểm tra: bắt đầu bằng 0 và có đúng 10 chữ số
    const phoneRegex = /^0\d{9}$/;
    return phoneRegex.test(cleanPhone);
  }

  // Format phone number display
  function formatPhoneNumber(input) {
    let value = input.value.replace(/\D/g, "");
    // Giới hạn tối đa 10 chữ số
    if (value.length > 10) {
      value = value.substring(0, 10);
    }

    if (value.length > 0) {
      if (value.length <= 3) {
        value = value;
      } else if (value.length <= 6) {
        value = value.substring(0, 3) + " " + value.substring(3);
      } else if (value.length <= 10) {
        value =
          value.substring(0, 3) +
          " " +
          value.substring(3, 6) +
          " " +
          value.substring(6);
      }
    }
    input.value = value;
  }

  // Password strength validation
  function validatePassword(password) {
    if (password.length < 6) {
      return { valid: false, message: "Mật khẩu phải có ít nhất 6 ký tự" };
    }
    return { valid: true, message: "Mật khẩu hợp lệ" };
  }

  // Username validation
  function validateUsername(username) {
    if (username.length < 3) {
      return { valid: false, message: "Tên đăng nhập phải có ít nhất 3 ký tự" };
    }
    if (!/^[a-zA-Z0-9_]+$/.test(username)) {
      return {
        valid: false,
        message: "Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới",
      };
    }
    return { valid: true, message: "Tên đăng nhập hợp lệ" };
  }

  // Full name validation
  function validateFullName(fullName) {
    if (fullName.trim().length < 2) {
      return { valid: false, message: "Họ tên phải có ít nhất 2 ký tự" };
    }
    if (fullName.trim().length > 50) {
      return { valid: false, message: "Họ tên không được quá 50 ký tự" };
    }
    if (!/^[a-zA-ZÀ-ỹ\s]+$/.test(fullName.trim())) {
      return {
        valid: false,
        message: "Họ tên chỉ được chứa chữ cái và khoảng trắng",
      };
    }
    return { valid: true, message: "Họ tên hợp lệ" };
  }

  // Address validation
  function validateAddress(address) {
    if (address.trim().length < 5) {
      return { valid: false, message: "Địa chỉ phải có ít nhất 5 ký tự" };
    }
    if (address.trim().length > 100) {
      return { valid: false, message: "Địa chỉ không được quá 100 ký tự" };
    }
    return { valid: true, message: "Địa chỉ hợp lệ" };
  }

  // Real-time validation
  const phoneInput = document.getElementById("phoneNumber");
  if (phoneInput) {
    phoneInput.addEventListener("input", function (e) {
      formatPhoneNumber(e.target);
    });

    phoneInput.addEventListener("blur", function (e) {
      const isValid = validatePhoneNumber(e.target.value);
      showValidationMessage(
        e.target,
        isValid,
        "Số điện thoại không đúng định dạng"
      );
    });
  }

  const passwordInput = document.getElementById("password");
  if (passwordInput) {
    passwordInput.addEventListener("blur", function (e) {
      const validation = validatePassword(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  const usernameInput = document.getElementById("username");
  if (usernameInput) {
    usernameInput.addEventListener("blur", function (e) {
      const validation = validateUsername(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  if (fullNameInput) {
    fullNameInput.addEventListener("blur", function (e) {
      const validation = validateFullName(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  // Real-time validation for address
  const addressInput = document.getElementById("address");
  if (addressInput) {
    addressInput.addEventListener("blur", function (e) {
      const validation = validateAddress(e.target.value);
      showValidationMessage(e.target, validation.valid, validation.message);
    });
  }

  // Show validation messages
  function showValidationMessage(input, isValid, message) {
    // Remove existing error message
    const existingError = input.parentNode.querySelector(".error-message");
    if (existingError) {
      existingError.remove();
    }

    if (!isValid) {
      input.style.borderColor = "#ef4444";
      input.style.backgroundColor = "#fef2f2";

      const errorDiv = document.createElement("div");
      errorDiv.className = "error-message text-red-500 text-sm mt-1";
      errorDiv.textContent = message;
      input.parentNode.appendChild(errorDiv);
    } else {
      input.style.borderColor = "#10b981";
      input.style.backgroundColor = "#f0fdf4";
    }
  }

  // Form submission validation
  const form = document.querySelector("form");
  if (form) {
    form.addEventListener("submit", function (e) {
      let isValid = true;
      const errors = [];

      // Validate all required fields
      const requiredFields = [
        "fullName",
        "phoneNumber",
        "address",
        "positionId",
        "username",
        "password",
      ];

      requiredFields.forEach((fieldId) => {
        const field = document.getElementById(fieldId);
        if (field && !field.value.trim()) {
          errors.push(
            `Trường ${field.previousElementSibling.textContent
              .replace("*", "")
              .trim()} là bắt buộc`
          );
          isValid = false;
        }
      });

      // Specific validations
      if (fullNameInput && fullNameInput.value) {
        const fullNameValidation = validateFullName(fullNameInput.value);
        if (!fullNameValidation.valid) {
          errors.push(fullNameValidation.message);
          isValid = false;
        }
      }

      if (addressInput && addressInput.value) {
        const addressValidation = validateAddress(addressInput.value);
        if (!addressValidation.valid) {
          errors.push(addressValidation.message);
          isValid = false;
        }
      }

      if (
        phoneInput &&
        phoneInput.value &&
        !validatePhoneNumber(phoneInput.value)
      ) {
        errors.push("Số điện thoại không đúng định dạng");
        isValid = false;
      }

      if (passwordInput && passwordInput.value) {
        const passwordValidation = validatePassword(passwordInput.value);
        if (!passwordValidation.valid) {
          errors.push(passwordValidation.message);
          isValid = false;
        }
      }

      if (usernameInput && usernameInput.value) {
        const usernameValidation = validateUsername(usernameInput.value);
        if (!usernameValidation.valid) {
          errors.push(usernameValidation.message);
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

  // Avatar preview function
  function previewAvatar(input) {
    const file = input.files[0];
    const preview = document.getElementById("avatarPreview");
    const placeholder = document.getElementById("avatarPlaceholder");

    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        preview.src = e.target.result;
        preview.classList.remove("hidden");
        placeholder.classList.add("hidden");
      };
      reader.readAsDataURL(file);
    } else {
      preview.classList.add("hidden");
      placeholder.classList.remove("hidden");
    }
  }

  // Make previewAvatar available globally
  window.previewAvatar = previewAvatar;
});
