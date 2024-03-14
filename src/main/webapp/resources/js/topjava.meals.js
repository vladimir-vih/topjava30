const mealAjaxUrl = "ajax/meals/";
// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    "desc"
                ]
            ]
        })
    );
});

function updateTable() {
    let filterForm = $('#filterForm');
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: filterForm.serialize()
    }).done(
        function (data) {
            populateTable(data);
        });
}

function resetFilter() {
    document.getElementById("filterForm").reset();
    updateTable();
    successNoty("Filter reset");
}