<%@ page import="home.utils.CRUD_Op" %>

<span style="font-size: 9px;"><i><b>CRUD PANEL</b></i></span><br>
<div id="crud_buttons">
    <input type="button" value="New"                    onclick="javascript:viewCrud('<%= CRUD_Op.CREATE.toString() %>', 'new', getBusnObjName());"/>
    <input type="button" value="Info"   disabled="true" onclick="javascript:viewCrud('<%= CRUD_Op.READ.toString() %>'  , getBusnObjId(), getBusnObjName());"/>
    <input type="button" value="Update" disabled="true" onclick="javascript:viewCrud('<%= CRUD_Op.UPDATE.toString() %>', getBusnObjId(), getBusnObjName());"/>
    <input type="button" value="Delete" disabled="true" onclick="javascript:viewCrud('<%= CRUD_Op.DELETE.toString() %>', getBusnObjId(), getBusnObjName());"/>
</div>
<div id="loading_crudOp"></div>
<div id="crud_operation"></div>
<script>
    var crud_buttons_locked = false;
    function getBusnObjId() { return getSelectedBusinObjId(); }
    function getBusnObjName() { return "planet"; }
</script>


