function getSelectedKhuyenMai() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectKhuyenMai"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    return selected;
}

function deleteKhuyenMai() {
    let selectedKhuyenMai = getSelectedKhuyenMai();
    if (selectedKhuyenMai.length === 0) {
        alert("Chọn ít nhất một khuyến mãi để xóa.");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa những khuyến mãi đã chọn?")) {
        let form = document.createElement("form");
        form.method = "POST";
        form.action = "/admin/khuyenmai/delete";

        selectedKhuyenMai.forEach((khuyenMaiId) => {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = "Ids";
            input.value = khuyenMaiId;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
}

function updateKhuyenMai() {
    let selectedKhuyenMai = getSelectedKhuyenMai();
    if (selectedKhuyenMai.length === 0) {
        alert("Chọn ít nhất một khuyến mãi để cập nhật.");
        return;
    }

    if (selectedKhuyenMai.length > 1) {
        alert("Bạn Chỉ Có Thể Cập Nhật Một Khuyến Mãi Tại Một Thời Điểm.");
        return;
    }
    window.location.href = `/admin/khuyenmai/khuyenmai-update/${selectedKhuyenMai[0]}`;
}

document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='ngayBatDau']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
    flatpickr("input[name='ngayKetThuc']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
});
