document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='fromDate']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
    flatpickr("input[name='toDate']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
});

document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='ngayChi']", {
        dateFormat: "d/m/Y",
        allowInput: false,
    });
});
