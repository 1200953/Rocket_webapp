<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->
<#-- @ftlvariable name="rockets" type="java.util.Collection<rockets.model.Rocket>" -->

<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Rockets: a rocket information repository</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a rocket information repository - Launch Service Providers">
</head>

<body>
<div id="title_pane">
    <h3>Launch Service Providers Listing Page</h3>
</div>

<div>
<#if errorMsg?? && errorMsg?has_content>
    <li><h4 class="errorMsg">${errorMsg}</h4></li>
<#elseif lsps?? && rockets?has_content>
    <ul>
        <#list lsps as lsp>
            <li><a href="/lsp/${lsp.id}">${lsp.name}</a></li>
        </#list>

    </ul>
<#else>
    <p>No launch service provider yet in the system. <a href="/lsp/create">Create one</a> now!</p>
</#if>

</div>

</body>
</html>