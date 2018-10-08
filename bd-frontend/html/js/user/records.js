$(document).ready(function () {




    $("#cashRecords").click(function(){
        getCashRecords();
    });
    $("#coinRecords").click(function(){
        getCoinRecords();
    });
});
//coinRecords
function getCoinRecords (pageNo) {
    if (pageNo == null) {
        pageNo = 0;
    }
    var userModule = {
        address: userInfoJsonCookieData.address,
        pageNum: pageNo,
        type: "1"
    };
    $.ajax({
        url: webPathName + "/order/list.do",
        type: "post",
        data: JSON.stringify(userModule),
        cache: false,
        dataType: "json",
        contentType: "application/json",
        error: function (e) {
            alert("请求失败");
        },
        success: function (res) {
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
                        pageHtml += "&nbsp;&nbsp;<a href='#' onclick='getCoinRecords(" + i + ")'>" + i + "</a>&nbsp;&nbsp;&nbsp;";
                    }
                    pageHtml += "</span></li>";
                    $("#coinRecords_list").html(htmlStr + pageHtml);
                }


            } else {
                alert(res.desc);
            }

        }
    });
}

function getCashRecords (pageNo) {
    if (pageNo == null) {
        pageNo = 0;
    }
    var userModule = {
        address : userInfoJsonCookieData.address,
        pageNum : pageNo,
        type : "2"
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
                        htmlStr += "<span style='width: 15%'><b>" + createTime + "</b></span>";
                        htmlStr += "<span>" + toQty + "</span>";
                        htmlStr += "</li>";
                    }

                    //分页
                    var pageHtml = "";
                    pageHtml += "<li><span>";
                    for (var i = 1; i <= totalNum; i++) {
                        pageHtml += "&nbsp;&nbsp;<a href='#' onclick='getCashRecords("+ i +")'>" + i + "</a>&nbsp;&nbsp;&nbsp;";
                    }
                    pageHtml += "</span></li>";
                    $("#cashRecords_list").html(htmlStr + pageHtml);
                }


            } else {
                alert(res.desc);
            }

        }
    });
}