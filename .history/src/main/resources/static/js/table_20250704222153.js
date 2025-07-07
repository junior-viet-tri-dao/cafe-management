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
    //
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

// tách bàn

function tachBan() {
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
        alert("Bạn Chỉ Có Thể Tách Một Bàn Tại Một Thời Điểm.");
        return;
    }
    fetch(`/tachban/${selected[0]}`)
        .then((res) => res.json())
        .then((data) => {
            if (!data.success) {
                alert(data.message || "Không lấy được thông tin bàn.");
                return;
            }
            showTachBanModal(data);
        })
        .catch((err) => {
            alert("Lỗi khi lấy thông tin tách bàn.");
        });
}

function showTachBanModal(data) {
    // data: {banNguon, hoaDonNguon, monList, banRanh}
    let tbody = "";
    data.monList.forEach((mon, idx) => {
        tbody += `<tr>
            <td>${mon.tenMon}</td>
            <td>${mon.soLuong}</td>
            <td>${mon.giaTaiThoiDiemBan}</td>
            <td><input type='number' min='0' max='${mon.soLuong}' value='0' class='form-control tachban_soluong' data-idx='${idx}' data-mathucdon='${mon.maThucDon}' data-gia='${mon.giaTaiThoiDiemBan}' onchange='updateTongTienTachBan()'></td>
        </tr>`;
    });
    document.getElementById("tachBan_tableBody").innerHTML = tbody;
    // Bàn đích
    let selectHtml = '<select id="tachBan_banDichSelect" class="form-select">';
    data.banRanh.forEach((ban) => {
        selectHtml += `<option value='${ban.maBan}'>${ban.tenBan}</option>`;
    });
    selectHtml += "</select>";
    document.getElementById("tachBan_banDichList").innerHTML = selectHtml;
    // Lưu lại thông tin nguồn
    document.getElementById("tachBan_maBanNguon").value = data.banNguon.maBan;
    document.getElementById("tachBan_hoaDonNguon").value = data.hoaDonNguon;
    updateTongTienTachBan();
    $("#tachBanModal").modal("show");
}

function updateTongTienTachBan() {
    let tong = 0;
    document.querySelectorAll(".tachban_soluong").forEach((inp) => {
        const sl = parseInt(inp.value) || 0;
        const g = parseFloat(inp.getAttribute("data-gia"));
        tong += sl * g;
    });
    document.getElementById("tachBan_tongTien").textContent = tong;
}

function submitTachBan(event) {
    event.preventDefault();
    const maBanNguon = document.getElementById("tachBan_maBanNguon").value;
    const hoaDonNguon = document.getElementById("tachBan_hoaDonNguon").value;
    const maBanDich = document.getElementById("tachBan_banDichSelect").value;
    let monList = [];
    document.querySelectorAll(".tachban_soluong").forEach((inp) => {
        const sl = parseInt(inp.value) || 0;
        if (sl > 0) {
            monList.push({
                maThucDon: inp.getAttribute("data-mathucdon"),
                soLuong: sl,
                giaTaiThoiDiemBan: inp.getAttribute("data-gia"),
            });
        }
    });
    if (monList.length === 0) {
        alert("Vui lòng chọn ít nhất một món để tách!");
        return;
    }
    fetch("/tachban", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `maBanNguon=${maBanNguon}&maBanDich=${maBanDich}&hoaDonNguon=${hoaDonNguon}&monListJson=${encodeURIComponent(
            JSON.stringify(monList)
        )}`,
    })
        .then((res) => res.json())
        .then((data) => {
            alert(data.message);
            if (data.success) {
                $("#tachBanModal").modal("hide");
                location.reload();
            }
        });
}

