<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<!DOCTYPE html>
<html>
  <head>
    <title>OAuth demo</title>
      <meta charset="UTF-8">
      <style type="text/css">
          .caption {
              font-style: italic;
          }
      </style>
  </head>
  <body>
    <h1>OAuthLogin JEE skeleton</h1>

    <h:login/>

    <p>
        The purpose of this site is to present a skeleton of JEE application that can accept external login
        using OAuth protocol (Google, Facebook, Twitter). Users are persisted in a relational database with JPA.
        The application uses <a href="https://github.com/hhru/subscribe">Subscribe library for OAuth</a>
        and <a href="https://github.com/ralfstx/minimal-json">minimal-json for JSON parsing</a>.
    </p>

    <p>
        Source codes are released under MIT license. You can download preliminary version of
        <a href="https://github.com/literakl/OAuthLogin">OAuthLogin on GitHub</a>. The skeleton
        consists of two Maven modules: OAuthLogin-ejb that performs OAuth authentication and database
        operations (JPA) and OAuthLogin-web that contains frontend (jsp as view, servlets as controller).
        I use following libraries:
    </p>

    <ul>
        <li><a href="https://github.com/hhru/subscribe">Subscribe</a> (successor of popular Scribe)</li>
        <li><a href="https://github.com/ralfstx/minimal-json">minimal-json</a> nice library for parsing JSON</li>
        <li><a href="https://bitbucket.org/nimbusds/nimbus-jose-jwt/wiki/Home">nimbus-jose-jwt</a> easy to use library for parsing JWT tokens</li>
        <li><a href="http://logging.apache.org/log4j/2.x/">log4j2</a> famous logging framework</li>
    </ul>

    <p>
        Discussion: <a href="discuss.jsp#disqus_thread">Discussion</a>
    </p>

    <h3>Changelog</h3>

    <ul>
        <li>19.3.2014 - Fixed build after package refactoring</li>
        <li>16.3.2014 - Mavenized, shared on GitHub</li>
        <li>6.3.2014 - Refactor common code to custom JSP tag</li>
        <li>6.3.2014 - Discussion added</li>
        <li>2.3.2014 - Beta version: deleted existing users for another test round (sorry)</li>
        <li>2.3.2014 - Switched to Subscribe from Scribe, update email from other provider</li>
        <li>28.2.2014 - Popup window for login</li>
        <li>26.2.2014 - Google scope fixed</li>
        <li>23.2.2014 - Alfa version released</li>
    </ul>

    <h3>Interesting StackOverflow questions</h3>

    <ul>
        <li><a href="http://stackoverflow.com/q/21068291/1639556">Servlet encoding issues with WELD</a></li>
        <li><a href="http://stackoverflow.com/q/21122979/1639556">OAuth concepts</a></li>
        <li><a href="http://stackoverflow.com/q/21361818/1639556">Parsing Facebook OAuth access token (unansweared)</a></li>
        <li><a href="http://stackoverflow.com/q/21647947/1639556">Lazy loading in JPA</a></li>
        <li><a href="http://stackoverflow.com/q/22028399/1639556">Passing properties to Log4J2 configuration and default values</a></li>
        <li><a href="http://stackoverflow.com/q/22079026/1639556">JQuery popup closing</a></li>
        <li><a href="http://stackoverflow.com/q/22127126/1639556">GitHub collaboration, fork, push, pull</a></li>
    </ul>

    <script type="text/javascript">
    /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
    var disqus_shortname = 'literak'; // required: replace example with your forum shortname

    /* * * DON'T EDIT BELOW THIS LINE * * */
    (function () {
        var s = document.createElement('script'); s.async = true;
        s.type = 'text/javascript';
        s.src = '//' + disqus_shortname + '.disqus.com/count.js';
        (document.getElementsByTagName('HEAD')[0] || document.getElementsByTagName('BODY')[0]).appendChild(s);
    }());
    </script>

    <h:footer/>
  </body>
</html>
