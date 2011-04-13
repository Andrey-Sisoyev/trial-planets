<%@ page contentType="text/html;charset=UTF-8" language="java" %>
Это версия 1.1. Решение с RichFaces <b><a href="./jsf/">тут</a></b> (увы не работает <img src="./resources/tired.gif"/>).
<h3>Table of planets</h3>
<input type="button" id="reload_pl_tab" value="Reload" />
<div id="loading_planetsList"></div>
<div id="planetsList"></div>

<script type="text/javascript">
    $(document).ready(function() {
        loadPlanetsTable();
        $('#reload_pl_tab').bind( "click", function(){ loadPlanetsTable(); } );
    });
</script>
