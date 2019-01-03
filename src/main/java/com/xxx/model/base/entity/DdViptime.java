package com.xxx.model.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员日期实体类
 */
@TableName("ddViptime")
public class DdViptime extends Model<DdViptime> {

    @Override
    protected Serializable pkVal(){ return this.id; }

    private static final long serialVersionUID = 1L;

    /**   */
    private Integer id;
    /**  用户手机 */
    private String userTelephone;
    /**  vip类型 */
    private String vipType;
    /**  VIP开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**  VIP结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public DdViptime(){
    }

    public DdViptime(String user_telephone, String vip_type, Date start_time, Date end_time) {
        super();
        this.userTelephone = user_telephone;
        this.vipType = vip_type;
        this.startTime = start_time;
        this.endTime = end_time;
    }

    // -------------------- GET AND SET --------------------

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getUserTelephone(){
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone){
        this.userTelephone = userTelephone;
    }

    public String getVipType(){
        return vipType;
    }

    public void setVipType(String vipType){
        this.vipType = vipType;
    }

    public java.util.Date getStartTime(){
        return startTime;
    }

    public void setStartTime(java.util.Date startTime){
        this.startTime = startTime;
    }

    public java.util.Date getEndTime(){
        return endTime;
    }

    public void setEndTime(java.util.Date endTime){
        this.endTime = endTime;
    }
}
