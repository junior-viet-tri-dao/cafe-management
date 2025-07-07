//datban
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

function showDatBanModal(data) {
    const form = document.querySelector("#datBanModal form");
    if (form) form.reset();
    document.getElementById("maBanInput").value = data.maBan;
    document.getElementById("maHoaDonInput").value = data.maHoaDon;
    document.getElementById("maNhanVienInput").value = data.maNhanVien;

    $("#datBanModal").modal("show");
}

//xemban

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

//huyban

function huyBan() {
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
        alert("Bạn Chỉ Có Thể Hủy Một Bàn Tại Một Thời Điểm.");
        return;
    }

    fetch(`/huyban/${selected[0]}`, {
        method: "DELETE",
    })
        .then((response) => response.json())
        .then((data) => {
            alert(data.message);
            if (data.success) {
                location.reload();
            }
        })
        .catch((error) => {
            alert("Có lỗi xảy ra khi hủy bàn.");
            console.log(error);
        });
}

//chuyenban
function chuyenBan() {
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
        alert("Bạn Chỉ Có Thể Chuyển Một Bàn Tại Một Thời Điểm.");
        return;
    }
    const maBanCu = selected[0];
    fetch(`/danhsachbanranh?maBanCu=${maBanCu}`)
        .then((res) => res.json())
        .then((data) => {
            showChuyenBanModal(maBanCu, data);
        })
        .catch((err) => {
            alert("Không lấy được danh sách bàn rảnh.");
        });
}

function showChuyenBanModal(maBanCu, data) {
    // data là mảng các bàn rảnh [{maBan, tenBan}]
    let selectHtml = '<select id="maBanMoiSelect" class="form-select">';
    // data.forEach((ban) => {
    //     selectHtml += `<option value="${ban.maBan}">${
    //         ban.tenBan || "Bàn " + ban.maBan
    //     }</option>`;
    // });
    (data.data || []).forEach((ban) => {
        selectHtml += `<option value="${ban.maBan}">${
            ban.tenBan || "Bàn " + ban.maBan
        }</option>`;
    });

    selectHtml += "</select>";
    document.getElementById("maBanCuInput").value = maBanCu;
    document.getElementById("chuyenBanDanhSach").innerHTML = selectHtml;
    $("#chuyenBanModal").modal("show");
}

function submitChuyenBan() {
    const maBanCu = document.getElementById("maBanCuInput").value;
    const maBanMoi = document.getElementById("maBanMoiSelect").value;
    fetch("/chuyenban", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `maBanCu=${maBanCu}&maBanMoi=${maBanMoi}`,
    })
        .then((res) => res.json())
        .then((data) => {
            alert(data.message);
            if (data.success) {
                $("#chuyenBanModal").modal("hide");
                location.reload();
            }
        });
}
