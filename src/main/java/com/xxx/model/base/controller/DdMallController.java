package com.xxx.model.base.controller;

import com.xxx.common.bean.Pager;
import com.xxx.common.bean.ResultData;
import com.xxx.common.framework.base.BaseController;
import com.xxx.common.interfaces.Permission;
import com.xxx.model.base.entity.DdMall;
import com.xxx.model.base.service.DdMallService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * 店铺视图层
 */
@Controller
@RequestMapping("ddMall")
public class DdMallController extends BaseController {

    @Resource
    private DdMallService ddMallService;

    @ModelAttribute
    public DdMall get(@RequestParam(required=false) Long id) {
        if (id != null){
            return ddMallService.selectById(id);
        }else{
            return new DdMall();
        }
    }

    //分页列表
    @Permission("base:ddMall:view")
    @RequestMapping("page")
    public String page(DdMall ddMall, Pager<DdMall> pager, Model model){
        Pager<DdMall> page = ddMallService.selectPage(ddMall, pager);
        model.addAttribute("pager",page);
        model.addAttribute("ddMall",ddMall);
        return "base/ddMall/ddMallPage";
    }

    //跳转添加编辑页面
    @Permission("base:ddMall:view")
    @GetMapping("saveFrom")
    public String saveFrom(DdMall ddMall,Model model){
        model.addAttribute("ddMall",ddMall);
        return "base/ddMall/ddMallSave";
    }

    //添加编辑操作
    @Permission("base:ddMall:edit")
    @ResponseBody
    @PostMapping("save")
    public ResultData<Object> save(DdMall ddMall){
        try {
            ddMallService.save(ddMall);
            return success();
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }

    //删除
    @Permission("base:ddMall:edit")
    @GetMapping("delById")
    public String delById(Long id){
        if(id != null){
            ddMallService.delById(id);
        }
        return "redirect:/ddMall/page";
    }

    //批量删除
    @Permission("base:ddMall:edit")
    @GetMapping("delByIds")
    public String delByIds(String ids){
        if(StringUtils.isBlank(ids)){
            ddMallService.deleteByIds(ids.split(","));
        }
        return "redirect:/ddMall/page";
    }


}