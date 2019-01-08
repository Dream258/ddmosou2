//检查登录
function checklogin() {
    $.ajax({
        type:'POST',
        url:'/api/checkLogin',
        dataType:'json',
        success:function(data){
            if(data==2) {
                window.location.href="/api/ranking";
            }else if(data==1){
                alertError("该功能仅限会员用户");
            }else {
                alertError('请登录');
            }
        },
        error:function(){
            alertError('系统错误');
        }
    });
}
//检查登录
function checklogin2() {
    $.ajax({
        type:'POST',
        url:'/api/checkLogin',
        dataType:'json',
        success:function(data){
            if(data==2) {
                window.location.href="/api/xiaoliang";
            }else if(data==1){
                alertError("该功能仅限会员用户");
            }else {
                alertError('请登录');
            }
        },
        error:function(){
            alertError('系统错误');
        }
    });
}
//退出登录
function exit() {
    window.location.href = "/api/exituser";
    /*$.ajax({
        url:'api/exituser',
        type:'POST',
        success:function(param){
            if(param == 1){
                window.location.href = "/api/";
            }else{
                alertError(param.msg)
            }
        },
        error:function(){
            alertError('系统错误')
        }
    });*/
}

//跳转购买记录页面
function buy() {
    window.location.href = "/api/selectOrders";
    /*$.ajax({
        url:'selectOrders',
        type:'POST',
        success:function(param){
            if(param == 1){
                window.location.href = "buylist";
            }else{
                alertError(param.msg)
            }
        },
        error:function(){
            alertError('系统错误')
        }
    });*/
}

//跳转邀请返利页面
function share() {
    window.location.href = "/api/subordinate";
    /*$.ajax({
        url:'subordinate',
        data:"pn=1",
        type:'POST',
        success:function(param){
            if(param == 1){
                window.location.href = "share_list";
            }else{
                alertError(param.msg)
            }
        },
        error:function(){
            alertError('系统错误')
        }
    });*/
}
//导航栏蓝条


