function b() {
    //未登陆
    if(userInfoJsonCookieData == null || userInfoJsonCookieData.address == null) {
        alert("请先登录！");
    }else{
        var price = $("#buyPrice").val();
        var amount = $("#buyAmount").val();
        var asset = $("#asset").val();
        var priceAsset = $("#priceAsset").val();
        if (asset == priceAsset) {
            alert("请选择交易资产类型");
            return;
        }
        var paramsModule = {
            uid : userInfoJsonCookieData.address,
            asset : asset,
            priceAsset : priceAsset,
            price : price,
            amount : amount
        };
        $.ajax({
            url: webPathName + "/order/buy.do",
            type: "post",
            data: JSON.stringify(paramsModule),
            cache: false,
            dataType : "json",
            contentType : "application/json",
            error : function (e) {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                var resultMessage = res.msg;
                if (resultCode == "0") {
                    alert("下单成功！");
                } else {
                    alert(res.desc);
                }

            }
        });
    }
}

function s() {
    //未登陆
    if(userInfoJsonCookieData == null || userInfoJsonCookieData.address == null) {
        alert("请先登录！");
    } else{
        var price = $("#sellPrice").val();
        var amount = $("#sellAmount").val();
        var asset = $("#asset").val();
        var priceAsset = $("#priceAsset").val();
        if (asset == priceAsset) {
            alert("请选择交易资产类型");
            return;
        }
        var paramsModule = {
            // uid : "1Hn6C7PRYAxoBtaJy8bEDVbzN7Joma2H4TV42Z",
            uid : userInfoJsonCookieData.address,
            asset : asset,
            priceAsset : priceAsset,
            price : price,
            amount : amount
        };
        $.ajax({
            url: webPathName + "/order/sell.do",
            type: "post",
            data: JSON.stringify(paramsModule),
            cache: false,
            dataType : "json",
            contentType : "application/json",
            error : function (e) {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                var resultMessage = res.msg;
                if (resultCode == "0") {
                    alert("下单成功！");
                } else {
                    alert(res.desc);
                }

            }
        });
    }
}

function h(n) {
    //未登陆
    if(userInfoJsonCookieData == null || userInfoJsonCookieData.address == null) {
        $("#orders").html("<p style=\"text-align: center;color: #fff;line-height: 200px;font-size: 14px;\">" +
            "You have not sign in yet,please　" +
            "<b style=\"color: #fd5e5e;\" id=\"hLoginHtml\">Login</b>　or　" +
            "<b style=\"color: #46c35e;\">Sign Up</b> " +
            "</p>");
        $("#hLoginHtml").click(function(){
            window.location.href='login.html';
        })
    }
    //已登陆
    else{
        var uid = userInfoJsonCookieData.address;
        var asset = $("#asset").val();
        var priceAsset = $("#priceAsset").val();
        var paramsModule = {
            uid : uid,
            asset : asset,
            priceAsset : priceAsset,
            status : 1
        };
        $.ajax({
            url: webPathName + "/order/his.do",
            type: "post",
            data: JSON.stringify(paramsModule),
            cache: false,
            dataType : "json",
            contentType : "application/json",
            error : function (e) {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                if (resultCode == "0") {
                    var data = res.data;
                    if (data.length > 0) {
                        var htmlStr = "";
                        for (var i = 0; i < data.length; i++) {
                            var vo = data[i];
                            var id = vo.id;
                            var time = vo.createdOn;
                            var price = vo.price;
                            var amount = vo.originAmount;
                            var turnover = vo.turnOver;
                            var operation = vo.operation;
                            htmlStr += "<li id='"+id+"'>";
                            htmlStr += "<span>" + datetimeFormat(time) + "</span>";
                            htmlStr += "<span>"+price+"</span>";
                            htmlStr += "<span>"+amount+"</span>";
                            htmlStr += "<span>"+turnover+"</span>";
                            htmlStr += "<span>"+operation+"</span>";
                            htmlStr += "</li>";
                        }
                        $("#orders").html(htmlStr);
                    }else {
                        $("#orders").html("");
                    }
                } else {
                    alert(res.desc);
                }

            }
        });
    }
}

