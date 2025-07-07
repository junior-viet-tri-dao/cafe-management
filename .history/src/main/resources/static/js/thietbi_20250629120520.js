function getSelectedThietBi() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectThietBi"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    return selected;
}

function deleteThietBi() {
    let selectedThietBi = getSelectedThietBi();
    if (selectedThietBi.length === 0) {
        alert("Chọn ít nhất một thiết bị để xóa.");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa những thiết bị đã chọn?")) {
        let form = document.createElement("form");
        form.method = "POST";
        form.action = "admin/thietbi/delete";

        selectedThietBi.forEach((thietBiId) => {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = "ids";
            input.value = thietBiId;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
}

function updateThietBi() {
    let selectedThietBi = getSelectedThietBi();
    if (selectedThietBi.length === 0) {
        alert("Chọn ít nhất một thiết bị để cập nhật.");
        return;
    }

    if (selectedThietBi.length > 1) {
        alert("Bạn Chỉ Có Thể Cập Nhật Một Thiết Bị Tại Một Thời Điểm.");
        return;
    }
    window.location.href = `/admin/thietbi/update/${selectedThietBi[0]}`;
}
