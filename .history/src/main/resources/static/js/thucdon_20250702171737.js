function getSelectedThucDon() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectThucDon"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    return selected;
}

function deleteThucDon() {
    let selectedThucDon = getSelectedThucDon();
    if (selectedThucDon.length === 0) {
        alert("Chọn ít nhất một món ăn để xóa.");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa những món ăn đã chọn?")) {
        let form = document.createElement("form");
        form.method = "POST";
        form.action = "admin/thucdon/delete";

        selectedThucDon.forEach((thucDonId) => {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = "Ids";
            input.value = thucDonId;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
}

function updateThucDon() {
    let selectedThucDon = getSelectedThucDon();
    if (selectedThucDon.length === 0) {
        alert("Chọn ít nhất một món ăn để cập nhật.");
        return;
    }

    if (selectedThucDon.length > 1) {
        alert("Bạn Chỉ Có Thể Cập Nhật Một Món Ăn Tại Một Thời Điểm.");
        return;
    }
    window.location.href = `/admin/thucdon/thucdon-update/${selectedThucDon[0]}`;
}
