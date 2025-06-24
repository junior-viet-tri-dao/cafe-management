document.addEventListener("DOMContentLoaded", function () {
  // Dữ liệu mẫu thu chi
  let expenseData = [
    {
      date: "2014-12-23",
      displayDate: "23/12/2014",
      revenue: 500000,
      expense: 1000000,
    },
    {
      date: "2014-12-24",
      displayDate: "24/12/2014",
      revenue: 1000000,
      expense: 200000,
    },
    {
      date: "2014-12-25",
      displayDate: "25/12/2014",
      revenue: 1500000,
      expense: 400000,
    },
  ];

  // Lấy các elements
  const expenseList = document.getElementById("expenseList");
  const addExpenseBtn = document.getElementById("addExpenseBtn");
  const viewBtn = document.getElementById("viewBtn");
  const fromDateInput = document.getElementById("fromDate");
  const toDateInput = document.getElementById("toDate");
  const emptyState = document.getElementById("emptyState");
  const loadingState = document.getElementById("loadingState");
  const totalRevenue = document.getElementById("totalRevenue");
  const totalExpense = document.getElementById("totalExpense");
  const profit = document.getElementById("profit");

  // Format số tiền
  function formatMoney(amount) {
    return new Intl.NumberFormat("vi-VN").format(amount);
  }

  // Render danh sách thu chi
  function renderExpenseList(data = expenseData) {
    if (data.length === 0) {
      expenseList.style.display = "none";
      emptyState.style.display = "block";
      updateSummary([], []);
      return;
    }

    expenseList.style.display = "block";
    emptyState.style.display = "none";

    // Tính tổng
    const totalRev = data.reduce((sum, item) => sum + item.revenue, 0);
    const totalExp = data.reduce((sum, item) => sum + item.expense, 0);

    const html =
      data
        .map(
          (item) => `
            <div class="grid grid-cols-3 border-b border-gray-300 hover:bg-gray-50">
                <div class="px-4 py-3 border-r border-gray-300">${
                  item.displayDate
                }</div>
                <div class="px-4 py-3 border-r border-gray-300 text-green-600 font-medium">${formatMoney(
                  item.revenue
                )}</div>
                <div class="px-4 py-3 text-red-600 font-medium">${formatMoney(
                  item.expense
                )}</div>
            </div>
        `
        )
        .join("") +
      `
            <div class="grid grid-cols-3 bg-gray-100 border-t-2 border-gray-400 font-bold">
                <div class="px-4 py-3 border-r border-gray-300">Tổng</div>
                <div class="px-4 py-3 border-r border-gray-300 text-green-600">${formatMoney(
                  totalRev
                )}</div>
                <div class="px-4 py-3 text-red-600">${formatMoney(
                  totalExp
                )}</div>
            </div>
        `;

    expenseList.innerHTML = html;
    updateSummary(totalRev, totalExp);
  }

  // Cập nhật summary
  function updateSummary(totalRev, totalExp) {
    const profitAmount = totalRev - totalExp;

    totalRevenue.textContent = formatMoney(totalRev);
    totalExpense.textContent = formatMoney(totalExp);

    profit.textContent =
      (profitAmount >= 0 ? "+" : "") + formatMoney(profitAmount);
    profit.className = `text-lg font-bold ${
      profitAmount >= 0 ? "text-blue-600" : "text-red-600"
    }`;
  }

  // Lọc dữ liệu theo ngày
  function filterDataByDate() {
    const fromDate = fromDateInput.value;
    const toDate = toDateInput.value;

    if (!fromDate || !toDate) {
      alert("Vui lòng chọn cả ngày bắt đầu và ngày kết thúc!");
      return;
    }

    if (new Date(fromDate) > new Date(toDate)) {
      alert("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
      return;
    }

    showLoading(true);

    // Simulate API call
    setTimeout(() => {
      const filteredData = expenseData.filter((item) => {
        return item.date >= fromDate && item.date <= toDate;
      });

      renderExpenseList(filteredData);
      showLoading(false);
    }, 500);
  }

  // Hiển thị loading
  function showLoading(show) {
    if (show) {
      expenseList.style.display = "none";
      emptyState.style.display = "none";
      loadingState.style.display = "block";
    } else {
      loadingState.style.display = "none";
    }
  }

  // Event listeners
  viewBtn.addEventListener("click", function () {
    filterDataByDate();
  });

  addExpenseBtn.addEventListener("click", function () {
    // TODO: Chuyển đến trang thêm chi tiêu
    console.log("Chuyển đến trang thêm chi tiêu");
    window.location.href = "/expenses/create";
    alert("Chức năng thêm chi tiêu sẽ được phát triển");
  });

  // Set min date cho date inputs
  fromDateInput.addEventListener("change", function () {
    toDateInput.min = this.value;
  });

  // Enter để xem
  [fromDateInput, toDateInput].forEach((input) => {
    input.addEventListener("keypress", function (e) {
      if (e.key === "Enter") {
        viewBtn.click();
      }
    });
  });

  // Khởi tạo
  renderExpenseList();
  console.log("Expense management loaded successfully");
});
