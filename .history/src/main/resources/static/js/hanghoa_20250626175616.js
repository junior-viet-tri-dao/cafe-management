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
    //
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
