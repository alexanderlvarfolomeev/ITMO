window.notify = function (message) {
    $.notify(message, {position: "bottom right"})
};
window.ajax = function (data, success, action) {
    data["action"] = action;
    $.ajax({
        dataType: "json",
        data: data,
        success: function (response) {
            success(response);
            if (response.redirect) {
                window.location.href = response.redirect;
            }
        },
        type: "POST"
    });
};

window.form_ajax = function (formId, success, action) {
    var data = $("#" + formId).serializeArray()
        .reduce(function (data, pair) {
            data[pair.name] = pair.value;
            return data
        }, {});
    ajax(data, success, action);
};

window.change_admin_div = function ($tr, adminMap) {
    if (adminMap["admin"] !== undefined) {
        if (adminMap["admin"]) {
            $tr.find(".user_admin .is_admin").text("admin");
            $tr.find(".user_admin .switch_admin").text("disable");
        } else {
            $tr.find(".user_admin .is_admin").text("-");
            $tr.find(".user_admin .switch_admin").text("enable");
        }
    } else {
        notify(adminMap["message"]);
    }
};
