<!DOCTYPE html>
<#import "../../macro/common.ftl" as common>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <title>JeeBoom  店铺</title>
        <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/layui/css/layui.css" />
        <link rel="stylesheet" href="${springMacroRequestContext.contextPath}/static/css/global.css" />
        <link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">
    </head>
    <body class="anim-fadeInUp">
        <div style="margin: 10px;">
            <div class="layui-tab-brief" >
                <ul class="layui-tab-title">
                    <li class="layui-this">${(ddMall?? && ddMall.id??)?string('编辑','添加')}店铺</li>
                </ul>
            </div>
            <form id="saveForm" class="layui-form layui-form-pane mt10" >
                <input type="hidden" value="${(ddMall.id)!}" name="id" />

                        <div class="layui-form-item">
                            <label class="layui-form-label">用户ID</label>
                            <div class="layui-input-block">
                                    <input type="text" name="userId" value="${(ddMall.userId)!}" placeholder="请输入用户ID" class="layui-input" maxlength="11">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">店铺ID</label>
                            <div class="layui-input-block">
                                    <input type="text" name="mallId" value="${(ddMall.mallId)!}" placeholder="请输入店铺ID" class="layui-input" maxlength="11">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">店铺名称</label>
                            <div class="layui-input-block">
                                    <input type="text" name="mallName" value="${(ddMall.mallName)!}" placeholder="请输入店铺名称" class="layui-input" maxlength="20">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">店铺LOGO</label>
                            <div class="layui-input-block">
                                    <input type="text" name="mallLogo" value="${(ddMall.mallLogo)!}" placeholder="请输入店铺LOGO" class="layui-input" maxlength="100">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">商品数量</label>
                            <div class="layui-input-block">
                                    <input type="text" name="goodsNum" value="${(ddMall.goodsNum)!}" placeholder="请输入商品数量" class="layui-input" maxlength="11">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">店铺销量</label>
                            <div class="layui-input-block">
                                    <input type="text" name="mallSold" value="${(ddMall.mallSold)!}" placeholder="请输入店铺销量" class="layui-input" maxlength="11">
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">创建时间</label>
                            <div class="layui-input-block">
                                    <#if ddMall?? && ddMall.createtime??>
                                    <input type="text" name="createtime" value="${ddMall.createtime?string('yyyy-MM-dd HH:mm:ss')}" placeholder="请输入创建时间" class="layui-input" onclick="layui.laydate({elem: this, istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" maxlength="19">
                                    <#else>
                                    <input type="text" name="createtime" value="" placeholder="请输入创建时间" class="layui-input" onclick="layui.laydate({elem: this, istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" maxlength="19">
                                    </#if>
                            </div>
                        </div>

                <@common.permission per='base:ddMall:edit'>
                <div class="layui-form-item">
                    <button class="layui-btn fr" lay-submit="" lay-filter="demo2">保存店铺</button>
                </div>
                </@common.permission>
            </form>
        </div>
        <script type="text/javascript" src="${springMacroRequestContext.contextPath}/static/layui/layui.js"></script>
        <script>
            layui.use(['form','laypage','layer','laydate'],function(){
                var form = layui.form;
                var layer = layui.layer;
                var $ = layui.jquery;
                var laydate = layui.laydate;
                form.on('submit(demo2)',function(data){
                    layer.load();
                    $.ajax({
                        url:"${springMacroRequestContext.contextPath}/ddMall/save",
                        type:"post",
                        data:$("#saveForm").serialize(),
                        dataType:"json",
                        success:function(d){
                            layer.closeAll('loading');
                            if(d.code == 200){
                                window.top.layer.msg("保存成功！", {icon: 1});
                                parent.layui.submitForm();
                            }else{
                                layer.msg("对不起，访问不成功！错误编码：" + d.code);
                            }
                        }
                    })
                    return false;
                });

            });
        </script>
    </body>
</html>
