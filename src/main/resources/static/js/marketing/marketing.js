document.addEventListener("DOMContentLoaded", function () {
  // Lấy các elements
  const addPromotionBtn = document.getElementById("addPromotionBtn");

  // Thêm khuyến mãi mới
  addPromotionBtn.addEventListener("click", function () {
    console.log("Chuyển đến trang thêm khuyến mãi");
    window.location.href = "/marketing/create";
  });

  // Xử lý sự kiện chỉnh sửa
  document.addEventListener("click", function (e) {
    if (e.target.closest(".edit-btn")) {
      const btn = e.target.closest(".edit-btn");
      const promotionId = btn.getAttribute("data-id");
      editPromotion(promotionId);
    }
  });

  // Xử lý sự kiện xóa
  document.addEventListener("click", function (e) {
    if (e.target.closest(".delete-btn")) {
      const btn = e.target.closest(".delete-btn");
      const promotionId = btn.getAttribute("data-id");
      const promotionName = btn.getAttribute("data-name");
      deletePromotion(promotionId, promotionName);
    }
  });

  // Chỉnh sửa khuyến mãi
  function editPromotion(id) {
    console.log("Chỉnh sửa khuyến mãi ID:", id);
    window.location.href = `/marketing/edit/${id}`;
  }

  // Xóa khuyến mãi
  function deletePromotion(id, name) {
    if (confirm(`Bạn có chắc chắn muốn xóa khuyến mãi "${name}"?`)) {
      console.log("Xóa khuyến mãi ID:", id);

      fetch(`/marketing/delete/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (response.ok) {
            alert("Xóa khuyến mãi thành công!");
            window.location.reload();
          } else {
            alert("Có lỗi xảy ra khi xóa khuyến mãi!");
          }
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("Có lỗi xảy ra khi xóa khuyến mãi!");
        });
    }
  }

  console.log("Marketing management loaded successfully");
});
