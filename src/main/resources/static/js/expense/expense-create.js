document.addEventListener("DOMContentLoaded", function () {
  // Lấy các elements
  const expenseList = document.getElementById("expenseList");
  const addExpenseBtn = document.getElementById("addExpenseBtn");
  const saveBtn = document.getElementById("saveBtn");
  const cancelBtn = document.getElementById("cancelBtn");
  const loadingState = document.getElementById("loadingState");
  const totalAmount = document.getElementById("totalAmount");

  // Form elements
  const newDateInput = document.getElementById("newDate");
  const newDescriptionInput = document.getElementById("newDescription");
  const newAmountInput = document.getElementById("newAmount");

  // Set ngày hiện tại
  const today = new Date();
  newDateInput.value = today.toISOString().split("T")[0];

  // Format số tiền
  function formatMoney(amount) {
    return new Intl.NumberFormat("vi-VN").format(amount);
  }

  // Format ngày hiển thị
  function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  // Bind events cho form
  function bindFormEvents() {
    const newDateInput = document.getElementById("newDate");
    const newDescriptionInput = document.getElementById("newDescription");
    const newAmountInput = document.getElementById("newAmount");

    // Enter để thêm
    [newDateInput, newDescriptionInput, newAmountInput].forEach((input) => {
      input.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
          addExpense();
        }
      });
    });

    // Format số tiền khi nhập
    newAmountInput.addEventListener("input", function () {
      const value = parseInt(this.value);
      if (value < 0) {
        this.value = 0;
      }
    });
  }

  // Thêm chi tiêu
  function addExpense() {
    const date = document.getElementById("newDate").value;
    const description = document.getElementById("newDescription").value.trim();
    const amount = document.getElementById("newAmount").value;

    if (!date) {
      alert("Vui lòng chọn ngày!");
      document.getElementById("newDate").focus();
      return;
    }

    if (!description) {
      alert("Vui lòng nhập mô tả chi tiêu!");
      document.getElementById("newDescription").focus();
      return;
    }

    if (!amount || amount <= 0) {
      alert("Vui lòng nhập số tiền hợp lệ!");
      document.getElementById("newAmount").focus();
      return;
    }

    // Thêm vào danh sách
    const newExpense = {
      date: date,
      displayDate: formatDate(date),
      description: description,
      amount: parseInt(amount),
    };

    expenseData.push(newExpense);

    // Sắp xếp theo ngày
    expenseData.sort((a, b) => new Date(a.date) - new Date(b.date));

    // Clear form
    document.getElementById("newDate").value = today
      .toISOString()
      .split("T")[0];
    document.getElementById("newDescription").value = "";
    document.getElementById("newAmount").value = "";

    // Re-render
    renderExpenseList();

    console.log("Đã thêm chi tiêu:", newExpense);
  }

  // Xóa chi tiêu
  window.removeExpense = function (index) {
    if (confirm("Bạn có chắc muốn xóa khoản chi tiêu này?")) {
      expenseData.splice(index, 1);
      renderExpenseList();
    }
  };

  // Lưu tất cả chi tiêu
  function saveAllExpenses() {
    if (expenseData.length === 0) {
      alert("Không có dữ liệu chi tiêu để lưu!");
      return;
    }

    showLoading(true);

    // Simulate API call
    setTimeout(() => {
      console.log("Lưu chi tiêu:", expenseData);
      showLoading(false);
      alert("Lưu chi tiêu thành công! (Demo)");

      // TODO: Redirect về trang danh sách
      // window.location.href = '/expenses';
    }, 1500);
  }

  // Hiển thị loading
  function showLoading(show) {
    if (show) {
      loadingState.style.display = "block";
      saveBtn.disabled = true;
      addExpenseBtn.disabled = true;
    } else {
      loadingState.style.display = "none";
      saveBtn.disabled = false;
      addExpenseBtn.disabled = false;
    }
  }

  // Event listeners
  addExpenseBtn.addEventListener("click", addExpense);

  saveBtn.addEventListener("click", saveAllExpenses);

  cancelBtn.addEventListener("click", function () {
    if (confirm("Bạn có chắc muốn hủy? Dữ liệu sẽ không được lưu.")) {
      console.log("Đã hủy thêm chi tiêu");
      // window.location.href = '/expenses';
    }
  });

  // Format số tiền cho các phần tử hiển thị số tiền
  document
    .querySelectorAll(".text-red-600.font-medium span")
    .forEach(function (el) {
      let value = el.textContent.replace(/\D/g, "");
      if (value) {
        el.textContent = formatMoney(value) + " đ";
      } else {
        el.textContent = "0 đ";
      }
    });

  document.querySelectorAll(".date-value").forEach(function (el) {
    let value = el.textContent.trim();
    el.textContent = formatDate(value);
  });

  // Khởi tạo
  renderExpenseList();
  console.log("Expense create form loaded successfully");
});
