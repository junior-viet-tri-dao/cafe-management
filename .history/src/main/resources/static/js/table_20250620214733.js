function datBan() {
    let selected = [];
    let checkboxes = document.querySelectorAll('input[name="datBan"]:checked');
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

// Hàm mở modal và truyền dữ liệu vào form
function showDatBanModal(data) {
    const form = document.querySelector("#datBanModal form");
    if (form) form.reset();
    document.getElementById("maBanInput").value = data.maBan;
    document.getElementById("maHoaDonInput").value = data.maHoaDon;
    document.getElementById("maNhanVienInput").value = data.maNhanVien;

    $("#datBanModal").modal("show");
}
