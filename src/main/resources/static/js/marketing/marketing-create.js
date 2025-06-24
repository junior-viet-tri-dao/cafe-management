document.addEventListener("DOMContentLoaded", function () {
  // Lấy các elements
  const promotionForm = document.getElementById("promotionForm");
  const cancelBtn = document.getElementById("cancelBtn");
  const loadingState = document.getElementById("loadingState");

  // Set ngày mặc định
  const today = new Date();
  const todayString = today.toISOString().split("T")[0];

  // Set ngày bắt đầu là hôm nay
  document.getElementById("ngayBatDau").value = todayString;

  // Set ngày kết thúc là 1 tuần sau
  const nextWeek = new Date(today);
  nextWeek.setDate(today.getDate() + 7);
  const nextWeekString = nextWeek.toISOString().split("T")[0];
  document.getElementById("ngayKetThuc").value = nextWeekString;

  // Validation ngày
  function validateDates() {
    const startDate = new Date(document.getElementById("ngayBatDau").value);
    const endDate = new Date(document.getElementById("ngayKetThuc").value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // Kiểm tra ngày bắt đầu không được nhỏ hơn hôm nay
    if (startDate < today) {
      alert("Ngày bắt đầu không được nhỏ hơn ngày hôm nay!");
      return false;
    }

    // Kiểm tra ngày kết thúc phải lớn hơn ngày bắt đầu
    if (endDate <= startDate) {
      alert("Ngày kết thúc phải lớn hơn ngày bắt đầu!");
      return false;
    }

    return true;
  }

  // Event listener cho ngày bắt đầu
  document.getElementById("ngayBatDau").addEventListener("change", function () {
    const startDate = new Date(this.value);
    const endDateInput = document.getElementById("ngayKetThuc");
    const endDate = new Date(endDateInput.value);

    // Nếu ngày kết thúc nhỏ hơn hoặc bằng ngày bắt đầu, tự động set ngày kết thúc
    if (endDate <= startDate) {
      const newEndDate = new Date(startDate);
      newEndDate.setDate(startDate.getDate() + 1);
      endDateInput.value = newEndDate.toISOString().split("T")[0];
    }
  });

  // Validation % giảm giá
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

    // Validate form
    const tenKhuyenMai = document.getElementById("tenKhuyenMai").value.trim();
    const ngayBatDau = document.getElementById("ngayBatDau").value;
    const ngayKetThuc = document.getElementById("ngayKetThuc").value;
    const phanTramGiam = document.getElementById("phanTramGiam").value;

    if (!tenKhuyenMai) {
      alert("Vui lòng nhập tên khuyến mãi!");
      document.getElementById("tenKhuyenMai").focus();
      return;
    }

    if (!ngayBatDau || !ngayKetThuc) {
      alert("Vui lòng chọn ngày bắt đầu và ngày kết thúc!");
      return;
    }

    if (!phanTramGiam || phanTramGiam <= 0) {
      alert("Vui lòng nhập phần trăm giảm giá hợp lệ!");
      document.getElementById("phanTramGiam").focus();
      return;
    }

    if (!validateDates()) {
      return;
    }

    // Tạo object dữ liệu
    const promotionData = {
      tenKhuyenMai: tenKhuyenMai,
      ngayBatDau: formatDateForDisplay(ngayBatDau),
      ngayKetThuc: formatDateForDisplay(ngayKetThuc),
      phanTramGiam: parseInt(phanTramGiam),
    };

    // Gửi dữ liệu
    createPromotion(promotionData);
  });

  // Tạo khuyến mãi
  function createPromotion(promotionData) {
    console.log("Dữ liệu khuyến mãi:", promotionData);

    // Hiển thị loading
    showLoading(true);

    // Simulate API call
    setTimeout(() => {
      // TODO: Gọi API thực tế
      /*
            fetch('/api/promotions', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(promotionData)
            })
            .then(response => {
                if (response.ok) {
                    alert('Tạo khuyến mãi thành công!');
                    window.location.href = '/marketing';
                } else {
                    throw new Error('Lỗi khi tạo khuyến mãi');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Có lỗi xảy ra khi tạo khuyến mãi!');
            })
            .finally(() => {
                showLoading(false);
            });
            */

      // Mock success response
      showLoading(false);
      alert("Tạo khuyến mãi thành công! (Demo)");

      // Uncomment để redirect về trang danh sách
      // window.location.href = '/marketing';

      // Reset form để demo
      resetForm();
    }, 1500);
  }

  // Hiển thị/ẩn loading
  function showLoading(show) {
    if (show) {
      promotionForm.style.display = "none";
      loadingState.style.display = "block";
    } else {
      promotionForm.style.display = "block";
      loadingState.style.display = "none";
    }
  }

  // Format ngày để hiển thị (dd/mm/yyyy)
  function formatDateForDisplay(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  // Reset form
  function resetForm() {
    promotionForm.reset();

    // Set lại ngày mặc định
    const today = new Date();
    const todayString = today.toISOString().split("T")[0];
    document.getElementById("ngayBatDau").value = todayString;

    const nextWeek = new Date(today);
    nextWeek.setDate(today.getDate() + 7);
    const nextWeekString = nextWeek.toISOString().split("T")[0];
    document.getElementById("ngayKetThuc").value = nextWeekString;
  }

  // Hủy
  cancelBtn.addEventListener("click", function () {
    if (confirm("Bạn có chắc muốn hủy? Dữ liệu sẽ không được lưu.")) {
      // window.location.href = '/marketing';
      console.log("Đã hủy tạo khuyến mãi");
      resetForm();
    }
  });

  console.log("Marketing create form loaded successfully");
});
