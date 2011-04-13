function loadPlanetsTable() {
    disableSubjCRUDButtons();
    showLoading("loading_planetsList", "table");

    $.ajax(
        { url: "./planetsList"
        , cache: false
        , dataType: "html"
        , global: false
        , timeout: 25000
        , type: "GET"
        , success:
            function(ret_html){
                $('#planetsList').empty();
                $('#planetsList').append(ret_html);
            }
        , error:
            function(ret){
                $('#planetsList').empty();
                $('#planetsList').append('<span class="error">Error!</span>');
                $('#planetsList').append(ret.responseText);
            }
        , complete:
            function(ret_html){
                hideLoading("loading_planetsList");
                enableSubjCRUDButtons();
            }
        }
    );
}