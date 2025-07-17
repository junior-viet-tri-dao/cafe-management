document.addEventListener("DOMContentLoaded", function () {
  console.log("Employee edit form loaded");

  // Avatar preview function
  function previewAvatar(input) {
    if (input.files && input.files[0]) {
      const reader = new FileReader();

      reader.onload = function (e) {
        const preview = document.getElementById("avatarPreview");
        const placeholder = document.getElementById("avatarPlaceholder");

        if (preview) {
          preview.src = e.target.result;
          preview.classList.remove("hidden");
          preview.classList.add("block");
        }

        if (placeholder) {
          placeholder.classList.add("hidden");
        }
      };

      reader.readAsDataURL(input.files[0]);
    }
  }

  // Make previewAvatar available globally
  window.previewAvatar = previewAvatar;

  // Format salary input
  const salaryInput = document.getElementById("salary");
  if (salaryInput) {
    // Display formatted value on load
    if (salaryInput.value) {
      const formattedValue = formatCurrency(salaryInput.value);
      salaryInput.setAttribute("title", formattedValue);
    }

    salaryInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      if (value) {
        e.target.value = value;
        const formatted = formatCurrency(value);
        e.target.setAttribute("title", formatted);
      }
    });

    salaryInput.addEventListener("blur", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      if (value) {
        e.target.value = value;
        const formatted = formatCurrency(value);
        e.target.setAttribute("title", formatted);
      }
    });
  }

  // Phone number formatting
  const phoneInput = document.getElementById("phone");
  if (phoneInput) {
    phoneInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
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
      e.target.value = value;
    });
  }

  // Position change handler (if needed)
  const positionSelect = document.getElementById("position");
  if (positionSelect) {
    positionSelect.addEventListener("change", function (e) {
      // You can add logic here to update salary based on position if needed
      console.log("Position changed:", e.target.value);
    });
  }

  // Form validation before submit
  const form = document.querySelector("form");
  if (form) {
    form.addEventListener("submit", function (e) {
      const requiredFields = ["fullName", "position", "salary", "phone"];
      let isValid = true;
      const errors = [];

      requiredFields.forEach((fieldId) => {
        const field = document.getElementById(fieldId);
        if (field && !field.value.trim()) {
          const label =
            field.previousElementSibling?.textContent
              ?.replace("*", "")
              .trim() || fieldId;
          errors.push(`Trường ${label} là bắt buộc`);
          isValid = false;
        }
      });

      // Validate phone number format
      if (phoneInput && phoneInput.value) {
        const cleanPhone = phoneInput.value.replace(/[\s\-\(\)]/g, "");
        const phoneRegex = /^0\d{9}$/;
        if (!phoneRegex.test(cleanPhone)) {
          errors.push("Số điện thoại không đúng định dạng");
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
});
