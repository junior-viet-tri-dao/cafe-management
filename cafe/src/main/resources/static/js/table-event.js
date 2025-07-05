// Mở chi tiết bàn
function submitViewDetail() {
    const form = document.getElementById("tableActionForm");
    const checkedBoxes = form.querySelectorAll("input[name='tableIds']:checked");

    if (checkedBoxes.length !== 1) {
        alert("Vui lòng chọn đúng 1 bàn để xem chi tiết!");
        return;
    }

    const tableId = checkedBoxes[0].value;
    window.location.href = "/sale/view_detail?tableIds=" + tableId;
}


// Chọn thực đơn
function submitSelectMenu() {
    const form = document.getElementById("tableActionForm");

    const checkedBoxes = form.querySelectorAll("input[name='tableIds']:checked");
    if (checkedBoxes.length !== 1) {
        alert("Vui lòng chọn đúng 1 bàn để gọi món!");
        return;
    }

    const tableId = checkedBoxes[0].value;
    window.location.href = "/sale/select?tableId=" + tableId;
}


// Thanh toán
function submitPayment() {
    const form = document.getElementById("tableActionForm");
    const checkedBoxes = form.querySelectorAll("input[name='tableIds']:checked");
    if (checkedBoxes.length !== 1) {
        alert("Vui lòng chọn đúng 1 bàn để thanh toán!");
        return;
    }
    const tableId = checkedBoxes[0].value;
    window.location.href = "/sale/payment?tableId=" + tableId;
}

// Kiểm tra chuyển bàn
function validateSwitchForm() {
    const selected = document.getElementById("targetTableId").value;
    if (!selected) {
        alert("Vui lòng chọn bàn muốn chuyển đến!");
        return false;
    }
    return true;
}


// kiểm tra gộp bàn
function validateMergeForm() {
    const checked = document.querySelectorAll('input[name="sourceTableIds"]:checked');
    if (checked.length === 0) {
        alert("Vui lòng chọn ít nhất một bàn để gộp!");
        return false;
    }
    return true;
}

function submitSwitchTable() {
    const form = document.getElementById("tableActionForm");
    const checkedBoxes = form.querySelectorAll("input[name='tableIds']:checked");

    if (checkedBoxes.length !== 1) {
        alert("Vui lòng chọn đúng 1 bàn để chuyển!");
        return;
    }

    const sourceTableId = parseInt(checkedBoxes[0].value);
    if (isNaN(sourceTableId)) {
        alert("ID bàn không hợp lệ.");
        return;
    }

    const url = `/sale/switch?sourceTableId=${sourceTableId}`;
    window.location.href = url;
}


function submitSplitTable() {
    const checkedBoxes = document.querySelectorAll("input[name='tableIds']:checked");

    if (checkedBoxes.length !== 2) {
        alert("Vui lòng chọn đúng 2 bàn để tách!");
        return;
    }

    const sourceTableId = parseInt(checkedBoxes[0].value);
    const targetTableId = parseInt(checkedBoxes[1].value);

    const url = `/sale/split?sourceTableId=${sourceTableId}&targetTableId=${targetTableId}`;
    window.location.href = url;
}


function submitMergeTable() {
    window.location.href = "/sale/merge";
}

function syncTargetTableIdAndReload() {
    const sourceId = document.querySelector('input[name="sourceTableId"]').value;
    const targetId = document.getElementById("targetTableIdSelect").value;
    if (targetId) {
        window.location.href = `/sale/split?sourceTableId=${sourceId}&targetTableId=${targetId}`;
    }
}

function moveItem(itemId, from, moveAll = false) {
    const srcQtyId = from === 'src' ? 'srcQty_' + itemId : 'destQty_' + itemId;
    const destQtyId = from === 'src' ? 'destQty_' + itemId : 'srcQty_' + itemId;
    const inputId = from === 'src' ? 'srcInput_' + itemId : 'destInput_' + itemId;

    const srcQtyElem = document.getElementById(srcQtyId);
    const destQtyElem = document.getElementById(destQtyId);
    const inputElem = document.getElementById(inputId);

    let srcQty = parseInt(srcQtyElem.innerText || srcQtyElem.value || 0);
    let destQty = parseInt(destQtyElem.innerText || destQtyElem.value || 0);
    let inputVal = parseInt(inputElem.value || 0);

    if (moveAll) {
        destQtyElem.innerText !== undefined ? destQtyElem.innerText = destQty + srcQty : destQtyElem.value = destQty + srcQty;
        srcQtyElem.innerText !== undefined ? srcQtyElem.innerText = 0 : srcQtyElem.value = 0;
    } else {
        if (inputVal > srcQty) {
            alert("Không thể chuyển quá số lượng hiện có!");
            return;
        }
        destQtyElem.innerText !== undefined ? destQtyElem.innerText = destQty + inputVal : destQtyElem.value = destQty + inputVal;
        srcQtyElem.innerText !== undefined ? srcQtyElem.innerText = srcQty - inputVal : srcQtyElem.value = srcQty - inputVal;
    }

    inputElem.value = 0;
}

function prepareSplitForm() {
    const targetId = document.getElementById("targetTableIdHidden").value;
    if (!targetId) {
        alert("Vui lòng chọn bàn đích!");
        return false;
    }

    let hasItem = false;
    document.querySelectorAll("span[id^='destQty_'], input[id^='destQty_']").forEach(el => {
        const val = el.tagName === 'SPAN' ? parseInt(el.innerText || 0) : parseInt(el.value || 0);
        if (val > 0) hasItem = true;
    });

    if (!hasItem) {
        alert("Bạn chưa chuyển món nào!");
        return false;
    }
    return true;
}
