<#-- @ftlvariable name="req_user" type="ru.itmo.wp.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if req_user??>
        <div><b>Id:</b> ${req_user.id}</div>
        <div><b>Login:</b> ${req_user.login}</div>
        <div><b>Creation time:</b> ${req_user.creationTime}</div>
    <#else>
        <h1>No such user</h1>
    </#if>
</@c.page>
