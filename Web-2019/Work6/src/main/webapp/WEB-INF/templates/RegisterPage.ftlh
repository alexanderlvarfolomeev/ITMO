<#-- @ftlvariable name="login" type="java.lang.String" -->
<#-- @ftlvariable name="email" type="java.lang.String" -->
<#-- @ftlvariable name="password" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Register</div>
        <div class="body">
            <form method="post" id="register-form" action="">
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
                        <label for="email">Email</label>
                    </div>
                    <div class="value">
                        <input id="email" name="email"/>
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
                    <input type="submit" id="submit-button" value="Login">
                </div>
            </form>
        </div>
    </div>

    <script>
        $("#submit-button").click(function () {
            form_ajax("register-form", function (response) {
                if (response.error) {
                    $(".error").text(response.error).show();
                }
                $("#password").val("");
            }, "register");
            return false;
        });
    </script>
</@c.page>
