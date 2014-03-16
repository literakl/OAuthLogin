<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<html>
<head>
    <title>You are logged</title>
</head>
<body>
    <script>
        window.opener.location.reload(false);
        if (window.name == "OAUTHLOGIN") {
            window.close();
        }
    </script>
</body>
</html>
