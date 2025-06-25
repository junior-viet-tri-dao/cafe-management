document.addEventListener("DOMContentLoaded", function () {
  console.log("Marketing management loaded - using form submission");

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

  // Chỉnh sửa khuyến mãi
  function editPromotion(id) {
    console.log("Chỉnh sửa khuyến mãi ID:", id);
    window.location.href = `/marketing/edit/${id}`;
  }

  console.log("Marketing management loaded successfully");
});
