<#import "common.ftl" as c/>

<@c.page>
    <div class="usertable">
        <div class="caption">Users</div>
        <table>
            <thead>
            <tr>
                <th>№</th>
                <th>Id</th>
                <th>Handle</th>
                <th>Name</th>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <td>${u?index + 1}</td>
                    <td>${u.id}</td>
                    <td>${u.handle}</td>
                    <td><#if user?? && u.id == user.id>*</#if><@c.userlink user=u nameOnly=false/></td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>