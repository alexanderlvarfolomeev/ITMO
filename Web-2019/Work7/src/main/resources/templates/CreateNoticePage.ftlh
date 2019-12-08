<#-- @ftlvariable name="creationForm" type="ru.itmo.wp.form.NoticeCredentials" -->
<#import "/spring.ftl" as spring>
<#import "commons.ftlh" as c>

<@c.page>
    <div class="notice-creation form-box">
        <div class="header">Register</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="createNotice"/>
                <div class="field">
                    <div class="name">
                        <label for="content">Login</label>
                    </div>
                    <div class="value">
                        <textarea id="content" name="content">${creationForm.content!}</textarea>
                    </div>
                    <@c.error "creationForm.content"/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Add">
                </div>
            </form>
        </div>
    </div>
</@c.page>
