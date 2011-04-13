<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<p>Snapshot date/time: <c:out value="${now}"/></p>
<p>Records count: <c:out value="${palnets_count}"/></p>
<table>
    <tr class="tab_header">
        <td class="cell_not_last"><p class="tab_header">Planet name</p></td>
        <td class="cell_not_last"><p class="tab_header">Distance to earth</p></td>
        <td class="cell_not_last"><p class="tab_header">Diameter</p></td>
        <td class="cell_not_last"><p class="tab_header">Discoverer</p></td>
        <td><p class="tab_header">Has atmosphere</p></td>
    </tr>

<c:forEach items="${planets_list}" var="entry">
    <tr data-busin_obj_id="${entry.plID}" onMouseOver="javascript:highlightRow(this)" onMouseOut="javascript:uNhighlightRow(this)" onclick="javascript:selectRow(this)">
        <td class="cell_not_last">${entry.name}</td>
        <td class="cell_not_last">${entry.distToEarth}</td>
        <td class="cell_not_last">${entry.diameter}</td>
        <td class="cell_not_last">${entry.discovererName}</td>
        <td>${entry.atmosphere}</td>
    </tr>
</c:forEach>
</table>
<script type="text/javascript">
    var cur_selection_holder = null;
    var cur_selection_candidate = null;
</script>