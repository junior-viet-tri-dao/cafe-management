document.addEventListener("DOMContentLoaded", function () {
  const menuForm = document.getElementById("menuForm");
  const addBtn = document.getElementById("addIngredientBtn");
  const menuDetailsDiv = document.getElementById("menuDetails");

  addBtn.addEventListener("click", function () {
    const select = document.getElementById("newIngredientName");
    const amount = document.getElementById("newIngredientAmount");
    const unit = document.getElementById("newIngredientUnit");

    const productId = select.value;
    const productName = select.options[select.selectedIndex].text;
    const quantity = amount.value;
    const unitName = unit.value;

    // Validate
    if (!productId || !quantity || !unitName) {
      alert("Vui lòng chọn thành phần, nhập số lượng và đơn vị tính!");
      return;
    }

    // Tạo dòng mới
    const row = document.createElement("div");
    row.className = "grid grid-cols-4 border-b border-gray-300";

    row.innerHTML = `
      <div class="px-3 py-2 border-r border-gray-300">
        <input type="hidden" name="" value="${productId}" />
        <input type="text" name="" value="${productName}" class="w-full bg-gray-100" readonly />
      </div>
      <div class="px-3 py-2 border-r border-gray-300">
        <input type="number" name="" value="${quantity}" class="w-full" min="0" step="0.1" required />
      </div>
      <div class="px-3 py-2 border-r border-gray-300">
        <input type="text" name="" value="${unitName}" class="w-full" required />
      </div>
      <div class="px-3 py-2 flex items-center justify-center">
        <button type="button" class="delete-ingredient-btn text-red-500 text-lg font-bold" title="Xóa thành phần">×</button>
      </div>
    `;

    // Xóa dòng khi bấm nút xóa
    row.querySelector(".delete-ingredient-btn").onclick = function () {
      row.remove();
      updateIngredientNames();
    };

    menuDetailsDiv.appendChild(row);

    // Reset input
    select.value = "";
    amount.value = "";
    unit.value = "";

    updateIngredientNames();
  });

  // Cập nhật lại name cho các input theo index
  function updateIngredientNames() {
    const rows = menuDetailsDiv.querySelectorAll(".grid");
    rows.forEach((row, idx) => {
      const inputs = row.querySelectorAll("input");
      if (inputs.length === 4) {
        inputs[0].name = `menuDetails[${idx}].productId`;
        inputs[1].name = `menuDetails[${idx}].productName`;
        inputs[2].name = `menuDetails[${idx}].quantity`;
        inputs[3].name = `menuDetails[${idx}].unitName`;
      }
    });
  }

  // Khi submit form, log ra dữ liệu như trước
  menuForm.addEventListener("submit", function (e) {
    // e.preventDefault();

    const itemName = document.getElementById("itemName").value;
    const currentPrice = document.getElementById("currentPrice").value;

    const ingredientRows = document.querySelectorAll("#menuDetails > .grid");
    const menuDetails = [];
    ingredientRows.forEach((row) => {
      const productId = row.querySelector('input[name$=".productId"]')?.value;
      const productName = row.querySelector(
        'input[name$=".productName"]'
      )?.value;
      const quantity = row.querySelector('input[name$=".quantity"]')?.value;
      const unitName = row.querySelector('input[name$=".unitName"]')?.value;
      if (productId && productName && quantity && unitName) {
        menuDetails.push({ productId, productName, quantity, unitName });
      }
    });

    // console.log({
    //   itemName,
    //   currentPrice,
    //   menuDetails,
    // });
  });
});
