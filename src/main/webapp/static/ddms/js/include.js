//检查登录
function checklogin() {
    $.ajax({
        type:'POST',
        url:'/api/checkLogin',
        dataType:'json',
        success:function(data){
            if(data==1) {
                window.location.href="/api/ranking";
            }else{
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
    $.ajax({
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
    });
}

//跳转购买记录页面
function buy() {
    $.ajax({
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
    });
}

//跳转邀请返利页面
function share() {
    $.ajax({
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
    });
}
