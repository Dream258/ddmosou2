package com.xxx.model.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xxx.model.base.entity.DdConcern;

@Mapper
public interface DdConcernDao extends BaseMapper<DdConcern> {

	@Select("SELECT * FROM dd_concern WHERE del_flag = 0 AND status =1 AND user_id = #{userId};")
	List<DdConcern> getList(DdConcern ddConcern);

	@Select("SELECT status FROM dd_concern WHERE del_flag = 0 AND goods_id =#{goodsId} AND user_id = #{userId};")
	Integer getStatus(DdConcern ddConcern);

	@Select("SELECT * FROM dd_concern WHERE del_flag = 0 AND goods_id =#{goodsId} AND user_id = #{userId};")
	DdConcern getItem(DdConcern ddConcern);

	@Update("UPDATE dd_concern set status = #{status},del_flag=#{delFlag} WHERE del_flag = 0 AND goods_id =#{goodsId} AND user_id = #{userId};")
	Integer updateItem(DdConcern ddConcern);
}
