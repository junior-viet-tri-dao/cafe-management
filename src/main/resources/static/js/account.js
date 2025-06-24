function previewAvatar(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      document.getElementById("avatarPreview").src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}

function togglePassword() {
  const passwordInput = document.getElementById("passwordField");
  if (passwordInput.type === "password") {
    passwordInput.type = "text";
  } else {
    passwordInput.type = "password";
  }
}

function resetForm() {
  if (confirm("Bạn có chắc chắn muốn đặt lại tất cả thông tin?")) {
    document.querySelector("form").reset();
    document.getElementById("avatarPreview").src =
      "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face";
  }
}

// JavaScript for Avatar Preview and Salary Update
function previewAvatar(input) {
  if (input.files && input.files[0]) {
    const reader = new FileReader();

    reader.onload = function (e) {
      const preview = document.getElementById("avatarPreview");
      const placeholder = document.getElementById("avatarPlaceholder");

      preview.src = e.target.result;
      preview.classList.remove("hidden");
      placeholder.classList.add("hidden");
    };

    reader.readAsDataURL(input.files[0]);
  }
}

// Update salary when position changes
function updateSalary(selectElement) {
  const selectedOption = selectElement.options[selectElement.selectedIndex];
  const salary = selectedOption.getAttribute("data-salary");
  const salaryInput = document.getElementById("salary");

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

// Format salary display on input
document.getElementById("salary").addEventListener("input", function (e) {
  let value = e.target.value.replace(/\D/g, "");
  if (value) {
    const formatted = parseInt(value).toLocaleString("vi-VN");
    e.target.setAttribute("title", formatted + " ₫");
  }
});

// Auto-generate username from full name
document
  .getElementById("fullName")
  .addEventListener("input", function (e) {
    const fullName = e.target.value;
    const username = fullName
      .toLowerCase()
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "")
      .replace(/đ/g, "d")
      .replace(/[^a-z0-9\s]/g, "")
      .replace(/\s+/g, "")
      .substring(0, 20); // Giới hạn độ dài username

    document.getElementById("username").value = username;
  });

// Validate form before submit
document.querySelector("form").addEventListener("submit", function (e) {
  const position = document.getElementById("position").value;
  const salary = document.getElementById("salary").value;

  if (!position) {
    alert("Vui lòng chọn chức vụ");
    e.preventDefault();
    return false;
  }

  if (!salary) {
    alert("Lương chưa được cập nhật, vui lòng chọn lại chức vụ");
    e.preventDefault();
    return false;
  }
});

// Auto-save notification
document.querySelector("form").addEventListener("submit", function (e) {
  e.preventDefault();
  const button = e.target.querySelector('button[type="submit"]');
  const originalText = button.innerHTML;

  button.innerHTML = `
          <svg class="w-4 h-4 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
          </svg>
          Đang cập nhật...
        `;

  setTimeout(() => {
    button.innerHTML = `
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
            Cập nhật thành công!
          `;
    setTimeout(() => {
      button.innerHTML = originalText;
    }, 2000);
  }, 1000);
});

// Validate số điện thoại
function validatePhoneNumber(phoneNumber) {
  // Loại bỏ khoảng trắng và ký tự đặc biệt
  const cleanPhone = phoneNumber.replace(/[\s\-\(\)]/g, "");

  // Regex cho số điện thoại Việt Nam
  const phoneRegex = /^(\+84|84|0)(3[2-9]|5[2689]|7[06-9]|8[1-689]|9[0-46-9])\d{7}$/;

  return phoneRegex.test(cleanPhone);
}

// Format số điện thoại khi nhập
function formatPhoneNumber(input) {
  let value = input.value.replace(/\D/g, "");

  if (value.length > 0) {
    if (value.length <= 3) {
      value = value;
    } else if (value.length <= 6) {
      value = value.slice(0, 3) + " " + value.slice(3);
    } else if (value.length <= 10) {
      value =
        value.slice(0, 3) + " " + value.slice(3, 6) + " " + value.slice(6);
    } else {
      value =
        value.slice(0, 10) + " " + value.slice(10, 13) + " " + value.slice(13);
    }
  }

  input.value = value;
}

// Validate form trước khi submit
document.querySelector("form").addEventListener("submit", function (e) {
  const phoneInput = document.getElementById("phoneNumber");
  const phoneValue = phoneInput.value.trim();

  if (!validatePhoneNumber(phoneValue)) {
    e.preventDefault();
    alert(
      "Số điện thoại không đúng định dạng Việt Nam!\nVí dụ: 0901234567, +84901234567"
    );
    phoneInput.focus();
    return false;
  }
});

// Thêm event listener cho input số điện thoại
document.addEventListener("DOMContentLoaded", function () {
  const phoneInput = document.getElementById("phoneNumber");
  if (phoneInput) {
    phoneInput.addEventListener("input", function (e) {
      formatPhoneNumber(e.target);
    });

    phoneInput.addEventListener("blur", function (e) {
      if (e.target.value && !validatePhoneNumber(e.target.value)) {
        e.target.style.borderColor = "#ef4444";
        e.target.style.backgroundColor = "#fef2f2";
      } else {
        e.target.style.borderColor = "";
        e.target.style.backgroundColor = "";
      }
    });
  }
});
