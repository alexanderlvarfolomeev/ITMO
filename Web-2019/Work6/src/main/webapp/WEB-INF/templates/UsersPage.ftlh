<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="user" type="ru.itmo.wp.model.domain.User" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="users datatable">
        <div class="caption">User</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Login</th>
                <#if (user?? && user.admin)>
                    <th class="user_admin">Admin</th>
                </#if>
            </tr>
            </thead>
            <tbody>
            <template>
                <tr>
                    <td class="user_id"></td>
                    <td class="user_login"></td>
                    <#if (user?? && user.admin)>
                        <td class="user_admin">
                            <div class="is_admin"></div>
                            <a href="javascript:void(0)" class="switch_admin"></a>
                        </td>
                    </#if>
                </tr>
            </template>
            <tr class="noData">
                <td colspan="3">
                    No data
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        $(function () {
            var $noData = $(".users .noData");
            $noData.find("td").text("Loading...");
            ajax({}, function (response) {
                $noData.hide();
                response["users"].forEach(function (user) {
                    var $tr = $($(".users template").prop("content")).clone();
                    $tr.find(".user_id").text(user["id"]);
                    $tr.find(".user_login").text(user["login"]);
                    change_admin_div($tr, user);
                    $tr.find("tr").attr("data-userId", user["id"]);
                    $(".users tbody").append($tr);
                });

                $(".users tr>td:not(.user_admin)").click(function () {
                    var $parent = $(this);
                    while (!$parent.is(".users tr")) {
                        $parent = $parent.parent();
                    }
                    ajax({userId: $parent.attr("data-userId")}, function (response) {
                        notify(response["user"].creationTime);
                    }, "findUser");
                });

                $(".users .switch_admin").click(function () {
                    var $tr = $(this).parent().parent();
                    ajax({
                        userId: $tr.attr("data-userId"),
                        admin: $(this).text() === "enable"
                    }, function (response) {
                        change_admin_div($tr, response);
                        if (response["user"] && !response["user"]["admin"]) {
                            $(".user_admin").remove();
                        }
                    }, "changeAdmin");
                });
            }, "findAll");
        });
    </script>
</@c.page>
