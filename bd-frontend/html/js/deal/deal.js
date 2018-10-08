var defaultCoinName = 'BEIDOU-USDT';
$(document).ready(function () {
    //1、获取交易市场所有币种及价格涨幅
    getCoinMarkets(defaultCoinName);
    getInfoByNick(defaultCoinName);
});

function getInfoByNick(name, nick) {
    // alert(name + "-" +nick);
    $("#priceAsset").attr("value",name);
    //2、获得当前币种最新价格及成交量
    getBaseInfo(nick);
    //3、获取当前币种成交历史
    getMarketHis(nick);
    //4、获取当前币种挂单价格
    buyList(nick);
    sellList(nick);
    //5、获取当前登录用户此币种的交易订单及历史订单
    r(nick);
}


function getCoinMarkets(coinName) {
    var paramsModule = {
        name : coinName
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
                        var name = vo.name;
                        var nick = vo.nick;
                        var price = vo.price;
                        var priceRise = vo.priceRise;
                        htmlStr += "<li id='"+name+"' name='"+name+"'>";
                        htmlStr += "<span>" + nick + "</span>";
                        htmlStr += "<span>" + price + "</span>";
                        htmlStr += "<span>" + priceRise + "</span>";
                        htmlStr += "</li>";
                    }
                    htmlStr += "</ul>";
                    $("#markets").html(htmlStr);
                    $('#markets ul').on('click','li',function(){
                        getInfoByNick($(this).attr("id"), $(this).attr("name"));
                    });
                }else {
                    $("#markets").html("");
                }
            } else {
                alert(res.desc);
            }

        }
    });
}

function getBaseInfo(nick) {
    var paramsModule = {
        nick : nick
    };
    $.ajax({
        url: webPathName + "/assetsMarket/getAllAsset.do",
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
                    var vo = data[0];
                    var name = vo.name;
                    var nick = vo.nick;
                    htmlStr += "<strong>BDC/CNY ≈ 2.2750</strong>";
                    htmlStr += "<span>Change +13.75%</span>";
                    htmlStr += "<span>Maximum price 2.275</span>";
                    htmlStr += "<span>Minimum price 1.944</span>";
                    htmlStr += "<span>24HTrading Volume 16002.8552 BDC</span>";
                    htmlStr += "<a href=\"\">BDC details></a>";
                    $("#dealPandect").html(htmlStr);
                }else {
                    $("#dealPandect").html("");
                }
            } else {
                alert(res.desc);
            }

        }
    });
}

function buyList(nick) {
    var asset = $("#asset").val();
    var priceAsset = nick;
    if(nick == null){
        priceAsset = $("#priceAsset").val();
    }
    var paramsModule = {
        asset : asset,
        priceAsset : priceAsset,
    };
    $.ajax({
        url: webPathName + "/match/buyList.do",
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
                    var buyStr = "<ul>";
                    for (var i = 0; i < data.length; i++) {
                        var vo = data[i];
                        var name = vo.asset;
                        var priceAssetName = vo.priceAssetName;
                        var price = vo.price;
                        var amount = vo.amount;
                        buyStr += "<li id='"+name+"' >";
                        buyStr += "<span>" + priceAssetName + "</span>";
                        buyStr += "<span style=\"color: #46c35e;\" id=\"price\">"+price+"</span>";
                        buyStr += "<span style=\"color: #46c35e;\">"+amount+"</span>";
                        buyStr += "</li>";
                    }
                    buyStr += "</ul>";
                    $("#buyList").html(buyStr);
                    $('#buyList ul').on('click','li',function(){
                        $("#sellPrice").val($(this).find('span[id^="price"]').text());
                    });
                }else {
                    $("#buyList").html("");
                }
            } else {
                alert(res.desc);
            }

        }
    });
}

function sellList(nick) {
    var asset = $("#asset").val();
    var priceAsset = nick;
    if(nick == null){
        priceAsset = $("#priceAsset").val();
    }
    var paramsModule = {
        asset : asset,
        priceAsset : priceAsset,
    };
    $.ajax({
        url: webPathName + "/match/sellList.do",
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
                    var sellStr = "<ul>";
                    for (var i = 0; i < data.length; i++) {
                        var vo = data[i];
                        var name = vo.asset;
                        var priceAssetName = vo.priceAssetName;
                        var price = vo.price;
                        var amount = vo.amount;
                        sellStr += "<li id='"+name+"'>";
                        sellStr += "<span>" + priceAssetName + "</span>";
                        sellStr += "<span style=\"color: #fd5e5e;\" id=\"price\">"+price+"</span>";
                        sellStr += "<span style=\"color: #fd5e5e;\">"+amount+"</span>";
                        sellStr += "</li>";
                    }
                    sellStr += "</ul>";
                    $("#sellList").html(sellStr);
                    $('#sellList ul').on('click','li',function(){
                        $("#buyPrice").val($(this).find('span[id^="price"]').text());
                    });
                }else {
                    $("#sellList").html("");
                }
            } else {
                alert(res.desc);
            }

        }
    });
}

function getMarketHis(nick) {
    var asset = $("#asset").val();
    var priceAsset = nick;
    if(nick == null){
        priceAsset = $("#priceAsset").val();
    }
    var paramsModule = {
        asset : asset,
        priceAsset : priceAsset,
    };
    $.ajax({
        url: webPathName + "/exchange/list.do",
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
                        var price = vo.price;
                        var amount = vo.amount;
                        var time = vo.time;
                        htmlStr += "<li>";
                        htmlStr += "<span>" + time + "</span>";
                        htmlStr += "<span style=\"color: #46c35e;\">"+price+"</span>";
                        htmlStr += "<span>"+amount+"</span>";
                        htmlStr += "</li>";
                    }
                    htmlStr += "</ul>";
                    $("#latestDeal").html(htmlStr);
                }else {
                    $("#latestDeal").html("");
                }
            } else {
                alert(res.desc);
            }

        }
    });
}




