function nhapThietBi() {
    let selected = [];
    let checkboxes = document.querySelectorAll(
        'input[name="selectThietBi"]:checked'
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
