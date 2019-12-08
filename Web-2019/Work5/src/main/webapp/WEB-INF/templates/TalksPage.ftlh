<#-- @ftlvariable name="talks" type="ru.itmo.wp.model.domain.Talk[]" -->
<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Send Message</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="send"/>
                <div class="message-form">
                    <div class="select-user-block">
                        <div>
                            <label for="select-list">Choose addressee:</label>
                        </div>
                        <div>
                            <select id="select-list" required name="targetId">
                                <#list users as user>
                                    <option value="${user.id}">${user.login}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="text-block">
                        <label for="text"></label>
                        <textarea id="text" name="text" type="textarea" rows="4"></textarea>
                    </div>
                </div>
                <div class="button-field">
                    <input type="submit" value="Send">
                </div>
            </form>
        </div>
    </div>
    <div class="datatable">
        <div class="caption">Messages</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>From</th>
                <th>To</th>
                <th>Text</th>
                <th>Time</th>
            </tr>
            </thead>
            <tbody>
            <#list talks as talk>
                <tr>
                    <td>${talk.id}</td>
                    <td>${c.findBy(users, "id", talk.sourceUserId).login}</td>
                    <td>${c.findBy(users, "id", talk.targetUserId).login}</td>
                    <td>${talk.text}</td>
                    <td>${talk.creationTime}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
