<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="posts" type="ru.itmo.tpl.model.Post[]" -->
<#import "common.ftl" as c/>

<#function userPost post>
    <#return post.user_id == user_id/>
</#function>

<@c.page>
    <#if handle??>
        <#assign viewedUser=c.findBy(users, "handle", handle)!/>
    <#else>
        <#assign viewedUser=c.findBy(users, "id", user_id)!/>
    </#if>
    <#if viewedUser.curr??>
        <div class="user-box">
            <span class="user">
                <h1><@c.userlink user=viewedUser.curr nameOnly=false/></h1>
                <ul>
                    <li>id: ${viewedUser.curr.id}
                    <li>handle: ${viewedUser.curr.handle}
                    <li>name: ${viewedUser.curr.name}
                    <li><a href=<@c.logged_user_href "/posts?user_id=" + viewedUser.curr.id/>>
                            User posts: ${posts?filter(x -> x.user_id == viewedUser.curr.id)?size}
                        </a>
                </ul>
            </span>
            <span class="image">
                <img src="/img/user.jpg" alt="Avatar">
            </span>
        </div>
        <div class="arrows">
            <#if viewedUser.pre??>
                <a href=<@c.logged_user_href "/user?handle=" + viewedUser.pre.handle/>>←</a>
            <#else>
                <span>←</span>
            </#if>
            <#if viewedUser.post??>
                <a href=<@c.logged_user_href "/user?handle=" + viewedUser.post.handle/>>→</a>
            <#else>
                <span>→</span>
            </#if>
        </div>
    <#else>
        <h1>No such user</h1>
    </#if>
</@c.page>

<#function userPost user_id post>
    <#return post.user_id == user_id/>
</#function>
