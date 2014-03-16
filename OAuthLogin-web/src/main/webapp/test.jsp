<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>AJAX calls using Jquery in Servlet</title>
    <script src="http://code.jquery.com/jquery-latest.js" type="text/javascript">
    </script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#submit').click(function (event) {
                var username = $('#user').val();
                $.get('api/find-user', {user: username}, function (responseText) {
                    $('#welcometext').text(responseText);
                });
            });
        });
    </script>
    <link rel="stylesheet" type="text/css" href="jar/styles/layout.css"/>
</head>
<body>
<form id="form1">
    <h1>AJAX Demo using Jquery in JSP and Servlet</h1>
    Enter your Name:
    <input type="text" id="user"/>
    <input type="button" id="submit" value="Ajax Submit"/>
    <br/>

    <div id="welcometext">
    </div>
</form>
</body>
</html>
