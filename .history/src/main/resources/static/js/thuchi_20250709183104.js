document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='fromDate']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
    flatpickr("input[name='ngayKetThuc']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
});