//thanh toan
function thanhToan() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectTable"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một bàn để thanh toán.");
        return;
    }
    if (selected.length > 1) {
        alert("Bạn Chỉ Có Thể Chọn Một Bàn Tại Một Thời Điểm Để Thanh Toán.");
        return;
    }
    const maBan = selected[0];
    fetch(`/thanhtoan/${maBan}`)
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showThanhToanModal(data);
            } else {
                alert(data.message || "Không thể thanh toán bàn này.");
            }
        })
        .catch((error) => {
            alert("Có lỗi xảy ra khi lấy thông tin thanh toán.");
            console.log(error);
        });
}

function showThanhToanModal(data) {
    // data: {maBan, maHoaDon, monList: [{tenMon, soLuong, thanhTien}], tongTien}
    document.getElementById("tt_maBan").textContent = data.maBan;
    document.getElementById("tt_maHoaDon").textContent = data.maHoaDon;
    let tbody = "";
    data.monList.forEach(function (mon) {
        tbody += `<tr>
            <td>${mon.tenMon}</td>
            <td>${mon.soLuong}</td>
            <td>${mon.thanhTien}</td>
        </tr>`;
    });
    document.getElementById("tt_tableBody").innerHTML = tbody;
    document.getElementById("tt_tongTien").textContent = data.tongTien;
    // Lưu lại maBan, maHoaDon, tongTien cho submit
    document.getElementById("tt_maBanInput").value = data.maBan;
    document.getElementById("tt_maHoaDonInput").value = data.maHoaDon;
    document.getElementById("tt_tongTienInput").value = data.tongTien;
    $("#thanhtoanModal").modal("show");
}

function submitThanhToan(event) {
    event.preventDefault();
    const maBan = document.getElementById("tt_maBanInput").value;
    const maHoaDon = document.getElementById("tt_maHoaDonInput").value;
    const tongTien = document.getElementById("tt_tongTienInput").value;
    fetch("/thanhtoan", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `maBan=${maBan}&maHoaDon=${maHoaDon}&tongTien=${tongTien}`,
    })
        .then((res) => res.json())
        .then((data) => {
            alert(data.message);
            if (data.success) {
                $("#thanhtoanModal").modal("hide");
                location.reload();
            }
        });
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

    var tbody = "";
    if (Array.isArray(data.monList) && data.monList.length > 0) {
        data.monList.forEach(function (mon) {
            tbody += `<tr>
                <td>${
                    mon.tenMon || "Khách chưa gọi món - Mau chốt đi còn gì ^^"
                }</td>
                <td>${mon.soLuong != null ? mon.soLuong : "Chưa cập nhật"}</td>
                <td>${
                    mon.giaTaiThoiDiemBan != null
                        ? mon.giaTaiThoiDiemBan
                        : "Chưa cập nhật"
                }</td>
                <td>${
                    mon.thanhTien != null ? mon.thanhTien : "Chưa cập nhật"
                }</td>
            </tr>`;
        });
    } else {
        tbody = `<tr><td colspan="4" class="text-center">Chưa cập nhật</td></tr>`;
    }
    var monTable = document.querySelector("#xemBan_monTable tbody");
    if (monTable) monTable.innerHTML = tbody;

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
            console.log("DATA DANHSACHBANRANH:", data);
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
    let selectHtml = '<select id="maBanMoiSelect" class="form-select">';
    selectHtml += '<option value="">-- Chọn bàn mới --</option>';
    (data.data || []).forEach((ban) => {
        if (ban.maBan !== undefined && ban.maBan !== null) {
            selectHtml += `<option value="${ban.maBan}">${
                ban.tenBan || "Bàn " + ban.maBan
            }</option>`;
        }
    });
    selectHtml += "</select>";
    document.getElementById("maBanCuInput").value = maBanCu;
    document.getElementById("chuyenBanDanhSach").innerHTML = selectHtml;
    $("#chuyenBanModal").modal("show");
}

