<#import "commons.ftlh" as c>

<@c.page>
    <div class="article-creation">
        <form id="article-form" method="post" action="">
            <div class="title-block">
                <label for="title">Title:   </label>
                <input id="title" name="title"/>
            </div>
            <div class="text-block">
                <label for="text">Text:</label>
                <textarea id="text" name="text" type="textarea" rows="20"></textarea>
            </div>
            <div class="hidden-flag-block">
                <label for="hidden-flag">Make hidden initially</label>
                <input id="hidden-flag" name="hidden" type="checkbox"/>
            </div>
            <div class="error" style="display: none"></div>
            <div class="button-field">
                <input type="submit" id="submit-button" value="Create">
            </div>
        </form>
    </div>

    <script>
        $("#submit-button").click(function () {
            form_ajax("article-form", function (response) {
                if (response.error) {
                    $(".error").text(response.error).show();
                } else {
                    notify("Article was saved");
                }
                $("#text").val("");
            }, "create");
            return false;
        });
    </script>
</@c.page>
