function addThanhPhanRow() {
    var table = document
        .getElementById("thanhPhanTable")
        .getElementsByTagName("tbody")[0];
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    row.innerHTML = document
        .getElementById("thanhPhanRowTemplate")
        .innerHTML.replace(/__index__/g, rowCount);
}
function removeThanhPhanRow(btn) {
    var row = btn.parentNode.parentNode;
    row.parentNode.removeChild(row);
}
