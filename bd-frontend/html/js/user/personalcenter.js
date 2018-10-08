$(document).ready(function () {



    getUserAsset();

    $("#myInformation").click(function(){
        getUserAsset();
    });

    $("#myOrder").click(function(){
        getOrderList();
    });
});



function getUserAsset () {
    var userModule = {
        address : userInfoJsonCookieData.address
    };
    $.ajax({
        url: webPathName + "/assetsMarket/getUserAsset.do",
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
                var data = res.data;
                if (data.length > 0) {
                    var htmlStr = "";
                    var totalPrice = 0;
                    for (var i = 0; i < data.length; i++) {
                        var vo = data[i];
                        var nick = vo.nick;
                        var icon = vo.icon;
                        var qty = vo.qty;
                        var price = vo.price;
                        if (price > 0) {
                            totalPrice += price * qty;
                        }
                        htmlStr += "<li>";
                        //htmlStr += "<span><img src='images/bdc_logo.png' alt='' /><b>" + nick + "</b></span><span>￥20002</span><span>1564564</span><span>0</span><span>123</span>";
                        //htmlStr += "<span><input type=\"button\" value=\"coin filed\" class=\"preson_green\"/><input type=\"button\" value=\"coin raising\" class=\"preson_red\"/></span>";
                        htmlStr += "<span><img src='images/bdc_logo.png' alt='' /><b>" + nick + "</b></span><span>" + qty + "</span>";
                        htmlStr += "<span><input type=\"button\" value=\"coin filed\" class=\"preson_green\"/><input type=\"button\" value=\"coin raising\" class=\"preson_red\"/></span>";
                        htmlStr += "</li>";
                    }
                    $("#totalPrice").html(totalPrice);
                    $("#zican_list").html(htmlStr);
                }


            } else {
                alert(res.desc);
            }

        }
    });
}

function getOrderList (pageNo) {
    if (pageNo == null) {
        pageNo = 0;
    }
    var userModule = {
        address : userInfoJsonCookieData.address,
        pageNum : pageNo
    };
    $.ajax({
        url: webPathName + "/order/list.do",
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
                var data = res.data.list;
                var totalNum = res.data.pages;
                var currentPage = res.data.pageNum;
                if (data.length > 0) {
                    var htmlStr = "";
                    var totalPrice = 0;
                    for (var i = 0; i < data.length; i++) {
                        var vo = data[i];
                        var createTime = vo.createTime;
                        var type = vo.type;
                        var typeName = "交易";
                        if (type == "1") {
                            typeName = "发送";
                        }
                        var fromName = vo.fromNickName;
                        var fromQty = vo.fromQty;
                        var toName = vo.toNickName;
                        var toQty = vo.toQty;
                        var toAddress = vo.toAddress;
                        var price = vo.price;
                        if (price > 0) {
                            totalPrice += price * qty;
                        }
                        htmlStr += "<li>";
                        htmlStr += "<span style='width: 15%'><b>" + createTime + "</b></span><span>" + typeName + "</span>";
                        htmlStr += "<span>" + fromName + "</span><span>" + fromQty + "</span>";
                        htmlStr += "<span>" + toName + "</span><span>" + toQty + "</span>";
                        htmlStr += "<span>" + toAddress + "</span>";
                        htmlStr += "</li>";
                    }

                    //分页
                    var pageHtml = "";
                    pageHtml += "<li><span>";
                    for (var i = 1; i <= totalNum; i++) {
                        pageHtml += "&nbsp;&nbsp;<a href='#' onclick='getOrderList("+ i +")'>" + i + "</a>&nbsp;&nbsp;&nbsp;";
                    }
                    pageHtml += "</span></li>";
                    $("#order_list").html(htmlStr + pageHtml);
                }


            } else {
                alert(res.desc);
            }

        }
    });
}