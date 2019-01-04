package com.xxx.model.base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xxx.common.framework.base.BaseDAO;
import com.xxx.model.base.entity.DdMall;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 店铺持久层
 */
@Component
public interface DdMallDAO extends BaseMapper<DdMall> {
    @Select("SELECT COUNT(0) FROM dd_mall WHERE 1=1 AND user_id = #{userId}")
    int getListSize(@Param("userId") int userId);

    @Select("SELECT * FROM dd_mall WHERE 1=1 AND user_id = #{userId}")
    List<Map<String,Object>> getList(@Param("userId") int userId);
}
@Component
interface AutoDdMallDAO extends BaseDAO<DdMall>{

}