function submitChuyenBan() {
    const maBanCu = document.getElementById("maBanCuInput").value;
    const maBanMoiSelect = document.getElementById("maBanMoiSelect");
    let maBanMoi = maBanMoiSelect ? maBanMoiSelect.value : null;

    if (!maBanMoi || isNaN(Number(maBanMoi))) {
        console.log("maBanMoi gửi lên:", maBanMoi);
        alert("Vui lòng chọn bàn mới để chuyển!");
        return;
    }
    maBanMoi = Number(maBanMoi);

    fetch("/chuyenban", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `maBanCu=${maBanCu}&maBanMoi=${maBanMoi}`,
    })
        .then((res) => res.json())
        .then((data) => {
            showDynamicAlert(data.message, data.success ? "success" : "danger");
            if (data.success) {
                $("#chuyenBanModal").modal("hide");
                setTimeout(() => location.reload(), 2000); // reload sau khi alert ẩn
            }
        });
    //         .then((data) => {
    //             alert(data.message);
    //             if (data.success) {
    //                 $("#chuyenBanModal").modal("hide");
    //                 location.reload();
    //             }
    //         });
}

function showDynamicAlert(message, type = "success") {
    const alertDiv = document.getElementById("dynamicAlert");
    alertDiv.className = "alert alert-" + type;
    alertDiv.textContent = message;
    alertDiv.style.display = "block";
    setTimeout(() => {
        alertDiv.style.display = "none";
    }, 2000);
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
    document.getElementById("ctd_maHoaDon").textContent = maHoaDon;
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

// GỘP BÀN
function gopBan() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectTable"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một bàn nguồn để gộp!");
        return;
    }
    fetch(`/gopban/chonbandich?banNguon=${selected.join(",")}`)
        .then((res) => res.json())
        .then((data) => {
            if (data.success) {
                showChonBanDichModal(selected, data.banDichList);
            } else {
                alert(data.message || "Không lấy được danh sách bàn đích.");
            }
        });
}

function showChonBanDichModal(banNguon, banDichList) {
    let selectHtml = '<select id="gopBan_banDichSelect" class="form-select">';
    banDichList.forEach((ban) => {
        selectHtml += `<option value="${ban.maBan}">${ban.tenBan}</option>`;
    });
    selectHtml += "</select>";
    document.getElementById("gopBan_banNguonInput").value = banNguon.join(",");
    document.getElementById("gopBan_banDichList").innerHTML = selectHtml;
    $("#gopBanChonBanDichModal").modal("show");
}

function submitChonBanDich(event) {
    event.preventDefault();
    const banNguon = document
        .getElementById("gopBan_banNguonInput")
        .value.split(",");
    const banDich = document.getElementById("gopBan_banDichSelect").value;
    // Hiện modal nhập thông tin khách
    document.getElementById("gopBan_banNguonInput2").value = banNguon.join(",");
    document.getElementById("gopBan_banDichInput2").value = banDich;
    $("#gopBanChonBanDichModal").modal("hide");
    $("#gopBanNhapKhachModal").modal("show");
}

function submitNhapKhach(event) {
    event.preventDefault();
    const banNguon = document
        .getElementById("gopBan_banNguonInput2")
        .value.split(",");
    const banDich = document.getElementById("gopBan_banDichInput2").value;
    const tenKhach = document.getElementById("gopBan_tenKhach").value;
    const sdtKhach = document.getElementById("gopBan_sdtKhach").value;
    const ngayGioDat = document.getElementById("gopBan_ngayGioDat").value;
    fetch("/gopban/tonghopmon", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `banNguon=${banNguon.join(
            ","
        )}&banDich=${banDich}&tenKhach=${encodeURIComponent(
            tenKhach
        )}&sdtKhach=${encodeURIComponent(
            sdtKhach
        )}&ngayGioDat=${encodeURIComponent(ngayGioDat)}`,
    })
        .then((res) => res.json())
        .then((data) => {
            if (data.success) {
                showXacNhanMonGopModal(
                    banNguon,
                    banDich,
                    tenKhach,
                    sdtKhach,
                    ngayGioDat,
                    data.monList
                );
            } else {
                alert(data.message || "Không tổng hợp được món.");
            }
        });
    $("#gopBanNhapKhachModal").modal("hide");
}

