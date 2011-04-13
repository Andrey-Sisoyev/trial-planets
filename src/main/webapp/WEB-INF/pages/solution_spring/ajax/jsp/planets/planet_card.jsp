<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="home.tries.planets.web.solution_spring.controller.command.PlanetCommand" %>
<%@ page import="home.utils.CRUD_Op" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    PlanetCommand cmd = (PlanetCommand) request.getAttribute("cmd");
    pageContext.setAttribute("form_disabled", cmd.getOperation().areFieldsReadOnly());
    // pageContext.setAttribute("buttons_disabled", cmd.getOperation() == CRUD_Op.READ);
%>

<div id="op_card_div">
    <form:form commandName="planetCommand" id="planet_crud_form">
        <form:hidden path="operation"/> <%-- made immutable actually, see setter in command object --%>
        <input type="hidden" name="committed" value="true"/>
        <input type="hidden" name="_operation" value="${cmd.operation}"/>
        <input type="hidden" name="_busin_obj_id" value="${cmd.planet.plID}"/>
        <table>
            <tr>
                <td>Planet ID:</td>
                <td><form:hidden path="planet.plID" />${cmd.planet.plID}</td>
                <td><form:errors path="planet.plID" /></td>
            </tr>
            <tr>
                <td>Planet Name:</td>
                <td><form:input path="planet.name" readonly="${form_disabled}"/></td>
                <td><form:errors path="planet.name" /></td>
            </tr>
            <tr>
                <td>Distance to Earth:</td>
                <td><form:input path="planet.distToEarth" readonly="${form_disabled}"/></td>
                <td><form:errors path="planet.distToEarth" /></td>
            </tr>
            <tr>
                <td>Diameter:</td>
                <td><form:input path="planet.diameter" readonly="${form_disabled}"/></td>
                <td><form:errors path="planet.diameter" /></td>
            </tr>
            <tr>
                <td>Discoverer name:</td>
                <td><form:input path="planet.discovererName" readonly="${form_disabled}"/></td>
                <td><form:errors path="planet.discovererName" /></td>
            </tr>
            <tr>
                <td>Has atmosphere:</td>
                <td><form:checkbox path="planet.atmosphere" disabled="${form_disabled}"/></td>
                <td><form:errors path="planet.atmosphere" /></td>
            </tr>
            <tr>
                <td colspan="3">
                    <div id="op_commitment" style="display: ${cmd.operation == "READ" ? "none" : "inline"}">
                        <input type="button" value="${cmd.operation}"  onclick="javascript:commitCRUDOp(this.form, loadPlanetsTable)"/>
                        <input type="button" value="Cancel" onclick="javascript:cancelOperation()"/>
                    </div>
                </td>
            </tr>
        </table>
    </form:form>
</div>

<script type="text/javascript">
    var op_commitments_buttons_locked = false;
</script>

