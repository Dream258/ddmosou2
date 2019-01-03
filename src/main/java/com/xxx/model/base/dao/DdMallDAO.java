package com.xxx.model.base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xxx.common.framework.base.BaseDAO;
import com.xxx.model.base.entity.DdMall;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * 店铺持久层
 */
@Component
public interface DdMallDAO extends BaseMapper<DdMall> {

}
@Component
interface AutoDdMallDAO extends BaseDAO<DdMall>{

}