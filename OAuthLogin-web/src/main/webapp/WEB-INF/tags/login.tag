<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>
    <span class="caption">Demo:</span>
    <%--@elvariable id="USER" type="cz.literak.demo.oauth.model.entity.User"--%>
    <c:if test="${not empty USER}">
        Logged as ${USER.firstName} ${USER.lastName}, <a href="logout">Logout</a>
        <c:if test="${not USER.areRegistered('TW,FB,GG')}">
            <a href="login.jsp" class="popup" data-width="600" data-height="400">Improve Login</a>
        </c:if>
    </c:if>
    <c:if test="${empty USER}">
        <a href="login.jsp" class="popup" data-width="600" data-height="400">Login</a>
    </c:if>
<%--
    <script>
        $("a.popup").click(function(event){
            event.preventDefault();
            var link = this;
            var href = link.getAttribute("href");
            var height = link.getAttribute("data-height");
            var width = link.getAttribute("data-width");
            window.open (href, "OAUTHLOGIN", "height=" + height +",width=" + width + "");
        });
    </script>
    <script type="text/javascript">
        (function () {
            var dsq = document.createElement('script');
            dsq.innerHTML = '';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
--%>
</p>
