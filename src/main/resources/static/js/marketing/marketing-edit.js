document.addEventListener("DOMContentLoaded", function () {
  let promotionId = null;

  // Lấy các elements
  const promotionForm = document.getElementById("promotionForm");
  const cancelBtn = document.getElementById("cancelBtn");
  const loadingIndicator = document.getElementById("loadingIndicator");
  const errorMessage = document.getElementById("errorMessage");
  const loadingState = document.getElementById("loadingState");
  const retryBtn = document.getElementById("retryBtn");

  // Dữ liệu mẫu để test
  const mockPromotions = {
    1: {
      id: "1",
      tenKhuyenMai: "Khai trương quán",
      ngayBatDau: "2015-01-21",
      ngayKetThuc: "2015-01-21",
      phanTramGiam: 50,
    },
    2: {
      id: "2",
      tenKhuyenMai: "Khuyến mãi sinh nhật",
      ngayBatDau: "2024-12-01",
      ngayKetThuc: "2024-12-31",
      phanTramGiam: 20,
    },
    3: {
      id: "3",
      tenKhuyenMai: "Giảm giá cuối tuần",
      ngayBatDau: "2024-06-15",
      ngayKetThuc: "2024-06-30",
      phanTramGiam: 15,
    },
  };

  // Lấy promotion ID từ URL
  function getPromotionIdFromUrl() {
    const urlParts = window.location.pathname.split("/");
    return urlParts[urlParts.length - 1];
  }

  // Tải thông tin khuyến mãi
  function loadPromotionData() {
    promotionId = getPromotionIdFromUrl();

    console.log("Promotion ID:", promotionId);

    if (!promotionId || promotionId === "edit") {
      showError();
      return;
    }

    showLoading();

    // Simulate API call với mock data
    setTimeout(() => {
      const promotionData = mockPromotions[promotionId];

      if (promotionData) {
        populateForm(promotionData);
        hideLoading();
        showForm();
      } else {
        console.error("Khuyến mãi không tồn tại");
        hideLoading();
        showError();
      }
    }, 1000);
  }

  // Điền dữ liệu vào form
  function populateForm(promotionData) {
    document.getElementById("promotionId").value = promotionData.id;
    document.getElementById("tenKhuyenMai").value =
      promotionData.tenKhuyenMai || "";
    document.getElementById("ngayBatDau").value =
      promotionData.ngayBatDau || "";
    document.getElementById("ngayKetThuc").value =
      promotionData.ngayKetThuc || "";
    document.getElementById("phanTramGiam").value =
      promotionData.phanTramGiam || "";
  }

  // Hiển thị/ẩn các phần tử
  function showLoading() {
    loadingIndicator.style.display = "block";
    promotionForm.style.display = "none";
    errorMessage.style.display = "none";
    loadingState.style.display = "none";
  }

  function hideLoading() {
    loadingIndicator.style.display = "none";
  }

  function showForm() {
    promotionForm.style.display = "block";
    errorMessage.style.display = "none";
    loadingState.style.display = "none";
  }

  function showError() {
    loadingIndicator.style.display = "none";
    promotionForm.style.display = "none";
    errorMessage.style.display = "block";
    loadingState.style.display = "none";
  }

  function showSavingLoading(show) {
    if (show) {
      promotionForm.style.display = "none";
      loadingState.style.display = "block";
    } else {
      promotionForm.style.display = "block";
      loadingState.style.display = "none";
    }
  }

  // Validation ngày
  function validateDates() {
    const startDate = new Date(document.getElementById("ngayBatDau").value);
    const endDate = new Date(document.getElementById("ngayKetThuc").value);

    if (endDate <= startDate) {
      alert("Ngày kết thúc phải lớn hơn ngày bắt đầu!");
      return false;
    }

    return true;
  }

  // Event listeners
  document.getElementById("ngayBatDau").addEventListener("change", function () {
    const startDate = new Date(this.value);
    const endDateInput = document.getElementById("ngayKetThuc");
    const endDate = new Date(endDateInput.value);

    if (endDate <= startDate) {
      const newEndDate = new Date(startDate);
      newEndDate.setDate(startDate.getDate() + 1);
      endDateInput.value = newEndDate.toISOString().split("T")[0];
    }
  });

  document
    .getElementById("phanTramGiam")
    .addEventListener("input", function () {
      const value = parseInt(this.value);
      if (value < 0) {
        this.value = 0;
      } else if (value > 100) {
        this.value = 100;
      }
    });

  // Submit form
  promotionForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const tenKhuyenMai = document.getElementById("tenKhuyenMai").value.trim();
    const ngayBatDau = document.getElementById("ngayBatDau").value;
    const ngayKetThuc = document.getElementById("ngayKetThuc").value;
    const phanTramGiam = document.getElementById("phanTramGiam").value;
    const id = document.getElementById("promotionId").value;

    if (!tenKhuyenMai) {
      alert("Vui lòng nhập tên khuyến mãi!");
      return;
    }

    if (!phanTramGiam || phanTramGiam <= 0) {
      alert("Vui lòng nhập phần trăm giảm giá hợp lệ!");
      return;
    }

    if (!validateDates()) {
      return;
    }

    const promotionData = {
      id: id,
      tenKhuyenMai: tenKhuyenMai,
      ngayBatDau: ngayBatDau,
      ngayKetThuc: ngayKetThuc,
      phanTramGiam: parseInt(phanTramGiam),
    };

    updatePromotion(promotionData);
  });

  // Cập nhật khuyến mãi
  function updatePromotion(promotionData) {
    console.log("Cập nhật khuyến mãi:", promotionData);

    showSavingLoading(true);

    setTimeout(() => {
      showSavingLoading(false);
      alert("Cập nhật khuyến mãi thành công! (Demo)");
    }, 1500);
  }

  // Function để load test data
  window.loadTestData = function (id) {
    const data = mockPromotions[id.toString()];
    if (data) {
      populateForm(data);
      showForm();
    }
  };

  // Hủy
  cancelBtn.addEventListener("click", function () {
    if (confirm("Bạn có chắc muốn hủy? Những thay đổi sẽ không được lưu.")) {
      console.log("Đã hủy chỉnh sửa");
    }
  });

  // Thử lại
  retryBtn.addEventListener("click", function () {
    loadPromotionData();
  });

  // Khởi tạo với dữ liệu mẫu
  showForm();
  console.log("Marketing edit form loaded");
});