function showXacNhanMonGopModal(
    banNguon,
    banDich,
    tenKhach,
    sdtKhach,
    ngayGioDat,
    monList
) {
    let tbody = "";
    monList.forEach((mon, idx) => {
        tbody += `<tr>
            <td>${mon.tenMon}</td>
            <td><input type='number' min='1' value='${mon.soLuong}' class='form-control gopBan_soLuong' data-idx='${idx}' data-mathucdon='${mon.maThucDon}' data-giatien='${mon.giaTaiThoiDiemBan}' onchange='updateThanhTienGopBan(${idx})'></td>
            <td><span id='gopBan_thanhTien_${idx}'>${mon.thanhTien}</span></td>
        </tr>`;
    });
    document.getElementById("gopBan_monTableBody").innerHTML = tbody;
    document.getElementById("gopBan_banNguonInput3").value = banNguon.join(",");
    document.getElementById("gopBan_banDichInput3").value = banDich;
    document.getElementById("gopBan_tenKhachInput3").value = tenKhach;
    document.getElementById("gopBan_sdtKhachInput3").value = sdtKhach;
    document.getElementById("gopBan_ngayGioDatInput3").value = ngayGioDat;
    updateTongTienGopBan();
    $("#gopBanXacNhanMonModal").modal("show");
}

function updateThanhTienGopBan(idx) {
    const input = document.querySelector(`input[data-idx='${idx}']`);
    const soLuong = parseInt(input.value) || 0;
    const gia = parseFloat(input.getAttribute("data-giatien"));
    const thanhTien = soLuong * gia;
    document.getElementById(`gopBan_thanhTien_${idx}`).textContent = thanhTien;
    updateTongTienGopBan();
}

function updateTongTienGopBan() {
    let tong = 0;
    document.querySelectorAll(".gopBan_soLuong").forEach((inp) => {
        const sl = parseInt(inp.value) || 0;
        const g = parseFloat(inp.getAttribute("data-giatien"));
        tong += sl * g;
    });
    document.getElementById("gopBan_tongTien").textContent = tong;
}

function submitXacNhanMonGop(event) {
    event.preventDefault();
    const banNguon = document
        .getElementById("gopBan_banNguonInput3")
        .value.split(",");
    const banDich = document.getElementById("gopBan_banDichInput3").value;
    const tenKhach = document.getElementById("gopBan_tenKhachInput3").value;
    const sdtKhach = document.getElementById("gopBan_sdtKhachInput3").value;
    const ngayGioDat = document.getElementById("gopBan_ngayGioDatInput3").value;
    let maThucDon = [],
        soLuong = [],
        gia = [];
    document.querySelectorAll(".gopBan_soLuong").forEach((inp) => {
        maThucDon.push(inp.getAttribute("data-mathucdon"));
        soLuong.push(inp.value);
        gia.push(inp.getAttribute("data-giatien"));
    });
    const tongTien = document.getElementById("gopBan_tongTien").textContent;
    fetch("/gopban/xacnhan", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `banNguon=${banNguon.join(
            ","
        )}&banDich=${banDich}&tenKhach=${encodeURIComponent(
            tenKhach
        )}&sdtKhach=${encodeURIComponent(
            sdtKhach
        )}&ngayGioDat=${encodeURIComponent(
            ngayGioDat
        )}&maThucDon=${maThucDon.join(",")}&soLuong=${soLuong.join(
            ","
        )}&gia=${gia.join(",")}&tongTien=${tongTien}`,
    })
        .then((res) => res.json())
        .then((data) => {
            showGopBanKetQuaModal(data.success, data.message);
        });
    $("#gopBanXacNhanMonModal").modal("hide");
}

function showGopBanKetQuaModal(success, message) {
    document.getElementById("gopBan_ketQuaMessage").textContent = message;
    $("#gopBanKetQuaModal").modal("show");
    if (success) {
        setTimeout(() => {
            location.reload();
        }, 1500);
    }
}
