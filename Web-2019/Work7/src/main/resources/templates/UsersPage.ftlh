<#-- @ftlvariable name="users" type="ru.itmo.wp.domain.User[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <th>Created</th>
                <#if user??>
                    <th>Enable/Disable</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <#if users?size==0>
                <tr class="noData">
                    <td colspan="4">
                        No data
                    </td>
                </tr>
            <#else>
                <#list users as cur_user>
                    <tr>
                        <td class="id">${cur_user.id}</td>
                        <td class="login"><a href="/user/${cur_user.id}">${cur_user.login}</a></td>
                        <td class="creationTime">${cur_user.creationTime}</td>
                        <#if user??>
                            <td>
                                <form method="post" action="">
                                    <input type="hidden" name="action" value="users/change"/>
                                    <input type="hidden" name="id" value=${cur_user.id}>
                                    <input type="hidden" name="disabled"
                                           value=${cur_user.disabled?string("false", "true")}>
                                    <div class="button-field">
                                        <input type="submit" value=${cur_user.disabled?string("Enable", "Disable")}>
                                    </div>
                                </form>
                            </td>
                        </#if>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</@c.page>
