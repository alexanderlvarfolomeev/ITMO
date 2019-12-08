<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Enter</div>
        <div class="body">
            <form method="post" id="enter-form" action="">
                <div class="field">
                    <div class="name">
                        <label for="login">Login</label>
                    </div>
                    <div class="value">
                        <input id="login" name="login"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" name="password" type="password"/>
                    </div>
                </div>
                <div class="error" style="display: none"></div>
                <div class="button-field">
                    <input type="submit" id="submit-button" value="Enter">
                </div>
            </form>
        </div>
    </div>

    <script>
        $("#submit-button").click(function () {
            form_ajax("enter-form", function (response) {
                if (response.error) {
                    $(".error").text(response.error).show();
                }
                $("#password").val("");
            }, "enter");
            return false;
        });
    </script>
</@c.page>
