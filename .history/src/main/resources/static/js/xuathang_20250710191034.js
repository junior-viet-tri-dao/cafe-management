document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='ngayXuat']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
});

document.querySelector("form").addEventListener("submit", function (e) {
    var ngayXuat = document.querySelector("[name='ngayXuat']").value.trim();
    if (!ngayXuat) {
        alert("Vui lòng nhập ngày xuất!");
        e.preventDefault();
    }
});