function r(n) {
    //未登陆
    if(userInfoJsonCookieData == null || userInfoJsonCookieData.address == null) {
        $("#orders").html("<p style=\"text-align: center;color: #fff;line-height: 200px;font-size: 14px;\">" +
            "You have not sign in yet,please　" +
            "<b style=\"color: #fd5e5e;\" id=\"rLoginHtml\">Login</b>　or　" +
            "<b style=\"color: #46c35e;\">Sign Up</b> " +
            "</p>");
        $("#rLoginHtml").click(function(){
            window.location.href='login.html';
        })
    }
    //已登陆
    else{
        var uid = userInfoJsonCookieData.address;
        var asset = $("#asset").val();
        var priceAsset = $("#priceAsset").val();
        var paramsModule = {
            uid : uid,
            asset : asset,
            priceAsset : priceAsset,
            status : 0
        };
        $.ajax({
            url: webPathName + "/order/listOrder.do",
            type: "post",
            data: JSON.stringify(paramsModule),
            cache: false,
            dataType : "json",
            contentType : "application/json",
            error : function (e) {
                alert("请求失败");
            },
            success : function (res) {
                var resultCode = res.result;
                if (resultCode == "0") {
                    var data = res.data;
                    if (data.length > 0) {
                        var htmlStr = "";
                        for (var i = 0; i < data.length; i++) {
                            var vo = data[i];
                            var id = vo.idStr;
                            var time = vo.createdOn;
                            var price = vo.price;
                            var amount = vo.amount;
                            var turnover = vo.turnOver;
                            htmlStr += "<li id='"+id+"'>";
                            htmlStr += "<span>" + datetimeFormat(time) + "</span>";
                            htmlStr += "<span>"+price+"</span>";
                            htmlStr += "<span>"+amount+"</span>";
                            htmlStr += "<span>"+turnover+"</span>";
                            htmlStr += "<span><input type=\"button\" value=\"撤销\" class=\"\" onclick=\"cancel('"+id+"')\"/></span>";
                            htmlStr += "</li>";
                        }
                        $("#orders").html(htmlStr);
                    }else {
                        $("#orders").html("");
                    }
                } else {
                    alert(res.desc);
                }

            }
        });
    }
}

function cancel(id) {
    var paramsModule = {
        id : id,
    };
    $.ajax({
        url: webPathName + "/order/cancel.do",
        type: "post",
        data: JSON.stringify(paramsModule),
        cache: false,
        dataType : "json",
        contentType : "application/json",
        error : function (e) {
            alert("请求失败");
        },
        success : function (res) {
            var resultCode = res.result;
            if (resultCode == "0") {
                alert("撤销成功");
                r();
            } else {
                alert(res.desc);
            }

        }
    });
}

function datetimeFormat(longTypeDate){
    var dateTypeDate = "";
    var date = new Date();
    date.setTime(longTypeDate);
    dateTypeDate += date.getFullYear(); //年
    dateTypeDate += "-" + getMonth(date); //月
    dateTypeDate += "-" + getDay(date); //日
    dateTypeDate += " " + getHours(date); //时
    dateTypeDate += ":" + getMinutes(date);  //分
    dateTypeDate += ":" + getSeconds(date);  //分
    return dateTypeDate;
}

function getMonth(date){
    var month = "";
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11
    if(month<10){
        month = "0" + month;
    }
    return month;
}
//返回01-30的日期
function getDay(date){
    var day = "";
    day = date.getDate();
    if(day<10){
        day = "0" + day;
    }
    return day;
}
//小时
function getHours(date){
    var hours = "";
    hours = date.getHours();
    if(hours<10){
        hours = "0" + hours;
    }
    return hours;
}
//分
function getMinutes(date){
    var minute = "";
    minute = date.getMinutes();
    if(minute<10){
        minute = "0" + minute;
    }
    return minute;
}
//秒
function getSeconds(date){
    var second = "";
    second = date.getSeconds();
    if(second<10){
        second = "0" + second;
    }
    return second;
}