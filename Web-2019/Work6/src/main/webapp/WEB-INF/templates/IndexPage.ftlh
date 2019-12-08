<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <div class="noData">Hello, Codeforces!</div>
        <template class="article-template">
            <article>
                <div class="title"></div>
                <div class="information"></div>
                <div class="body"></div>
                <div class="footer">
                    <div class="left">
                        <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                        <span class="positive-score">0</span>
                        <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                    </div>
                    <div class="right">
                        <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                        <div class="date"></div>
                        <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                        <a href="#">0</a>
                    </div>
                </div>
            </article>
        </template>
    </div>

    <script>
        $(function () {
            $("aside section").click(function () {
                $(this).css("background-color", "red");
            });
        });

        $(function () {
            var $noData = $(".articles .noData");
            $noData.find("td").text("Loading...");
            ajax({}, function (response) {
                var users = response["users"];
                $noData.hide();
                response["articles"].forEach(function (article) {
                    var $tr = $($(".article-template").prop("content")).clone();
                    $tr.find(".title").text(article["title"]);
                    $tr.find(".information").text("By " + users[article["userId"] - 1]["login"]);
                    article["text"].match(/[^\r\n]+/g).forEach(function (p) {
                        $tr.find(".body").append("<p>" + p + "</p>\n");
                    });
                    $tr.find(".date").text(article["creationTime"]);
                    $(".articles").append($tr);
                });
            }, "findAll");
        })
    </script>
</@c.page>
