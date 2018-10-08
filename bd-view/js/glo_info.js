var webPathName = "/bd-web-2.0.2.RELEASE";
$(document).ready(function () {
    var userInfo = $.cookie('bdc_userinfo');
    if (userInfo != null) {
        var userInfoJson = JSON.parse(userInfo);
        $("#showLoginName").html(userInfoJson.data.name);
    }

    $('.message .close')
        .on('click', function() {
            $(this)
                .closest('.message')
                .transition('fade')
            ;
        });
})





