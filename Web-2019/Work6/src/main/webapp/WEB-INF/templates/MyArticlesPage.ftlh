<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles datatable">
        <div class="caption">Your article</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Title</th>
                <th>Hide/Show</th>
            </tr>
            </thead>
            <tbody>
            <template>
            </template>
            <#list articles as article>
                <tr>
                    <td class="article_id">${article.id}</td>
                    <td class="article_title">${article.title}</td>
                    <td class="hide_show_button">
                        <input type="submit" id="change-button" value="${article.hidden?then("Show", "Hide")}"
                               data-articleId="${article.id}">
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <script>
        $(".articles #change-button").click(function () {
            var button = $(this);
            ajax({
                articleId: button.attr("data-articleId"),
                hidden: button.val() === "Hide"
            }, function (response) {
                if(response.hidden === undefined) {
                    return;
                }
                button.val(response.hidden ? "Show" : "Hide")
            }, "changeHidden");
            return false;
        });
    </script>
</@c.page>
