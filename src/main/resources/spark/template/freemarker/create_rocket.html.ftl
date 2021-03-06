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

    <meta name="description" content="Rockets: a rocket information repository - Create Rocket">
</head>

<body>

<div id="title_pane">
    <h3>Rocket Creation</h3>
</div>

<p>${errorMsg!""}</p>

<div>
    <p>* Fields are required.</p>
</div>
<form name="create_rocket" action="/rocket/create" method="POST">
    <div id="admin_left_pane" class="fieldset_without_border">
        <div><p>Rocket Details</p></div>
        <ol>
            <li>
                <label for="rocketName" class="bold">Rocket Name:*</label>
                <input id="rocketName" name="rocketName" type="text" value="${rocketName!""}">
            </li>
            <li>
                <label for="country" class="bold">Country:*</label>
                <input id="country" name="country" type="text" value="${country!""}">
            </li>
            <li>
                <label for="manufacturer" class="bold">Manufacturer:*</label>
                <input id="manufacturer" name="manufacturer" type="text" value="${manufacturer!""}">
            </li>
        </ol>
    </div>

    <#if errorMsg?? && errorMsg?has_content>
        <div id="error">
            <p>Error: ${errorMsg}</p>
        </div>
    </#if>
    <div id="buttonwrapper">
        <button type="submit">Create New Rocket</button>
        <a href="/">Cancel</a>
    </div>
</form>

</body>