document.addEventListener("DOMContentLoaded", function () {
  let ingredients = [];

  const ingredientsList = document.getElementById("ingredientsList");
  const addIngredientBtn = document.getElementById("addIngredientBtn");
  const menuForm = document.getElementById("menuForm");
  const cancelBtn = document.getElementById("cancelBtn");
  const messageBox = document.getElementById("messageBox");

  addIngredientBtn.addEventListener("click", function () {
    const productId = document.getElementById("newIngredientName").value;
    const productName =
      document.getElementById("newIngredientName").selectedOptions[0].text;
    const quantity = document
      .getElementById("newIngredientAmount")
      .value.trim();
    const unitName = document.getElementById("newIngredientUnit").value.trim();

    if (productId && quantity && unitName) {
      // Kiểm tra trùng lặp
      const existingIngredient = ingredients.find(
        (ing) => ing.productId === productId
      );
      if (existingIngredient) {
        showMessage("Thành phần này đã được thêm!", "error");
        return;
      }

      const ingredient = {
        productId: parseInt(productId),
        productName: productName,
        quantity: parseFloat(quantity),
        unitName: unitName,
      };

      ingredients.push(ingredient);
      renderIngredients();
      clearIngredientInputs();
    } else {
      showMessage("Vui lòng điền đầy đủ thông tin thành phần!", "error");
    }
  });

  function renderIngredients() {
    ingredientsList.innerHTML = "";
    if (ingredients.length === 0) {
      ingredientsList.innerHTML = `
        <div class="grid grid-cols-3 py-8 text-center text-gray-500">
          <div class="col-span-3">Chưa có thành phần nào</div>
        </div>
      `;
      return;
    }

    ingredients.forEach((ingredient, idx) => {
      const row = document.createElement("div");
      row.className =
        "grid grid-cols-3 border-b border-gray-300 last:border-b-0";

      row.innerHTML = `
        <div class="px-3 py-2 border-r border-gray-300 text-center">${ingredient.productName}
          <input type="hidden" name="menuDetails[${idx}].productId" value="${ingredient.productId}" />
        </div>
        <div class="px-3 py-2 border-r border-gray-300 text-center">${ingredient.quantity}
          <input type="hidden" name="menuDetails[${idx}].quantity" value="${ingredient.quantity}" />
        </div>
        <div class="px-3 py-2 flex justify-center items-center text-center">
          <span>${ingredient.unitName}</span>
          <input type="hidden" name="menuDetails[${idx}].unitName" value="${ingredient.unitName}" />
          <button 
            type="button" 
            onclick="removeIngredient(${idx})"
            class="text-red-500 hover:text-red-700 ml-2"
            title="Xóa thành phần"
          >
            ✕
          </button>
        </div>
      `;
      ingredientsList.appendChild(row);
    });
  }

  function clearIngredientInputs() {
    document.getElementById("newIngredientName").value = "";
    document.getElementById("newIngredientAmount").value = "";
    document.getElementById("newIngredientUnit").value = "";
  }

  function showMessage(msg, type) {
    if (!messageBox) return;
    messageBox.innerHTML = `
      <div class="mb-4 px-4 py-3 rounded-lg ${
        type === "success"
          ? "bg-green-50 border border-green-200 text-green-800"
          : "bg-red-50 border border-red-200 text-red-800"
      } shadow flex items-center gap-2" role="alert">
        <span>${msg}</span>
      </div>
    `;
    setTimeout(() => {
      messageBox.innerHTML = "";
    }, 3000);
  }

  window.removeIngredient = function (index) {
    ingredients.splice(index, 1);
    renderIngredients();
  };

  menuForm.addEventListener("submit", function (e) {
    // Kiểm tra dữ liệu trước khi submit
    const itemName = document.getElementById("tenMon").value.trim();
    const currentPrice = document.getElementById("giaTien").value.trim();

    if (!itemName || !currentPrice) {
      showMessage("Vui lòng điền đầy đủ tên món và giá tiền!", "error");
      e.preventDefault();
      return;
    }

    if (ingredients.length === 0) {
      showMessage("Vui lòng thêm ít nhất 1 thành phần!", "error");
      e.preventDefault();
      return;
    }
    // Không cần fetch/ajax, form sẽ submit bình thường
  });

  cancelBtn.addEventListener("click", function () {
    if (confirm("Bạn có chắc muốn hủy? Dữ liệu sẽ không được lưu.")) {
      window.location.href = "/menu";
    }
  });

  renderIngredients();

  ["newIngredientName", "newIngredientAmount", "newIngredientUnit"].forEach(
    (id) => {
      document.getElementById(id).addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
          e.preventDefault();
          addIngredientBtn.click();
        }
      });
    }
  );
});
