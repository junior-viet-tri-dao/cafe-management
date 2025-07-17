function nhapHangHoa() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectHangHoa"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một hàng hóa để nhập.");
        return;
    }
    if (selected.length > 1) {
        alert("Bạn Chỉ Có Thể Nhập Một Hàng Hóa Tại Một Thời Điểm.");
        return;
    }
    // gửi data be
    fetch(`/hanghoa/nhapHangHoa/${selected[0]}`)
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showDonNhapModal(data);
            } else {
                alert(data.message);
            }
        })
        .catch((error) => {
            console.log(error);
        });
}

function showDonNhapModal(data) {
    // data: {maHangHoa, tenHangHoa, donViTinh}
    document.getElementById("donNhap_maHangHoa").value = data.maHangHoa;
    document.getElementById("donNhap_tenHangHoa").value = data.tenHangHoa;
    document.getElementById("donNhap_donViTinh").value = data.donViTinh;
    document.getElementById("donNhap_ngayNhap").value = "";
    document.getElementById("donNhap_soLuong").value = "";
    document.getElementById("donNhap_tongTien").value = "";
    $("#donNhapModal").modal("show");
}

document.addEventListener("DOMContentLoaded", function () {
    fetchDanhSachDonNhap();
});

function fetchDanhSachDonNhap() {
    fetch("/hanghoa/danhsachnhap")
        .then((res) => res.json())
        .then((data) => {
            if (data.success) {
                renderDanhSachDonNhap(data.list);
            }
        });
}

function formatVND(number) {
    if (number == null) return "";
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND";
}

function formatDateToDMY(dateStr) {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) return dateStr; // nếu không phải ngày hợp lệ thì trả về nguyên bản
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function renderDanhSachDonNhap(list) {
    const tbody = document.querySelector("tbody.son");
    if (!tbody) return;
    tbody.innerHTML = "";
    list.forEach((dn) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td></td>
            <td>${dn.tenHangHoa}</td>
            <td>${formatDateToDMY(dn.ngayNhap)}</td>
            <td>${dn.soLuong}</td>
            <td>${dn.donViTinh}</td>
            <td>${formatVND(dn.donGia)}</td>
            <td>${formatVND(dn.tongTien)}</td>
        `;
        tbody.appendChild(tr);
    });
}

function submitDonNhap(event) {
    event.preventDefault();
    const maHangHoa = document.getElementById("donNhap_maHangHoa").value;
    const ngayNhap = document.getElementById("donNhap_ngayNhap").value;
    const soLuong = document.getElementById("donNhap_soLuong").value;
    const tongTien = document.getElementById("donNhap_tongTien").value;
    fetch("/hanghoa/nhap", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `maHangHoa=${maHangHoa}&ngayNhap=${ngayNhap}&soLuong=${soLuong}&tongTien=${tongTien}`,
    })
        .then((res) => res.json())
        .then((data) => {
            alert(data.message);
            if (data.success) {
                $("#donNhapModal").modal("hide");
                if (window.location.pathname.includes("hanghoa-danhsachnhap")) {
                    renderDanhSachDonNhap([data.donNhap]);
                } else {
                    location.reload();
                }
            }
        });
}

function getSelectedHangHoa() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectHangHoa"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    return selected;
}

function deleteHangHoa() {
    let selectedHangHoa = getSelectedHangHoa();
    if (selectedHangHoa.length === 0) {
        alert("Chọn ít nhất một hàng hóa để xóa.");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa những hàng hóa đã chọn?")) {
        let form = document.createElement("form");
        form.method = "POST";
        form.action = "/admin/hanghoa/delete";

        selectedHangHoa.forEach((hangHoaId) => {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = "hangHoaIds";
            input.value = hangHoaId;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
}
