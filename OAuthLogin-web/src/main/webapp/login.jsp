<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <meta charset="UTF-8">
</head>
<body>
    <%--@elvariable id="USER" type="cz.literak.demo.oauth.model.entity.User"--%>
    <c:if test="${USER == null or not USER.isRegistered('TW')}">
        <p><a href="login/twitter">Login with Twitter</a></p>
    </c:if>
    <c:if test="${USER == null or not USER.isRegistered('GG')}">
        <p><a href="login/google">Login with Google</a></p>
    </c:if>
    <c:if test="${USER == null or not USER.isRegistered('FB')}">
        <p><a href="login/facebook">Login with Facebook</a></p>
    </c:if>
    <script src='ga.js'></script>

    <form method="post" action="api/find-user">
        <input type="text" name="email" value="your@email.tld">
        <input type="button" id="submit" value="Find me"/>
    </form>
    <div id="logins"></div>
    <%--http://www.lennu.net/2012/06/25/jquery-ajax-example-with-json-response/--%>
    <%--http://www.programming-free.com/2012/08/ajax-with-jsp-and-servlet-using-jquery.html--%>
    <%--http://www.programming-free.com/2012/09/ajax-with-servlets-using-jquery-and-json.html--%>
    <script src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#submit').click(function (event) {
                var email = $('#email').val();
                $.get('api/find-user', {email: email}, function (responseText) {
                    $('#logins').text(responseText);
                });
            });
        });
        /*
    $(document).ready(function(){
      $(':submit').live('click', function() {
          var button = $(this).val();
          $.ajax({
              url: 'api/find-user',
              data: 'button=' + $('#email').val(),
              dataType: 'json',
              success: function(data) // Variable data contains the data we get from serverside
              {
                  $('#logins').html('');
                  for (var i in data.providers) {
                      $('#logins').append(i + ' ');
                  }
              }
          });
          return false; // keeps the page from not refreshing
      });
    });
*/
    </script>
<%--
    <script>
        success: function(data){
           var json = $.parseJSON(data); // create an object with the key of the array
           alert(json.html); // where html is the key of array that you want, $response['html'] = "<a>something..</a>";
        },
        error: function(data){
           var json = $.parseJSON(data);
           alert(json.error);
        } ...
    $.ajax({
        url: "main.php",
        type: "POST",
        dataType: "json",
        data: {"action": "loadall", "id": id},
        success: function(data){
            console.log(data);
        },
        error: function(error){
             console.log("Error:");
             console.log(error);
        }
    });
    </script>
--%>
</body>
</html>
