function submitReservation() {
    const form = document.getElementById('tableActionForm');
    const checked = form.querySelectorAll('input[name="tableIds"]:checked');
    if (checked.length === 0) {
        alert('Vui lòng chọn ít nhất một bàn để đặt.');
        return;
    }

    const params = new URLSearchParams();
    checked.forEach(cb => params.append('tableIds', cb.value));
    window.location.href = '/sale/reservation/new?' + params.toString();
}

function confirmCancel() {
    const form = document.getElementById("tableActionForm");
    const checked = form.querySelectorAll('input[name="tableIds"]:checked');
    if (checked.length === 0) {
        alert("Vui lòng chọn ít nhất một bàn để hủy.");
        return;
    }

    if (confirm("Bạn có chắc chắn muốn hủy bàn đã chọn không?")) {
        form.action = "/sale/cancel";
        form.method = "post";
        form.submit();
    }
}