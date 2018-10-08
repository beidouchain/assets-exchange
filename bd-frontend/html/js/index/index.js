var baseCoinName = 'BEIDOU-USDT';
var baseCoinNick = 'BEIDOU-USDT';
$(document).ready(function () {
    //fetching current price of all other coins based on baseCoin
    getCurrentPriceOnBaseCoin(baseCoinName);
});

function getCurrentPriceOnBaseCoin(nameOfCoin) {
    var paramsModule = {
        name : nameOfCoin
    };
    $.ajax({
        url: webPathName + "/assetsMarket/getMarketCategoryStateList.do",
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
                    var htmlStr = "<ul>";
                    for (var i = 0; i < data.length; i++) {
                        var vo = data[i];
                        var nick = vo.nick;
                        var price = vo.price;
                        htmlStr += "<li>";
                        htmlStr += "<img src=\"images/bdc_logo.png\" alt=\"\" />";
                        htmlStr += "<span>" + nick + "/" + baseCoinNick + "</span>";
                        htmlStr += "<strong>" + price + "</strong>";
                        htmlStr += "</li>";
                    }
                    htmlStr += "</ul>";
                    $("#mainstream").html(htmlStr);
                    //
                    getCurrentPriceOnBaseCoinEx(baseCoinName);
                }
            } else {
                alert(res.desc);
            }

        }
    });
}

function getCurrentPriceOnBaseCoinEx(nameOfCoin) {
    var paramsModule = {
        name : nameOfCoin
    };
    $.ajax({
        url: webPathName + "/assetsMarket/getMarketCategoryStateList.do",
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
                    var htmlStr = "<ul>";
                    for (var i = 0; i < data.length; i++) {
                        var vo = data[i];
                        var nick = vo.nick;
                        var price = vo.price;
                        var maxPrice = vo.maxPrice;
                        var minPrice = vo.minPrice;
                        var priceRise = vo.priceRise;
                        var count = vo.count;
                        htmlStr += "<li>";
                        htmlStr += "<div class=\"currency_tit\">";
                        htmlStr += "<p>" + nick + "/" + baseCoinNick + "</p>";
                        htmlStr += "<strong><span>" + price + "</span></strong>";
                        htmlStr += "<ul>";
                        //
                        htmlStr += "<li>";
                        htmlStr += "<span>" + maxPrice + "</span>";
                        htmlStr += "<strong>24HMaximum price</strong>";
                        htmlStr += "</li>";

                        htmlStr += "<li>";
                        htmlStr += "<span>" + minPrice + "</span>";
                        htmlStr += "<strong>24HMinimum price</strong>";
                        htmlStr += "</li>";

                        htmlStr += "<li>";
                        htmlStr += "<span>" + priceRise + "</span>";
                        htmlStr += "<strong>24Hchanges</strong>";
                        htmlStr += "</li>";

                        htmlStr += "<li>";
                        htmlStr += "<span>" + count + "</span>";
                        htmlStr += "<strong>24HTrading volume</strong>";
                        htmlStr += "</li>";
                        //
                        htmlStr += "</ul>";
                        htmlStr += "</div>";
                        htmlStr += "</li>";
                    }
                    htmlStr += "</ul>";
                    $("#all_currency_info").html(htmlStr);
                }
            } else {
                alert(res.desc);
            }

        }
    });
}




