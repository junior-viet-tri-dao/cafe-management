document.addEventListener("DOMContentLoaded", function () {
  // Dữ liệu mẫu khuyến mãi
  let promotions = [
    {
      id: 1,
      tenKhuyenMai: "Khai trương quán",
      ngayBatDau: "21/1/2015",
      ngayKetThuc: "21/1/2015",
      phanTramGiam: 50,
    },
    {
      id: 2,
      tenKhuyenMai: "Khuyến mãi sinh nhật",
      ngayBatDau: "01/12/2024",
      ngayKetThuc: "31/12/2024",
      phanTramGiam: 20,
    },
    {
      id: 3,
      tenKhuyenMai: "Giảm giá cuối tuần",
      ngayBatDau: "15/06/2024",
      ngayKetThuc: "30/06/2024",
      phanTramGiam: 15,
    },
  ];

  // Lấy các elements
  const promotionsList = document.getElementById("promotionsList");
  const addPromotionBtn = document.getElementById("addPromotionBtn");
  const emptyState = document.getElementById("emptyState");

  // Render danh sách khuyến mãi
  function renderPromotions() {
    if (promotions.length === 0) {
      promotionsList.style.display = "none";
      emptyState.style.display = "block";
      return;
    }

    promotionsList.style.display = "block";
    emptyState.style.display = "none";

    const html = promotions
      .map(
        (promotion) => `
            <div class="grid grid-cols-5 border-b border-gray-300 hover:bg-gray-50">
                <div class="px-4 py-3 border-r border-gray-300">${promotion.tenKhuyenMai}</div>
                <div class="px-4 py-3 border-r border-gray-300">${promotion.ngayBatDau}</div>
                <div class="px-4 py-3 border-r border-gray-300">${promotion.ngayKetThuc}</div>
                <div class="px-4 py-3 border-r border-gray-300">${promotion.phanTramGiam}</div>
                <div class="px-4 py-3 flex justify-center space-x-2">
                    <button 
                        class="px-3 py-1 bg-blue-500 text-white rounded text-sm hover:bg-blue-600"
                        onclick="editPromotion(${promotion.id})"
                        title="Chỉnh sửa"
                    >
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                        </svg>
                    </button>
                    <button 
                        class="px-3 py-1 bg-red-500 text-white rounded text-sm hover:bg-red-600"
                        onclick="deletePromotion(${promotion.id})"
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

    promotionsList.innerHTML = html;
  }

  // Thêm khuyến mãi mới
  addPromotionBtn.addEventListener("click", function () {
    // TODO: Chuyển đến trang thêm khuyến mãi
    console.log("Chuyển đến trang thêm khuyến mãi");
    window.location.href = "/marketing/create";
    // alert("Chức năng thêm khuyến mãi sẽ được phát triển");
  });

  // Chỉnh sửa khuyến mãi
  window.editPromotion = function (id) {
    const promotion = promotions.find((p) => p.id === id);
    if (promotion) {
      console.log("Chỉnh sửa khuyến mãi:", promotion);
      // TODO: Chuyển đến trang chỉnh sửa
      // window.location.href = `/marketing/edit/${id}`;
      alert(`Chỉnh sửa khuyến mãi: ${promotion.tenKhuyenMai}`);
    }
  };

  // Xóa khuyến mãi
  window.deletePromotion = function (id) {
    const promotion = promotions.find((p) => p.id === id);
    if (
      promotion &&
      confirm(
        `Bạn có chắc chắn muốn xóa khuyến mãi "${promotion.tenKhuyenMai}"?`
      )
    ) {
      // TODO: Gọi API xóa
      console.log("Xóa khuyến mãi:", promotion);

      // Xóa khỏi mảng (demo)
      promotions = promotions.filter((p) => p.id !== id);
      renderPromotions();

      alert("Xóa khuyến mãi thành công!");
    }
  };

  // Sắp xếp theo cột
  function sortPromotions(column, direction = "asc") {
    promotions.sort((a, b) => {
      let valueA = a[column];
      let valueB = b[column];

      // Xử lý ngày tháng
      if (column === "ngayBatDau" || column === "ngayKetThuc") {
        valueA = new Date(valueA.split("/").reverse().join("-"));
        valueB = new Date(valueB.split("/").reverse().join("-"));
      }

      if (direction === "asc") {
        return valueA > valueB ? 1 : -1;
      } else {
        return valueA < valueB ? 1 : -1;
      }
    });

    renderPromotions();
    console.log(`Đã sắp xếp theo ${column} (${direction})`);
  }

  // Event listeners cho sort (có thể thêm sau)
  // TODO: Thêm functionality cho sort headers

  // Khởi tạo
  renderPromotions();

  console.log("Marketing management loaded successfully");
});
