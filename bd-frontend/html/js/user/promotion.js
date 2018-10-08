$(document).ready(function () {

    //myPromotion
    $("#myPromotion").click(function(){
        getMyPromotion();
    });
});

function getMyPromotion (pageNo) {
    if (pageNo == null) {
        pageNo = 0;
    }
    var userModule = {
        address : userInfoJsonCookieData.address,
        pageNum : pageNo
    };
    $.ajax({
        url: webPathName + "/userPromotion/list.do",
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
                        var otherAddress = vo.otherAddress;
                        var timeStr = vo.createTime;

                        htmlStr += "<li >";
                        htmlStr += "<span style='width: 15%'>" + timeStr + "</span><span>" + otherAddress + "</span>";
                        htmlStr += "</li>";
                    }
                    //分页
                    var pageHtml = "";
                    pageHtml += "<li style='width: 100%'><span style='width: 100%'>";
                    for (var i = 1; i <= totalNum; i++) {
                        pageHtml += "&nbsp;&nbsp;<a href='#' onclick='getMyPromotion("+ i +")'>" + i + "</a>&nbsp;&nbsp;&nbsp;";
                    }
                    pageHtml += "</span></li>";
                    $("#promotion_list").html(htmlStr + pageHtml);
                }


            } else {
                alert(res.desc);
            }

        }
    });
}