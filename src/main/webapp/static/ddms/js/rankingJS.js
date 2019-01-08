$(function(){
    var a = $("#userId").val();
    $.get('/api/getKeyVip?userId='+a+'&type=1',
        function(res) {
            $.each(res.goods, function (index, data) {
                $("#ranking_1").append("<tr><td style='max-width:150px'><dl><dt><img src='"+data.goodsThumbnail+"'></dt><dd>"+data.goodsName+"</dd></dl></td><td>"
                    +data.goodsKey+"</td><td>"+data.goodsSold+"</td><td>"+data.goodsTop+"</td><td>" +
                    "<a href='#' data-toggle='modal' data-target='#myModal1' onclick='key_walk1("+data.id+");'>走势</a>" +
                    "&nbsp;&nbsp;&nbsp;<a href='javascript:deleteGoods("+data.id+")'>删除</a></td></tr>");
            });
        });
    $.get('/api/getCatsVip?userId='+a+'&type=2', function(res) {
        $.each(res.goods, function (index, data) {
            $("#ranking_2").append("<tr><td style='max-width:150px'><dl><dt><img src='"+data.goodsThumbnail+"'></dt><dd>"+data.goodsName+"</dd></dl></td><td>"
                +data.goodsKey+"</td><td>"+data.goodsSold+"</td><td>"+data.goodsTop+"</td><td>" +
                "<a href='#' data-toggle='modal' data-target='#myModal2' onclick='key_walk2("+data.id+");'>走势</a>" +
                "&nbsp;&nbsp;&nbsp;<a href='javascript:deleteGoods("+data.id+")'>删除</a></td></tr>");
        });
    });
    $.get('/api/getMallVip?userId='+a, function(res) {
        $.each(res.malls, function (index, data) {
            $("#ranking_3").append("<tr><td>"+data.mallId+"</td><td>"
                +data.mallName+"</td><td style='width:15%'><img src='"+data.mallLogo+"' style='width:50%; margin:3% auto'></td><td>"+data.goodsNum+
                "</td><td>" + data.mallSold +"</td><td>"+data.createtime+"</td><td>"+
                "&nbsp;&nbsp;&nbsp;<a href='javascript:deleteMall("+data.id+")'>删除</a></td></tr>");
        });
    });

})

$('.clearfix li').click(function() {
    var index = $(this).index();
    $(this).find('a').css({
        'color': '#D8271C',
        'background-color': '#fff'
    }).parent().siblings().find('a').css({
        'color': '#666',
        'background-color': '#F6F7FE'
    });
    $('.contents .mytable').eq(index).show().siblings().hide();
});
$('.modal-content .tab a').click(function() {
    $(this).css('color', '#D8271C').siblings().css('color', '#666');
    var index = $(this).index();
    $('.tabs .main').eq(index).show().siblings().hide();
});

function  key_walk1(id) {
    $.get('/api/keyWalk?keyId='+id, function(res) {
        var chart = Highcharts.chart('container1', {
            chart: {
                type: 'line'
                //inverted: true //翻转X轴Y轴
            },
            title: {
                text: '商品排名走势折线图'
            },
            subtitle: {
                text: ''+res.walk.goodsName
            },
            xAxis: {
                categories: [''+res.walk.oldTime,''+res.walk.time]
            },
            yAxis: {
                title: {
                    text: '排名'
                },
                reversed: true //逆向
                //tickPositions: [100, 50, 20, 10, 0]
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true// 开启数据标签
                    },
                    enableMouseTracking: false// 关闭鼠标跟踪，对应的提示框、点击事件会失效
                }
            },
            series: [{
                name: '商品走势', data: [res.walk.oldTop, res.walk.top]
            }]
        });
    });
}

