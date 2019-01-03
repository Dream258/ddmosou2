package com.xxx.model.base.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 店铺实体类
 */
@TableName("ddMall")
public class DdMall extends Model<DdMall> {

    @Override
    protected Serializable pkVal(){ return this.id; }

    private static final long serialVersionUID = 1L;

    /**  店铺 */
    private Integer id;
    /**  店铺ID */
    private String mallId;
    /**  店铺名称 */
    private String mallName;
    /**  店铺LOGO */
    private String mallLogo;
    /**  商品数量 */
    private String goodsNum;
    /**  店铺销量 */
    private String mallSold;
    /**  创建时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createtime;

    public DdMall(){
    }

    // -------------------- GET AND SET --------------------

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getMallId(){
        return mallId;
    }

    public void setMallId(String mallId){
        this.mallId = mallId;
    }

    public String getMallName(){
        return mallName;
    }

    public void setMallName(String mallName){
        this.mallName = mallName;
    }

    public String getMallLogo(){
        return mallLogo;
    }

    public void setMallLogo(String mallLogo){
        this.mallLogo = mallLogo;
    }

    public String getGoodsNum(){
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum){
        this.goodsNum = goodsNum;
    }

    public String getMallSold(){
        return mallSold;
    }

    public void setMallSold(String mallSold){
        this.mallSold = mallSold;
    }

    public java.util.Date getCreatetime(){
        return createtime;
    }

    public void setCreatetime(java.util.Date createtime){
        this.createtime = createtime;
    }
}
