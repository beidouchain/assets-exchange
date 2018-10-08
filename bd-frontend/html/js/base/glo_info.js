var webPathName = "http://120.27.23.110:9090/bd-web-2.0.2.RELEASE";
//var webPathName = "http://127.0.0.1:8080/bd-web-2.0.2.RELEASE";
//120.27.23.110 192.168.23.126
var userInfoJsonCookie;
var userInfoJsonCookieData;
//默认交易资产
var defaultAssetCoinData;
$(document).ready(function () {
    var userInfo = $.cookie('bdc_userinfo');
    if (userInfo != null) {
        userInfoJsonCookie = JSON.parse(userInfo);
        userInfoJsonCookieData = userInfoJsonCookie.data;
        //$("#showLoginName").html(userInfoJson.data.name);

        var showUserHtml = "name：" + userInfoJsonCookieData.name;
        showUserHtml += " code:" + userInfoJsonCookieData.id;
        showUserHtml += " card id:" + userInfoJsonCookieData.cardId;
        $("#userInfoShow").html(showUserHtml);
        $("#showUserId").html("用户：" + userInfoJsonCookieData.name );
        getAssetForType();
    }
})
function goDeal() {
    location.href = "deal.html";
}

function goIndex() {
    location.href = "index.html";
}

function loginOut () {
    $.cookie('bdc_userinfo', '', { expires: -1 });
    location.href = "login.html";
}

function getAssetForType () {
    var userModule = {
        type : 3
    };
    $.ajax({
        url: webPathName + "/assetsMarket/getAssetCategoryForType.do",
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
                defaultAssetCoinData = res.data;
                //alert(defaultAssetCoinData.name + "-" + defaultAssetCoinData.nick);
            } else {
                alert(res.desc);
            }

        }
    });
}


