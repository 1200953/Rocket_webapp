<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="rocket" type="rockets.model.Rocket" -->
<#-- @ftlvariable name="participants" type="java.util.Set<flymetomars.model.Person>" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Rockets: a rocket information repository</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a rocket information repository - Launch Service Provider">
</head>

<body>
<div id="title_pane">
    <h3>Launch Service Provider Details Page</h3>
</div>

<div>
<#if errorMsg?? && errorMsg?has_content>
    <li><h4 class="errorMsg">${errorMsg}</h4></li>
<#else>
    <p>Launch Service Provider details:</p>
    <ul>
        <li>Name: ${lsp.name}</li>
        <li>Year Founded: ${lsp.yearFounded}</li>
        <li>Country: ${lsp.country}</li>
    </ul>
</#if>

</div>

</body>
</html>