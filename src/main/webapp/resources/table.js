// generalized, but at it's current implementation allows only one table per whole page

function getSelectedBusinObjId() {
    var busin_obj_id = cur_selection_holder.getAttribute("data-busin_obj_id");
    if(busin_obj_id == null || isNaN(busin_obj_id)) abort("Page error! Business object ID unspecified!");
    return busin_obj_id;
}

// var cur_selection_holder = null;    // declared by the table
// var cur_selection_candidate = null; // declared by the table

function highlightRow(elem){
    highlightRow_(elem, 'transparent', '#95F0D4');
}

function uNhighlightRow(elem) {
    uNhighlightRow_(elem, "transparent");
}

function selectRow(elem) {
    selectRow_(elem, "transparent", "#95F0D4", "#CDF095");
    if(cur_selection_holder == null) disableSubjCRUDButtons();
    else enableSubjCRUDButtons();
    // to access businass object ID use cur_selection_holder.getAttribute("data-busin_obj_id");
}

function highlightRow_(elem, unselected_backcolor, selected_backcolor){
    if(cur_selection_candidate != elem && cur_selection_holder != elem) {
        if(cur_selection_candidate != null)
            cur_selection_candidate.style["backgroundColor"] = unselected_backcolor;
        cur_selection_candidate = elem;
        cur_selection_candidate.style["backgroundColor"] = selected_backcolor;
    }
}

function uNhighlightRow_(elem, unselected_backcolor) {
    if(cur_selection_holder != elem) {
        if(cur_selection_candidate != null)
            cur_selection_candidate.style["backgroundColor"] = unselected_backcolor;
        cur_selection_candidate = null;
    }
}

function selectRow_(elem, unselected_backcolor, selected_backcolor, chosen_backcolor) {
    if(cur_selection_holder == elem) {
        cur_selection_candidate = cur_selection_holder;
        cur_selection_candidate.style["backgroundColor"] = selected_backcolor;
        cur_selection_holder = null;

    } else {
        if(cur_selection_holder != null) cur_selection_holder.style["backgroundColor"] = unselected_backcolor;
        cur_selection_holder = elem;
        cur_selection_holder.style["backgroundColor"] = chosen_backcolor;
        cur_selection_candidate = null;
    }
}
