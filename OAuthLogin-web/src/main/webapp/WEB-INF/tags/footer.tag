<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>
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
<script src='ga.js'></script>
