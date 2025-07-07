function nhapThietBi() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectThietBi"]:checked'
    );
    checkboxes.forEach((checkbox) => selected.push(checkbox.value));
    if (selected.length === 0) {
        alert("Chọn ít nhất một thiết bị để Nhập.");
        return;
    }
    if (selected.length > 1) {
        alert("Bạn Chỉ Có Thể Nhập Một Thiết Bị Tại Một Thời Điểm.");
        return;
    }
    //
    fetch(`/nhapthietbi/${selected[0]}`)
        .then((response) => response.json())
        .then((data) => {
            if (data.success) {
                showNhapThietBiModal(data);
            } else {
                alert(data.message);
            }
        })
        .catch((error) => {
            console.log(error);
        });
}
