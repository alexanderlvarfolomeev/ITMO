<#-- @ftlvariable name="uri" type="java.lang.String" -->
<#-- @ftlvariable name="menuItems" type="java.util.Map.Entry<String, String>[]" -->
<#-- @ftlvariable name="posts" type="ru.itmo.tpl.model.Post[]" -->
<#macro header>
    <header>
        <a href="/"><img src="/img/logo.png" alt="Codeforces" title="Codeforces"/></a>
        <div class="languages">
            <a href="#"><img src="/img/gb.png" alt="In English" title="In English"/></a>
            <a href="#"><img src="/img/ru.png" alt="In Russian" title="In Russian"/></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user=user nameOnly=true/>
                |
                <a href="/index">Logout</a>
            <#else>
                <a href="#">Enter</a>
                |
                <a href="#">Register</a>
            </#if>
        </div>
        <nav>
            <ul>
                <#list menuItems as item>
                    <#if item.key == uri>
                        <li class="underlinedItem"><a href=<@logged_user_href item.key/>>${item.value}</a></li>
                    <#else>
                        <li><a href=<@logged_user_href item.key/>>${item.value}</a></li>
                    </#if>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts as post>
            <section>
                <div class="header">
                    Post #${post.id}
                </div>
                <div class="body">
                    <@post_text post=post view_all=false/>
                </div>
                <div class="footer">
                    <a href=<@logged_user_href href="/post?post_id=${post.id}"/>>View all</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        <a href="#">Codeforces</a> &copy; 2010-2019 by Mike Mirzayanov
    </footer>
</#macro>

<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" type="text/css" href="/css/normalize.css">
        <link rel="stylesheet" type="text/css" href="/css/style.css">
        <link rel="icon" href="/favicon.ico" type="image/x-icon"/>
        <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro userlink user nameOnly>
    <#assign attributes="style=color:" + user.color>
    <a class="username"
       href=<@logged_user_href "/user?handle=" + user.handle/> ${nameOnly?then("", attributes)}>${user.name}</a>
</#macro>

<#macro logged_user_href href>
    <#if user??>
        <#if href?contains("?")>
            ${href+"&logged_user_id="+user.id}
        <#else>
            ${href+"?logged_user_id="+user.id}
        </#if>
    <#else>
        ${href}
    </#if>
</#macro>

<#function findBy items key id>
    <#list items as item>
        <#if item[key]==id>
            <#assign subseq = {"curr" : item}>
            <#if !item?is_first>
                <#assign subseq += {"pre" : items[item?index - 1]}>
            </#if>
            <#if !item?is_last>
                <#assign subseq += {"post" : items[item?index + 1]}>
            </#if>
            <#return subseq/>
        </#if>
    </#list>
</#function>

<#macro show_posts view_all=false user_id=false post_id=false>
    <#list posts as post>
        <#if (user_id?is_boolean && post_id?is_boolean ||
        user_id?is_number && post.user_id == user_id ||
        post_id?is_number && post.id == post_id)>
            <article>
                <div class="title">${post.title}</div>
                <#assign user_author=findBy(users, "id", post.user_id).curr!/>
                <div class="information">By <@userlink user=user_author nameOnly=false/></div>
                <div class="body">
                    <@post_text post=post view_all=view_all/>
                </div>
                <div class="footer">
                    <div class="left">
                        <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                        <span class="positive-score">+173</span>
                        <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                    </div>
                    <div class="right">
                        <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                        2 days ago
                        <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                        <a href="#">68</a>
                    </div>
                </div>
            </article>
        </#if>
    </#list>
</#macro>

<#macro post_text post view_all>
    <#if view_all>
        ${post.text}
    <#else>
        ${post.text[0..*250] + (post.text?length gt 250)?then("...", "")}
    </#if>
</#macro>