function  key_walk2(id) {
    $.get('/api/keyWalk?keyId='+id, function(res) {
        var chart = Highcharts.chart('container2', {
            chart: {
                type: 'line'
                //inverted: true //翻转X轴Y轴
            },
            title: {
                text: '商品排名走势折线图'
            },
            subtitle: {
                text: ''+res.walk.goodsName
            },
            xAxis: {
                categories: [''+res.walk.oldTime,''+res.walk.time]
            },
            yAxis: {
                title: {
                    text: '排名'
                },
                reversed: true //逆向
                //tickPositions: [100, 50, 20, 10, 0]
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true// 开启数据标签
                    },
                    enableMouseTracking: false// 关闭鼠标跟踪，对应的提示框、点击事件会失效
                }
            },
            series: [{
                name: '商品走势', data: [res.walk.oldTop, res.walk.top]
            }]
        });
    });
}

$(function(){
    $('.add_type .radio-inline').on('click', function () {
        var val=$('input[name="inlineRadioOptions"]:checked').val();
        if(val=='1'){
            $('.form1').show();
            $('.form2').hide();
            $('.form3').hide();
        }
        if(val=='2'){
            $('.form1').hide();
            $('.form2').show();
            $('.form3').hide();
        }
        if(val=='3'){
            $('.form1').hide();
            $('.form2').hide();
            $('.form3').show();
        }
    })
})

function addGood() {
    var keyword = $("#keyword_form").val();
    var goodsId = $("#keyword_sku").val();
    var userId = $("#userId").val();
    $.ajax({
        url: "/api/addKey",
        data: {
            "userId" : userId,
            "keyword" : keyword,
            "goodsId" : goodsId,
            "sort" : "0",
            "type" : "1"
        },
        type: "GET",
        dataType : "json",
        beforeSend: function(){
            $("#onlylalala").show();
        },success:function(data){
            if(data["success"]=="000") {
                alert("添加成功");
                window.location.href="/api/ranking";
            }else if(data["success"]=="002"){
                alert("您要监控的商品在一千名之外，或该商品名称里未包含关键字！");
            }else{
                alertError("监控数已满10个");
            }
        },complete: function () {
            $("#onlylalala").hide();
        },
        error:function(){
            alertError('系统错误');
        }
    });
}
function addCat() {
    var goodsId = $("#category_sku").val();
    var parentId = $("#category3 option:selected").val();
    var userId = $("#userId").val();
    $.ajax({
        url: "/api/addCat",
        data: {
            "userId" : userId,
            "goodsId" : goodsId,
            "parentId" : parentId,
            "type" : "2"
        },
        type: "GET",
        dataType : "json",
        beforeSend: function(){
            $("#onlylalala").show();
        },success:function(data){
            if(data["success"]=="000") {
                alert("添加成功");
                window.location.href="/api/ranking";
            }else if(data["success"]=="002"){
                alert("您要监控的商品在一千名之外，或该商品名称里未包含关键字！");
            }else{
                alertError("监控数已满10个");
            }
        },complete: function () {
            $("#onlylalala").hide();
        },
        error:function(){
            alertError('系统错误');
        }
    });
}
function addMall() {
    var userId = $("#userId").val();
    var mallID = $("#actSku").val();
    $.ajax({
        url: "/api/addMall",
        data: {
            "userId" : userId,
            "mallID" : mallID
        },
        type: "GET",
        dataType : "json",
        beforeSend: function(){
            $("#onlylalala").show();
        },success:function(data){
            if(data["success"]=="000") {
                alert("添加成功");
                window.location.href="/api/ranking";
            }else if(data["success"]=="002"){
                alert("未找到该店铺信息！");
            }else{
                alertError("监控数已满10个");
            }
        },complete: function () {
            $("#onlylalala").hide();
        },
        error:function(){
            alertError('系统错误');
        }
    });
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

function deleteGoods(data) {
    if(confirm("是否删除？")==true){
        window.location.href="/api/deleteGood?id="+data;
    }
}
function deleteMall(data) {
    if(confirm("是否删除？")==true){
        window.location.href="/api/deleteMall?id="+data;
    }
}

$(function(){
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

});