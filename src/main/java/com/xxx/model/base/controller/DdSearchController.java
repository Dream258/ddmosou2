package com.xxx.model.base.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xxx.common.interfaces.NotLogin;
import com.xxx.common.util.PDDUtils;
import com.xxx.model.base.entity.DdConcern;
import com.xxx.model.base.entity.DdMember;
import com.xxx.model.base.entity.DdSearch;
import com.xxx.model.base.service.DdSearchService;

@NotLogin
@Controller
@RequestMapping("ddSearch")
public class DdSearchController {

	@Autowired
	private DdSearchService ddSearchService;

	@RequestMapping("search")
	@ResponseBody
	public List<DdSearch> Search(HttpServletRequest httpServletRequest, String keys) {

		DdMember ddMember = (DdMember) httpServletRequest.getSession().getAttribute("user_login");
		return ddSearchService.search(keys, ddMember.getId());
	}

	@RequestMapping("list")
	@ResponseBody
	public List<DdSearch> list(HttpServletRequest httpServletRequest) {

		DdMember ddMember = (DdMember) httpServletRequest.getSession().getAttribute("user_login");
		List<DdConcern> list = ddSearchService.list(ddMember.getId());
		List<DdSearch> ddSearchs = new ArrayList<>();
		list.forEach(l -> {
			JSONObject jsonObject = PDDUtils.getGoodsInfo(l.getGoodsId().toString());
			DdSearch ddSearch = new DdSearch();
			ddSearch.setIsConcern(1);
			ddSearch.setGoods_id(jsonObject.get("goods_id").toString());
			ddSearch.setGoods_name(jsonObject.get("goods_name").toString());
			ddSearch.setGoods_image_url(jsonObject.get("goods_image_url").toString());
			ddSearch.setSold_quantity((int) jsonObject.get("sold_quantity"));
			ddSearch.setMin_group_price((int) jsonObject.get("min_group_price"));
			ddSearch.setMin_normal_price((int) jsonObject.get("min_normal_price"));
			ddSearch.setMall_name(jsonObject.get("mall_name").toString());
			ddSearch.setRank(1000);
			ddSearchs.add(ddSearch);
		});
		return ddSearchs;
	}

	@RequestMapping("concern")
	@ResponseBody
	public Boolean concern(HttpServletRequest httpServletRequest, DdConcern ddConcern) {
		DdMember ddMember = (DdMember) httpServletRequest.getSession().getAttribute("user_login");
		ddConcern.setUserId(ddMember.getId());
		return ddSearchService.concern(ddConcern) > 0 ? true : false;
	}
}
