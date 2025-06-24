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
