$(document).ready(function () {
    $("#signupSub").click(function() {
        var isChecked = $('#agreeCheck').attr('checked');
        if (!isChecked) {
            alert("请阅读用户使用协议");
            return;
        }
        var pwd = $("#sig_password").val();
        var repwd = $("#sig_repassword").val();
        var email = $("#sig_email").val();
        var name = $("#sig_userName").val();
        if (name.length == 0) {
            alert("请填写用户名");
            return;
        }
        if (pwd.length == 0) {
            alert("请填写密码");
            return;
        }
        if (pwd != repwd) {
            alert("密码请填写一致");
            return;
        }
        if (email.length == 0) {
            alert("请填写邮箱");
            return;
        }
        if (!check_email(email)) {
            alert("请输入正确的email");
            return;
        }
        var userModule = {
            pwd : pwd,
            name : name,
            email : email
        };
        $.ajax({
            url: webPathName + "/user/regist.do",
            type: "post",
            data: userModule,
            cache: false,
            dataType : "json",
            error : function () {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                if (resultCode == "0") {
                    alert("注册成功");
                } else {
                    alert(res.desc);
                }

            }
        });
    });

    //登录
    $("#loginSub").click(function() {
        var pwd = $("#pwd").val();
        var name = $("#nick").val();
        if (name.length == 0) {
            alert("请填写用户名");
            return;
        }
        if (pwd.length == 0) {
            alert("请填写密码");
            return;
        }
        var userModule = {
            pwd : pwd,
            name : name
        };
        $.ajax({
            url: webPathName + "/user/login.do",
            type: "post",
            data: JSON.stringify(userModule),
            cache: false,
            dataType : "json",
            contentType : "application/json",
            error : function (e) {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                if (resultCode == "0") {
                    alert("登录成功");
                    $.cookie('bdc_userinfo', JSON.stringify(res), { expires: 2 });
                    location.href = "main.html";
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
<!--
    $("#fileuploader").uploadFile({
        url:webPathName + "/ocr/save.do",
        fileName:"myfile"
    });
-->
})



