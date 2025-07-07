function nhapThietBi() {
    // Lấy danh sách các thiết bị đã chọn
    var selectedThietBi = [];
    $("input[name='selectThietBi']:checked").each(function () {
        selectedThietBi.push($(this).val());
    });

    if (selectedThietBi.length === 0) {
        alert("Vui lòng chọn ít nhất một thiết bị để nhập.");
        return;
    }

    // Gửi yêu cầu nhập thiết bị
    $.ajax({
        url: "/admin/thietbi/nhap",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(selectedThietBi),
        success: function (response) {
            alert("Nhập thiết bị thành công.");
            // Cập nhật lại danh sách thiết bị
            location.reload();
        },
        error: function (xhr, status, error) {
            alert("Đã xảy ra lỗi trong quá trình nhập thiết bị.");
        },
    });
}
