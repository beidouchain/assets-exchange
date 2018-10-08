$(document).ready(function () {

    //登录
    $("#loginin").click(function() {
        var loginPrivateKey = $("#loginPrivateKey").val();
        var loginAddress = $("#loginAddress").val();
        if (loginAddress.length == 0) {
            alert("请填写用户名");
            return;
        }
        if (loginPrivateKey.length == 0) {
            alert("请填写密码");
            return;
        }
        var userModule = {
            address : loginAddress,
            privateKey : loginPrivateKey
        };
        $.ajaxSettings.crossDomain = true;
        $.ajax({
            url: webPathName + "/user/login.do",
            type: "post",
            data: JSON.stringify(userModule),
            cache: false,
            dataType : "json",
            responseType:'application/json',
            contentType : "application/json",
            error : function (e) {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                if (resultCode == "0") {
                    alert("登录成功");
                    $.cookie('bdc_userinfo', JSON.stringify(res), { expires: 2 });
                    location.href = "personalcenter.html";
                } else {
                    alert(res.desc);
                }

            }
        });
    });

    function check_email(email) {
        var reg = /\w+[@]{1}\w+[.]\w+/;
        if (!reg.test(email)){
            return false;
        } else {
            return true;
        }
    }
})



