document.addEventListener("DOMContentLoaded", function () {
  // Sample data
  const reportData = {
    all: [
      { date: "23/12/2014", revenue: 500000, expense: 1000000 },
      { date: "24/12/2014", revenue: 1000000, expense: 200000 },
      { date: "25/12/2014", revenue: 1500000, expense: 400000 },
    ],
    import: [
      { date: "23/12/2014", revenue: 300000, expense: 800000 },
      { date: "24/12/2014", revenue: 800000, expense: 150000 },
    ],
    sales: [
      { date: "23/12/2014", revenue: 600000, expense: 900000 },
      { date: "24/12/2014", revenue: 1200000, expense: 180000 },
    ],
  };

  // Get elements
  const reportTypeRadios = document.querySelectorAll(
    'input[name="reportType"]'
  );
  const exportTypeRadios = document.querySelectorAll(
    'input[name="exportType"]'
  );
  const viewReportBtn = document.getElementById("viewReportBtn");
  const exportBtn = document.getElementById("exportBtn");
  const printBtn = document.getElementById("printBtn");
  const reportTable = document.getElementById("reportTable");
  const fromDateInput = document.getElementById("fromDate");
  const toDateInput = document.getElementById("toDate");

  // Format money
  function formatMoney(amount) {
    return new Intl.NumberFormat("vi-VN").format(amount);
  }

  // Render report table
  function renderReportTable(data) {
    const total = data.reduce(
      (acc, item) => ({
        revenue: acc.revenue + item.revenue,
        expense: acc.expense + item.expense,
      }),
      { revenue: 0, expense: 0 }
    );

    const html =
      data
        .map(
          (item) => `
            <div class="grid grid-cols-3 border-b border-gray-300 hover:bg-gray-50">
                <div class="px-4 py-3 border-r border-gray-300">${
                  item.date
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
                  total.revenue
                )}</div>
                <div class="px-4 py-3 text-red-600">${formatMoney(
                  total.expense
                )}</div>
            </div>
        `;

    reportTable.innerHTML = html;
  }

  // Event listeners
  viewReportBtn.addEventListener("click", function () {
    const selectedType = document.querySelector(
      'input[name="reportType"]:checked'
    ).value;
    const data = reportData[selectedType] || reportData["all"];
    renderReportTable(data);
    console.log("Viewing report for:", selectedType);
  });

  exportBtn.addEventListener("click", function () {
    const selectedType = document.querySelector(
      'input[name="exportType"]:checked'
    ).value;
    console.log("Exporting as:", selectedType);
    alert(`Xuất file định dạng ${selectedType.toUpperCase()} (Demo)`);
  });

  // Format lại số tiền cho các phần tử trong bảng báo cáo (nếu cần)
  document
    .querySelectorAll(".text-green-600.font-medium, .text-red-600.font-medium")
    .forEach(function (el) {
      let value = el.textContent.replace(/\D/g, "");
      if (value) {
        el.textContent = Number(value).toLocaleString("vi-VN") + " đ";
      }
    });

  // Đảm bảo ngày bắt đầu không lớn hơn ngày kết thúc
  if (fromDateInput && toDateInput) {
    fromDateInput.addEventListener("change", function () {
      toDateInput.min = this.value;
    });
  }

  // Initialize with default data
  renderReportTable(reportData["all"]);

  console.log("Report page loaded successfully");
});
