document.addEventListener("DOMContentLoaded", function () {
  // Dữ liệu mẫu chi tiêu
  let expenseData = [
    {
      date: "2014-12-23",
      displayDate: "23/12/2014",
      description: "Nộp thuế",
      amount: 1000000,
    },
    {
      date: "2014-12-24",
      displayDate: "24/12/2014",
      description: "Wifi",
      amount: 200000,
    },
    {
      date: "2014-12-30",
      displayDate: "30/12/2014",
      description: "Sửa đèn",
      amount: 20000,
    },
  ];

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

  // Render danh sách chi tiêu
  function renderExpenseList() {
    const total = expenseData.reduce((sum, item) => sum + item.amount, 0);

    const dataHTML = expenseData
      .map(
        (item, index) => `
            <div class="grid grid-cols-3 border-b border-gray-300 hover:bg-gray-50 group">
                <div class="px-4 py-3 border-r border-gray-300">${
                  item.displayDate
                }</div>
                <div class="px-4 py-3 border-r border-gray-300">${
                  item.description
                }</div>
                <div class="px-4 py-3 text-red-600 font-medium flex justify-between items-center">
                    ${formatMoney(item.amount)}
                    <button 
                        onclick="removeExpense(${index})"
                        class="ml-2 text-red-500 hover:text-red-700 opacity-0 group-hover:opacity-100 transition-opacity"
                        title="Xóa"
                    >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                        </svg>
                    </button>
                </div>
            </div>
        `
      )
      .join("");

    const formHTML = `
            <div class="grid grid-cols-3 bg-yellow-50 border-b border-gray-300">
                <div class="px-2 py-2 border-r border-gray-300">
                    <input
                        type="date"
                        id="newDate"
                        class="w-full px-2 py-1 border border-gray-400 rounded text-sm focus:outline-none focus:border-blue-500"
                        value="${newDateInput.value}"
                    />
                </div>
                <div class="px-2 py-2 border-r border-gray-300">
                    <input
                        type="text"
                        id="newDescription"
                        placeholder="Mô tả chi tiêu"
                        class="w-full px-2 py-1 border border-gray-400 rounded text-sm focus:outline-none focus:border-blue-500"
                        value="${newDescriptionInput.value}"
                    />
                </div>
                <div class="px-2 py-2">
                    <input
                        type="number"
                        id="newAmount"
                        placeholder="Số tiền"
                        min="0"
                        step="1000"
                        class="w-full px-2 py-1 border border-gray-400 rounded text-sm focus:outline-none focus:border-blue-500"
                        value="${newAmountInput.value}"
                    />
                </div>
            </div>
        `;

    const totalHTML = `
            <div class="grid grid-cols-3 bg-gray-100 border-t-2 border-gray-400 font-bold">
                <div class="px-4 py-3 border-r border-gray-300">Tổng</div>
                <div class="px-4 py-3 border-r border-gray-300"></div>
                <div class="px-4 py-3 text-red-600">${formatMoney(total)}</div>
            </div>
        `;

    expenseList.innerHTML = dataHTML + formHTML + totalHTML;

    // Rebind events after re-render
    bindFormEvents();
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

  // Khởi tạo
  renderExpenseList();
  console.log("Expense create form loaded successfully");
});
