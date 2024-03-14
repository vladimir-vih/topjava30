const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        populateTable(data);
    });
}

function changeState(event) {
    let checkBox = event.target;
    let id = checkBox.closest('tr').getAttribute('id');
    let newState = checkBox.checked;
    $.post(ctx.ajaxUrl + id + "/" + newState)
        .done(function () {
            checkBox.closest('tr').setAttribute("user-enabled", newState);
            successNoty("User state changed");
        })
        .fail(function () {
            checkBox.checked = !newState;
        });
}