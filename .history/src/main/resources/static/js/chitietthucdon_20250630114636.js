window.updateGiaTien = function () {
    var select = document.getElementById("thucDonSelect");
    var giaTienSpan = document.getElementById("giaTienHienTai");
    var selectedId = select.value;
    var gia = "";
    for (var i = 0; i < window.thucDons.length; i++) {
        if (window.thucDons[i].maThucDon == selectedId) {
            gia = window.thucDons[i].giaTienHienTai;
            break;
        }
    }
    if (giaTienSpan) {
        giaTienSpan.textContent = gia ? gia + " VNĐ" : "";
    }
};

window.addThanhPhanRow = function () {
    var table = document
        .getElementById("thanhPhanTable")
        .getElementsByTagName("tbody")[0];
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    row.innerHTML = document
        .getElementById("thanhPhanRowTemplate")
        .innerHTML.replace(/__index__/g, rowCount);
};

window.removeThanhPhanRow = function (btn) {
    var row = btn.parentNode.parentNode;
    row.parentNode.removeChild(row);
};

document.addEventListener("DOMContentLoaded", function () {
    window.updateGiaTien();
});
