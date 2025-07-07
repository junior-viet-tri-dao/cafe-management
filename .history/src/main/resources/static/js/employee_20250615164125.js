function getSelectedEmployees() {
    let selected = [];
    let checkboxes = document.querySelectorAll('input[name="selectedEmployee"]:checked');
    checkboxes.forEach(checkbox => selected.push(checkbox.value));
    return selected;
}



function deleteEmployee() {
    let selectedEmployees = getSelectedEmployees();
    if (selectedEmployees.length === 0) {
        alert("Please select at least one employee to delete.");
        return;
    }


    if (confirm("Are you sure you want to delete the selected employee?")) {

        let form = document.createElement("form");
        form.method = "POST";
        form.action = "/admin/employee/delete";

        selectedEmployees.forEach(userId => {
            let input = document.createElement("input");
            input.type = "hidden";
            input.name = "userIds";
            input.value = userId;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
}


function updateEmployee() {
    let selectedEmployees = getSelectedEmployees();
    if (selectedEmployees.length === 0) {
        alert("Please select a employee to update.");
        return;
    }

    if (selectedEmployees.length > 1) {
        alert("You can only update one employee at a time.");
        return;
    }

    window.location.href = `/admin/employee/update/${selectedEmployees[0]}`;
}