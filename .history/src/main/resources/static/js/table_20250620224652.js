function datBan() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectTable"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một bàn.");
        return;
    }
    if (selected.length > 1) {
        alert("Bạn Chỉ Có Thể Đặt Một Bàn Tại Một Thời Điểm.");
        return;
    }
    // Gửi AJAX request
    fetch(`/datban/${selected[0]}`)
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showDatBanModal(data);
            } else {
                alert(data.message);
            }
        })
        .catch((error) => {
            console.log(error);
        });
}

function xemBan() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectTable"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một bàn.");
        return;
    }
    if (selected.length > 1) {
        alert("Bạn Chỉ Có Xem Một Bàn Tại Một Thời Điểm.");
        return;
    }
    fetch(`/xemban/${selected[0]}`)
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showXemBanModal(data);
            } else {
                alert(data.message || "Không tìm thấy thông tin đặt bàn.");
            }
        })
        .catch((error) => {
            console.log(error);
        });
}

//open modal
function showDatBanModal(data) {
    const form = document.querySelector("#datBanModal form");
    if (form) form.reset();
    document.getElementById("maBanInput").value = data.maBan;
    document.getElementById("maHoaDonInput").value = data.maHoaDon;
    document.getElementById("maNhanVienInput").value = data.maNhanVien;

    $("#datBanModal").modal("show");
}

function showXemBanModal(data) {
    document.getElementById("xemBan_tenKhachHang").textContent =
        data.tenKhachHang || "";
    document.getElementById("xemBan_sdtKhachHang").textContent =
        data.sdtKhachHang || "";
    document.getElementById("xemBan_ngayGioDat").textContent =
        data.ngayGioDat || "";
    document.getElementById("xemBan_maHoaDon").textContent =
        data.maHoaDon || "";
    document.getElementById("xemBan_maNhanVien").textContent =
        data.maNhanVien || "";

    $("#xemBanModal").modal("show");
}
