<head>
    <title>Planets Web app</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js" type="text/javascript"></script>
    <!-- в связи с проблемами AJAX -->
</head>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<p class="error">Operation "${operation}" failed! Planet ID: #${busin_obj_id}</p>
<p class="error">${failure_msg}</p>