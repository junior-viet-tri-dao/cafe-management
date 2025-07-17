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

document.addEventListener("DOMContentLoaded", function () {
    var form = document.querySelector(
        'form[th\\:action="@{/admin/hanghoa/hanghoa-xuathang}"]'
    );
    var soLuongInput = document.querySelector('input[name="soLuong"]');
    var hangHoaSelect = document.getElementById("hangHoaSelect");
    var hangHoaList = /*[[${hangHoaList}]]*/ []; // Thymeleaf sẽ render danh sách này

    form.addEventListener("submit", function (e) {
        var soLuongXuat = parseInt(soLuongInput.value, 10);
        var maHangHoa = hangHoaSelect.value;
        var hangHoa = hangHoaList.find((h) => h.maHangHoa == maHangHoa);
        if (hangHoa) {
            var soLuongConLai = hangHoa.soLuong - soLuongXuat;
            if (soLuongConLai === 0) {
                var ok = confirm(
                    "Với số lượng xuất này bạn sẽ hết hàng trong kho, bạn có muốn tiếp tục không?"
                );
                if (!ok) {
                    e.preventDefault();
                    soLuongInput.focus();
                }
            }
        }
    });
});
