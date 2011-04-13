// generalized, but at it's current implementation allows only one CRUD panel per whole page

// var crud_buttons_locked = false; // declared by the buttons

function viewCrud(op, busin_obj_id, busin_obj_name){
    crud_buttons_locked = true;
    disableAllCRUDButtons();
    showLoading("loading_crudOp", "CRUD table");

    $.ajax(
        { url: "./" + busin_obj_name + "/crud"
        , cache: false
        , dataType: "html"
        , global: false
        , timeout: 15000
        , headers: {busin_obj_id: busin_obj_id, operation: op} // do not put in "data" if you use Spring.MVC.SimpleFormController, because it will forward these fields in the action of next <form:form ... >, and if you happen to have fields with similar names in the form, you'll get |field=array| instead |field=value| with the form submition
        , type: "GET"
        , success:
            function(ret_html){
                $('#crud_operation').empty();
                $('#crud_operation').append(ret_html);
            }
        , error:
            function(ret){
                $('#crud_operation').empty();
                $('#crud_operation').append("<span class='error'>Error!</span>");
                $('#crud_operation').append(ret.responseText);
            }
        , complete:
            function(ret_html){
                hideLoading("loading_crudOp");
                crud_buttons_locked = false;
                enableAllCRUDButtons();
            }
        }
    );
}

function disableAllCRUDButtons() {
    $('#crud_buttons > input').each(function(idx, elem){ elem.disabled = true; });
}
function enableAllCRUDButtons() {
    $('#crud_buttons > input').each(function(idx, elem){ if(!crud_buttons_locked) if(idx == 0 || cur_selection_holder != null) elem.disabled = false; });
}
function disableSubjCRUDButtons() {
    $('#crud_buttons > input').each(function(idx, elem){ if(idx>0) elem.disabled = true; });
}
function enableSubjCRUDButtons() {
    $('#crud_buttons > input').each(function(idx, elem){ if(idx>0) if(!crud_buttons_locked && cur_selection_holder != null) elem.disabled = false; });
}

// ==============================================================

// var op_commitments_buttons_locked = false; // declared by the form

function disableCRUDCommitmentButtons() {
    $('#op_commitment > input').each(function(idx, elem){ elem.disabled = true; });
}
function enableCRUDCommitmentButtons() {
    $('#op_commitment > input').each(function(idx, elem){ if(!op_commitments_buttons_locked) elem.disabled = false; });
}

function cancelCRUDOp() {
    $('#crud_operation').empty();
    enableAllCRUDButtons();
}

// generalized !!!
function commitCRUDOp(crud_form, onsuccess) {
    $("#loading_crudOp").show();
    showLoading("loading_crudOp", " CRUD operation commitment result");
    op_commitments_buttons_locked = true;

    disableCRUDCommitmentButtons();

    $.ajax(
        { url: crud_form.action
        , cache: false
        , dataType: "html"
        , data: $("#" + crud_form.id).serialize()
        , global: false
        , timeout: 15000
        , type: "POST"
        , success:
            function(ret_html){
                $('#crud_operation').empty();
                $('#crud_operation').append(ret_html);
                onsuccess();
            }
        , error:
            function(ret){
                $('#crud_operation').empty();
                $('#crud_operation').append("<span class='error'>Error!</span>");
                $('#crud_operation').append(ret.responseText);
            }
        , complete:
            function(ret_html){
                hideLoading("loading_crudOp");
                op_commitments_buttons_locked = false;
                enableCRUDCommitmentButtons();
            }
        }
    );
}