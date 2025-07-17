document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='ngayGioDat']", {
        enableTime: true,
        dateFormat: "d/m/Y H:i",
        time_24hr: true,
        allowInput: false,
    });
});

document.addEventListener("DOMContentLoaded", function () {
    flatpickr("input[name='ngayGioDat-gopBan']", {
        enableTime: true,
        dateFormat: "d/m/Y H:i",
        time_24hr: true,
        allowInput: false,
    });
});
