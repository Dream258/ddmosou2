package com.xxx.model.base.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xxx.common.framework.base.BaseDAO;
import com.xxx.model.base.entity.DdViptime;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 会员日期持久层
 */
@Component
public interface DdViptimeDAO extends BaseMapper<DdViptime> {
    //查询到期天数
    public Integer selectDays(@Param("telephone") String telephone);

    //查询个人VIP服务
    public List<DdViptime> getVIPtimeByTelephone(@Param("telephone") String telephone);

    //添加VIP信息
    public Integer addVIPtime(DdViptime viptime);

    // 按手机号和会员类型查询
    public DdViptime getVIPtimeByTelephoneAndType(@Param("telephone") String telephone, @Param("user_type") String user_type);

    // 查询是否到期
    public DdViptime selectYN(@Param("telephone") String telephone, @Param("user_type") String user_type);

    // 延长会员时间
    public Integer updateEnd_time(@Param("telephone") String telephone,
                                  @Param("time") Integer time, @Param("user_type") String user_type);

    // 重新开通会员
    public Integer updateStart_End_time(@Param("telephone") String telephone,
                                        @Param("time1") Date time1, @Param("time2") Date time2,
                                        @Param("user_type") String vip_type);
}
@Component
interface AutoDdViptimeDAO extends BaseDAO<DdViptime>{

}