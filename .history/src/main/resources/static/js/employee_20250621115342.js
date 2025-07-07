function getSelectedEmployees() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectedEmployee"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    return selected;
}

function deleteEmployee() {
    let selectedEmployees = getSelectedEmployees();
    if (selectedEmployees.length === 0) {
        alert("Vui lòng chọn ít nhất một nhân viên để xóa.");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn xóa những nhân viên đã chọn?")) {
        let form = document.createElement("form");
        form.method = "POST";
        form.action = "/admin/employee/delete";

        selectedEmployees.forEach((userId) => {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = "userIds";
            input.value = userId;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
}

function updateEmployee() {
    let selectedEmployees = getSelectedEmployees();
    if (selectedEmployees.length === 0) {
        alert("Chọn ít nhất một nhân viên để cập nhật.");
        return;
    }

    if (selectedEmployees.length > 1) {
        alert("Bạn Chỉ Có Thể Cập Nhật Một Nhân Viên Tại Một Thời Điểm.");
        return;
    }

    window.location.href = `/admin/employee/update/${selectedEmployees[0]}`;
}
