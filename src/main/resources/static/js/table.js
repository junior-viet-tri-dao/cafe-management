console.log("Table management script loaded");

// Đợi DOM load hoàn toàn
document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM loaded, initializing table management");

  // Select all functionality
  const selectAllCheckbox = document.getElementById("selectAll");
  if (selectAllCheckbox) {
    selectAllCheckbox.addEventListener("change", function () {
      const checkboxes = document.querySelectorAll(".table-checkbox");
      checkboxes.forEach((checkbox) => {
        checkbox.checked = this.checked;
      });
    });
  }

  // Individual checkbox functionality
  const tableCheckboxes = document.querySelectorAll(".table-checkbox");
  tableCheckboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", function () {
      const allCheckboxes = document.querySelectorAll(".table-checkbox");
      const checkedCheckboxes = document.querySelectorAll(
        ".table-checkbox:checked"
      );
      const selectAllCheckbox = document.getElementById("selectAll");

      if (selectAllCheckbox) {
        if (checkedCheckboxes.length === allCheckboxes.length) {
          selectAllCheckbox.checked = true;
          selectAllCheckbox.indeterminate = false;
        } else if (checkedCheckboxes.length > 0) {
          selectAllCheckbox.checked = false;
          selectAllCheckbox.indeterminate = true;
        } else {
          selectAllCheckbox.checked = false;
          selectAllCheckbox.indeterminate = false;
        }
      }
    });
  });

  // Button functionality với kiểm tra null
  const viewTableBtn = document.getElementById("viewTableBtn");
  if (viewTableBtn) {
    viewTableBtn.addEventListener("click", function () {
      alert("Xem bàn - Chức năng sẽ được phát triển");
    });
  }

  const moveTableBtn = document.getElementById("moveTableBtn");
  if (moveTableBtn) {
    moveTableBtn.addEventListener("click", function () {
      alert("Chuyển bàn - Chức năng sẽ được phát triển");
    });
  }

  const splitTableBtn = document.getElementById("splitTableBtn");
  if (splitTableBtn) {
    splitTableBtn.addEventListener("click", function () {
      alert("Tách bàn - Chức năng sẽ được phát triển");
    });
  }

  const mergeTableBtn = document.getElementById("mergeTableBtn");
  if (mergeTableBtn) {
    mergeTableBtn.addEventListener("click", function () {
      alert("Gộp bàn - Chức năng sẽ được phát triển");
    });
  }

  const cancelTableBtn = document.getElementById("cancelTableBtn");
  if (cancelTableBtn) {
    cancelTableBtn.addEventListener("click", function () {
      alert("Hủy bàn - Chức năng sẽ được phát triển");
    });
  }

  const bookTableBtn = document.getElementById("bookTableBtn");
  if (bookTableBtn) {
    bookTableBtn.addEventListener("click", function () {
      alert("Đặt bàn - Chức năng sẽ được phát triển");
    });
  }

  console.log("Table management initialized successfully");
});
