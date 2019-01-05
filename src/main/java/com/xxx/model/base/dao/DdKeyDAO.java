package com.xxx.model.base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xxx.common.framework.base.BaseDAO;
import com.xxx.model.base.entity.DdKey;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 关键词持久层
 */
@Component
public interface DdKeyDAO extends BaseMapper<DdKey> {

    @Select("SELECT COUNT(0) FROM dd_key WHERE 1=1 AND user_id = #{userId}")
    int getListSize(@Param("userId") int userId);

    @Select("SELECT * FROM dd_key WHERE 1=1 AND user_id = #{userId} AND type = #{type}")
    List<Map<String,Object>> getList(@Param("userId") int userId, @Param("type") String type);

    @Select("SELECT * FROM dd_key WHERE id = #{id}")
    Map<String,Object> getKey(@Param("id") int keyId);

    @Delete("delete from dd_key where id = #{id}")
    Integer deleteKey(@Param("id") int keyId);

}
@Component
interface AutoDdKeyDAO extends BaseDAO<DdKey>{

}