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
    let startDate = document.getElementById("inputFilterStartDate").value;
    let endDate = document.getElementById("inputFilterEndDate").value;
    let startTime = document.getElementById("inputFilterStartTime").value;
    let endTime = document.getElementById("inputFilterEndTime").value;
    $.get(mealAjaxUrl + "filter?" +
        "startDate=" + startDate +
        "&endDate=" + endDate +
        "&startTime=" + startTime +
        "&endTime=" + endTime,
        function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
}

function resetFilter() {
    let form = $('#filterForm');
    form.find(":input").val("");
    updateTable();
    successNoty("Filter reset");
}