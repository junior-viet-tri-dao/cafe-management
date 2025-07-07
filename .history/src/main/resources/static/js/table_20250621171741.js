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
    // fetch(`/danhsachbanranh?maBanCu=${maBanCu}`)
    //     .then((res) => res.json())
    //     .then((data) => {
    //         showChuyenBanModal(maBanCu, data);
    //     }
    // )
    //     .catch((err) => {
    //         alert("Không lấy được danh sách bàn rảnh.");
    //     });
    fetch(`/danhsachbanranh?maBanCu=${maBanCu}`)
        .then((res) => res.json())
        .then((data) => {
            if (data.success) {
                showChuyenBanModal(maBanCu, data);
            } else {
                alert(data.message || "Có lỗi xảy ra!");
            }
        })
        .catch((err) => {
            alert("Không lấy được danh sách bàn rảnh.");
        });
}

function showChuyenBanModal(maBanCu, data) {
    // data la mang [{maBan, tenBan}]
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

// chọn thực đơn
function chonThucDon() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectTable"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một bàn để gọi món.");
        return;
    }
    if (selected.length > 1) {
        alert("Bạn Chỉ Có Thể Chọn Món Cho Một Bàn Tại Một Thời Điểm.");
        return;
    }
    const maBan = selected[0];
    fetch(`/thucdon/${maBan}`)
        .then((response) => response.json())
        .then((data) => {
            console.log("DATA:", data);
            if (data.success) {
                showChonThucDonModal(data);
            } else {
                alert(data.message || "Không tìm thấy thực đơn cho bàn này.");
            }
        })
        .catch((error) => {
            console.log(error);
        });
}

function showChonThucDonModal(data) {
    const maHoaDon = data.maHoaDon;
    const thucDonList = data.thucDonList;
    // document.getElementById("ctd_maHoaDon").textContent = maHoaDon;
    document.getElementById("ctd_maHoaDonInput").value = maHoaDon;
    let tbody = "";
    thucDonList.forEach((item, idx) => {
        tbody += `<tr>
            <td>${item.maThucDon}</td>
            <td>${item.tenMon}</td>
            <td>${item.giaTienHienTai}</td>
            <td><input type="number" min="0" value="0" class="form-control ctd_soluong" data-idx="${idx}" data-giatien="${item.giaTienHienTai}" data-mathucdon="${item.maThucDon}" onchange="updateThanhTienCTD(${idx})"></td>
            <td><span id="ctd_thanhtien_${idx}">0</span></td>
        </tr>`;
    });
    document.getElementById("ctd_tableBody").innerHTML = tbody;
    document.getElementById("ctd_tongTien").textContent = "0";
    $("#chonThucDonModal").modal("show");
}

function updateThanhTienCTD(idx) {
    const input = document.querySelector(`input[data-idx="${idx}"]`);
    const soLuong = parseInt(input.value) || 0;
    const gia = parseFloat(input.getAttribute("data-giatien"));
    const thanhTien = soLuong * gia;
    document.getElementById(`ctd_thanhtien_${idx}`).textContent = thanhTien;
    // update tổng tiền
    let tong = 0;
    document.querySelectorAll(".ctd_soluong").forEach((inp) => {
        const sl = parseInt(inp.value) || 0;
        const g = parseFloat(inp.getAttribute("data-giatien"));
        tong += sl * g;
    });
    document.getElementById("ctd_tongTien").textContent = tong;
}

function submitChonThucDon(event) {
    event.preventDefault();
    const maHoaDon = document.getElementById("ctd_maHoaDonInput").value;
    const maThucDon = [];
    const soLuong = [];
    document.querySelectorAll(".ctd_soluong").forEach((inp) => {
        const sl = parseInt(inp.value) || 0;
        if (sl > 0) {
            maThucDon.push(inp.getAttribute("data-mathucdon"));
            soLuong.push(sl);
        }
    });
    if (maThucDon.length === 0) {
        alert("Vui lòng chọn ít nhất một món!");
        return;
    }
    fetch("/thucdon/goi", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `maHoaDon=${maHoaDon}&maThucDon=${maThucDon.join(
            ","
        )}&soLuong=${soLuong.join(",")}`,
    })
        .then((res) => res.json())
        .then((data) => {
            alert(data.message);
            if (data.success) {
                $("#chonThucDonModal").modal("hide");
                location.reload();
            }
        });
}
