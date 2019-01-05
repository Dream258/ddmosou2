package com.xxx.model.base.service;

import com.xxx.common.framework.base.BaseService;
import com.xxx.model.base.dao.DdMallDAO;
import com.xxx.model.base.entity.DdMall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 店铺业务逻辑类
 */
@Service
public class DdMallService extends BaseService<DdMall> {

    @Resource
    private DdMallDAO ddMallDAO;

    /** 保存 */
    @Transactional
    public void save(DdMall ddMall) {
        if(ddMall.getId() == null){
            this.insert(ddMall);
        }else{
            this.updateParams(ddMall);
        }
    }

    public int getListSize(String userId){
        return ddMallDAO.getListSize(userId);
    }

    public List<Map<String,Object>> getList(int userId){
        return ddMallDAO.getList(userId);
    }

    public Integer deleteMall(int keyId){
        return ddMallDAO.deleteMall(keyId);
    }
}