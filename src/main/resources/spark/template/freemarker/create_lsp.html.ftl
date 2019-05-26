<#-- @ftlvariable name="description" type="java.lang.String" -->
<#-- @ftlvariable name="location" type="java.lang.String" -->
<#-- @ftlvariable name="timeValue" type="java.lang.String" -->
<#-- @ftlvariable name="missionName" type="java.lang.String" -->
<#-- @ftlvariable name="errorMsg" type="java.lang.String" -->


<!doctype html public "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Rockets: a rocket information repository</title>

    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">

    <meta name="description" content="Rockets: a rocket information repository - Create Launch Service Provider">
</head>

<body>

<div id="title_pane">
    <h3>Launch Service Provider Creation</h3>
</div>

<p>${errorMsg!""}</p>

<div>
    <p>* Fields are required.</p>
</div>
<form name="create_lsp" action="/lsp/create" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div><p>Mission Details</p></div>
        <ol>
            <li>
                <label for="lspName" class="bold">Launch Service Provider Name:*</label>
                <input id="lspName" name="lspName" type="text" value="${lspName!""}">
            </li>
            <li>
                <label for="yearFounded" class="bold">Year Founded:*</label>
                <input id="yearFounded" name="yearFounded" type="text" value="${yearFounded!""}">
            </li>
            <li>
                <label for="country" class="bold">Country:*</label>
                <input id="country" name="country" type="text" value="${country!""}">
            </li>
        </ol>
    </div>

    <#if errorMsg?? && errorMsg?has_content>
        <div id="error">
            <p>Error: ${errorMsg}</p>
        </div>
    </#if>
    <div id="buttonwrapper">
        <button type="submit">Create New Launch Service Provider</button>
        <a href="/">Cancel</a>
    </div>
</form>

</body>