document.addEventListener("DOMContentLoaded", function () {
  let ingredients = [];

  // Lấy các elements
  const ingredientsList = document.getElementById("ingredientsList");
  const addIngredientBtn = document.getElementById("addIngredientBtn");
  const menuForm = document.getElementById("menuForm");
  const cancelBtn = document.getElementById("cancelBtn");

  // Thêm thành phần
  addIngredientBtn.addEventListener("click", function () {
    const name = document.getElementById("newIngredientName").value.trim();
    const amount = document.getElementById("newIngredientAmount").value.trim();
    const unit = document.getElementById("newIngredientUnit").value;

    if (name && amount && unit) {
      // Kiểm tra trùng lặp
      const existingIngredient = ingredients.find(
        (ing) => ing.name.toLowerCase() === name.toLowerCase()
      );
      if (existingIngredient) {
        alert("Thành phần này đã được thêm!");
        return;
      }

      const ingredient = {
        name: name,
        amount: parseFloat(amount),
        unit: unit,
      };

      ingredients.push(ingredient);
      renderIngredients();
      clearIngredientInputs();
    } else {
      alert("Vui lòng điền đầy đủ thông tin thành phần!");
    }
  });

  // Render danh sách thành phần
  function renderIngredients() {
    if (ingredients.length === 0) {
      ingredientsList.innerHTML = `
                <div class="grid grid-cols-3 py-8 text-center text-gray-500">
                    <div class="col-span-3">Chưa có thành phần nào</div>
                </div>
            `;
      return;
    }

    const html = ingredients
      .map(
        (ingredient, index) => `
            <div class="grid grid-cols-3 border-b border-gray-300 last:border-b-0">
                <div class="px-3 py-2 border-r border-gray-300">${ingredient.name}</div>
                <div class="px-3 py-2 border-r border-gray-300">${ingredient.amount}</div>
                <div class="px-3 py-2 flex justify-between items-center">
                    <span>${ingredient.unit}</span>
                    <button 
                        type="button" 
                        onclick="removeIngredient(${index})"
                        class="text-red-500 hover:text-red-700 ml-2"
                        title="Xóa thành phần"
                    >
                        ✕
                    </button>
                </div>
            </div>
        `
      )
      .join("");

    ingredientsList.innerHTML = html;
  }

  // Xóa thành phần
  window.removeIngredient = function (index) {
    if (confirm("Bạn có chắc muốn xóa thành phần này?")) {
      ingredients.splice(index, 1);
      renderIngredients();
    }
  };

  // Xóa input thành phần
  function clearIngredientInputs() {
    document.getElementById("newIngredientName").value = "";
    document.getElementById("newIngredientAmount").value = "";
    document.getElementById("newIngredientUnit").value = "";
  }

  // Submit form
  menuForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const tenMon = document.getElementById("tenMon").value.trim();
    const giaTien = document.getElementById("giaTien").value.trim();

    if (!tenMon || !giaTien) {
      alert("Vui lòng điền đầy đủ tên món và giá tiền!");
      return;
    }

    if (ingredients.length === 0) {
      if (!confirm("Bạn chưa thêm thành phần nào. Bạn có muốn tiếp tục?")) {
        return;
      }
    }

    const menuData = {
      tenMon: tenMon,
      giaTien: parseFloat(giaTien),
      thanhPhan: ingredients,
    };

    // Gửi dữ liệu lên server
    saveMenu(menuData);
  });

  // Lưu món ăn
  function saveMenu(menuData) {
    console.log("Dữ liệu món ăn:", menuData);

    // TODO: Gửi request đến server
    fetch("/api/menus", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(menuData),
    })
      .then((response) => {
        if (response.ok) {
          alert("Thêm món thành công!");
          // Reset form
          resetForm();
          // Chuyển hướng về danh sách menu
          window.location.href = "/menus";
        } else {
          throw new Error("Lỗi khi thêm món");
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("Có lỗi xảy ra khi thêm món!");
      });
  }

  // Reset form
  function resetForm() {
    menuForm.reset();
    ingredients = [];
    renderIngredients();
  }

  // Hủy
  cancelBtn.addEventListener("click", function () {
    if (confirm("Bạn có chắc muốn hủy? Dữ liệu sẽ không được lưu.")) {
      window.location.href = "/menus";
    }
  });

  // Khởi tạo
  renderIngredients();

  // Enter để thêm thành phần
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
