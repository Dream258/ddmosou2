$(function(){

    /*$.each(json, function (index, data) {});*/
    $.get('/api/getCats?parentId=0', function(res) {
        $("#category1").append(res.cats);
        var id = $("#category1 option:selected").val();
        $.get('/api/getCats?parentId='+id, function(res) {
            $("#category2").append(res.cats);
            var id = $("#category2 option:selected").val();
            $.get('/api/getCats?parentId='+id, function(res) {
                $("#category3").append(res.cats);
            });
        });
    });

})

function getMall() {
    var a = $("#mall_a").val();
    if(a!=""){
        $.get('/api/getMall?mallId='+a, function(res) {
            var a;
            if(res.mall==null){
                a = '<h1>搜索结果</h1><div class="result_title1">查排名功能当前处于测试阶段，查询结果会有偏差，商家可作为参考，更多功能敬请期待</div>';
            }else{
                a = '<table class="mytable"><tr><td>店铺LOGO</td><td>店铺名称</td><td>商品数量</td><td>总销量</td></tr>' +
                    '<tr><td><dl><dt><img src="'+res.mall.logo+'"></dt></td><td><dd>'+res.mall.name+'</dd></dl></td><td>'+res.mall.size+'</td><td>'
                    +res.mall.sold+'</td></tr></table>';
            }
            $(".only_three").html(a);
        });
    }else{
        alert("请填写店铺ID！");
    }
}

function cats1() {
    var id = $("#category1 option:selected").val();
    $.get('/api/getCats?parentId='+id, function(res) {
        $("#category2").empty();
        $("#category2").append(res.cats);
        var id = $("#category2 option:selected").val();
        $.get('/api/getCats?parentId='+id, function(res) {
            $("#category3").empty();
            $("#category3").append(res.cats);
        });
    });
}

function cats2() {
    var id = $("#category2 option:selected").val();
    $.get('/api/getCats?parentId='+id, function(res) {
        $("#category3").empty();
        $("#category3").append(res.cats);
    });
}

function getCats() {
    var a = $("#category3 option:selected").val();
    var b = $("#category_b").val();
    if(b!=""){
        $.get('/api/getKey?parentId='+a+"&goodsId="+b, function(res) {
            var a;
            if(res.goods==null){
                a = '<h1>搜索结果</h1><div class="result_title1">查排名功能当前处于测试阶段，查询结果会有偏差，商家可作为参考，更多功能敬请期待</div>';
            }else{
                a = '<table class="mytable"><tr><td>商品信息</td><td>单价</td><td>团购价</td><td>排名</td><td>销量</td></tr>' +
                    '<tr><td><dl><dt><img src="'+res.goods.img+'"></dt><dd>'+res.goods.name+'</dd></dl></td><td>'+res.goods.normal+'</td><td>'
                    +res.goods.group+'</td><td>'+res.goods.top+'</td><td>'+res.goods.sold+'</td></tr></table>';
            }
            $(".only_two").html(a);
        });
    }else{
        alert("请填写商品ID！");
    }
}

function getKey(){
    var a = $("#keyword_a").val();
    var b = $("#keyword_b").val();
    var c = $("input[name='sort']:checked").val();
    if(a!=""&&b!=""){
        $.get('/api/getKey?keyword='+a+"&goodsId="+b+"&sort="+c, function(res) {
            var a;
            if(res.goods==null){
                a = '<h1>搜索结果</h1><div class="result_title1">查排名功能当前处于测试阶段，查询结果会有偏差，商家可作为参考，更多功能敬请期待</div>';
            }else{
                a = '<table class="mytable"><tr><td>商品信息</td><td>单价</td><td>团购价</td><td>排名</td><td>销量</td></tr>' +
                    '<tr><td><dl><dt><img src="'+res.goods.img+'"></dt><dd>'+res.goods.name+'</dd></dl></td><td>'+res.goods.normal+'</td><td>'
                    +res.goods.group+'</td><td>'+res.goods.top+'</td><td>'+res.goods.sold+'</td></tr></table>';
            }
            $(".only_one").html(a);
        });
    }else{
        alert("请填写关键词/商品ID！");
    }
}