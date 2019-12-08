<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="comment" type="ru.itmo.wp.domain.Comment" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if post??>
        <@c.post post=post/>
        <#if user??>
        <div class="comment-creation-form">
            <form method="post" action="">
                <input type="hidden" name="action" value="writeComment"/>
                <div class="field">
                    <div class="name">
                        <label for="text">Write Comment:</label>
                    </div>
                    <div class="value">
                        <textarea id="text" name="text">${comment.text!}</textarea>
                    </div>
                    <@c.error "comment.text"/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Send">
                </div>
            </form>
        </div>
        </#if>
        <#list post.comments as comment>
            <@c.comment comment=comment/>
        </#list>
    <#else>
        No such post
    </#if>
</@c.page>
