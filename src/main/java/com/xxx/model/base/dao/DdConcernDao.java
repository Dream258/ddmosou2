package com.xxx.model.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xxx.model.base.entity.DdConcern;

@Mapper
public interface DdConcernDao extends BaseMapper<DdConcern> {

	@Select("SELECT * FROM dd_concern WHERE del_flag = 0 AND user_id = #{userId};")
	List<DdConcern> getList(DdConcern ddConcern);

	@Select("<script>" + "SELECT status FROM dd_concern WHERE goods_id IN "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>" + "#{item}"
			+ "</foreach>" + "</script>")
	List<Integer> getStatusList(List<String> list);

	@Select("SELECT status FROM dd_concern WHERE goods_id =#{goodsId} AND user_id = #{userId};")
	Integer getStatus(DdConcern ddConcern);
}
