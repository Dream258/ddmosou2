package com.xxx.model.base.service;

import com.xxx.common.framework.base.BaseService;
import com.xxx.model.base.dao.DdViptimeDAO;
import com.xxx.model.base.entity.DdViptime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 会员日期业务逻辑类
 */
@Service
public class DdViptimeService extends BaseService<DdViptime> {

    @Resource
    private DdViptimeDAO ddViptimeDAO;

    /** 保存 */
    @Transactional
    public void save(DdViptime ddViptime) {
        if(ddViptime.getId() == null){
            this.insert(ddViptime);
        }else{
            this.updateParams(ddViptime);
        }
    }

    //查询到期天数
    public Integer selectDays(String telephone) {
        return ddViptimeDAO.selectDays(telephone);
    }

    public Integer addVIPtime(DdViptime viptime) {
        return ddViptimeDAO.addVIPtime(viptime);
    }

    public List<DdViptime> getVIPtimeByTelephone(String telephone) {
        return ddViptimeDAO.getVIPtimeByTelephone(telephone);
    }

    //按手机号和会员类型查询
    public DdViptime getVIPtimeByTelephoneAndType(String telephone,String user_type){
        return ddViptimeDAO.getVIPtimeByTelephoneAndType(telephone, user_type);
    }

    public Integer updateEnd_time(String telephone, Integer time, String vip_type) {
        return ddViptimeDAO.updateEnd_time(telephone, time,vip_type);
    }

    public Integer updateStart_End_time(String telephone, Date time1, Date time2, String vip_type) {
        return ddViptimeDAO.updateStart_End_time(telephone, time1, time2, vip_type);
    }

    public DdViptime selectYN(String telephone, String user_type) {
        return ddViptimeDAO.selectYN(telephone, user_type);
    }
}