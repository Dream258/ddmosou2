package com.xxx.model.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxx.common.framework.base.BaseService;
import com.xxx.common.util.PDDUtils;
import com.xxx.model.base.dao.DdConcernDao;
import com.xxx.model.base.entity.DdConcern;
import com.xxx.model.base.entity.DdSearch;

@Service
public class DdSearchService extends BaseService<DdConcern> {

	@Autowired
	private DdConcernDao ddConcernDao;

	public List<DdSearch> search(String keys) {

		List<Map> list = PDDUtils.getGoodsListByPage(keys, null, null);
		List<DdSearch> ddSearchs = new ArrayList<>();
		List<String> goodIds = new ArrayList<>();
		DdConcern ddConcern = new DdConcern();
		ddConcern.setUserId(123);

		list.forEach(m -> {
			DdSearch ddSearch = new DdSearch();
			goodIds.add(m.get("goods_id").toString());
			String goods_id = m.get("goods_id").toString();
			ddSearch.setGoods_id(goods_id);
			ddConcern.setGoodsId(goods_id);
			Integer isConcern = ddConcernDao.getStatus(ddConcern);
			ddSearch.setIsConcern(ddConcernDao.getStatus(ddConcern) == null ? 0 : isConcern);
			ddSearch.setGoods_name(m.get("goods_name").toString());
			ddSearch.setGoods_image_url(m.get("goods_image_url").toString());
			ddSearch.setSold_quantity((int) m.get("sold_quantity"));
			ddSearch.setMin_group_price((int) m.get("min_group_price"));
			ddSearch.setMin_normal_price((int) m.get("min_normal_price"));
			ddSearch.setMall_name(m.get("mall_name").toString());
			ddSearch.setRank(1000);
			ddSearchs.add(ddSearch);
		});

		return ddSearchs;
	}

	public List<DdConcern> list(Integer userId) {
		DdConcern ddConcern = new DdConcern();
		ddConcern.setUserId(userId);
		return ddConcernDao.getList(ddConcern);
	}

	public Integer concern(DdConcern ddConcern) {
		return ddConcernDao.insert(ddConcern);
	}

	public Integer noConcern(DdConcern ddConcern) {
		return ddConcernDao.updateById(ddConcern);
	}

}
