document.addEventListener("DOMContentLoaded", function () {
  console.log("Employee list loaded");

  // Format all salary amounts in the table
  function formatAllSalaries() {
    const salaryElements = document.querySelectorAll(".salary-amount");

    salaryElements.forEach((element) => {
      const originalValue = element.textContent.trim();

      // Skip if already formatted or empty
      if (originalValue.includes("₫") || !originalValue) {
        return;
      }

      const formattedValue = formatCurrency(originalValue);
      element.textContent = formattedValue;
    });
  }

  // Format salaries when page loads
  formatAllSalaries();

  // Observer to format salaries when content changes dynamically
  const observer = new MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
      if (mutation.type === "childList") {
        const addedNodes = Array.from(mutation.addedNodes);
        addedNodes.forEach((node) => {
          if (node.nodeType === Node.ELEMENT_NODE) {
            const salaryElements = node.querySelectorAll
              ? node.querySelectorAll(".salary-amount")
              : [];
            salaryElements.forEach((element) => {
              const originalValue = element.textContent.trim();
              if (!originalValue.includes("₫") && originalValue) {
                element.textContent = formatCurrency(originalValue);
              }
            });
          }
        });
      }
    });
  });

  // Start observing the table for changes
  const tableBody = document.getElementById("employeeTableBody");
  if (tableBody) {
    observer.observe(tableBody, {
      childList: true,
      subtree: true,
    });
  }

  // Delete employee function
  window.deleteEmployee = function (employeeId) {
    if (confirm("Bạn có chắc chắn muốn xóa nhân viên này?")) {
      // Implement delete logic here
      fetch(`/employee/delete/${employeeId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (response.ok) {
            location.reload();
          } else {
            alert("Có lỗi xảy ra khi xóa nhân viên");
          }
        })
        .catch((error) => {
          alert("Có lỗi xảy ra khi xóa nhân viên");
        });
    }
  };
});
