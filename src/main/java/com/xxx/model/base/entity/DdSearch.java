package com.xxx.model.base.entity;

public class DdSearch {

	private String goods_image_url;
	private String goods_id;
	private String goods_name;
	private String mall_name;
	private Integer sold_quantity;
	private Integer min_group_price;
	private Integer min_normal_price;
	private Integer rank;
	private Integer concern_id;
	private Integer isConcern;

	public String getGoods_image_url() {
		return goods_image_url;
	}

	public void setGoods_image_url(String goods_image_url) {
		this.goods_image_url = goods_image_url;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getMall_name() {
		return mall_name;
	}

	public void setMall_name(String mall_name) {
		this.mall_name = mall_name;
	}

	public Integer getSold_quantity() {
		return sold_quantity;
	}

	public void setSold_quantity(Integer sold_quantity) {
		this.sold_quantity = sold_quantity;
	}

	public Integer getMin_group_price() {
		return min_group_price;
	}

	public void setMin_group_price(Integer min_group_price) {
		this.min_group_price = min_group_price;
	}

	public Integer getMin_normal_price() {
		return min_normal_price;
	}

	public void setMin_normal_price(Integer min_normal_price) {
		this.min_normal_price = min_normal_price;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getConcern_id() {
		return concern_id;
	}

	public void setConcern_id(Integer concern_id) {
		this.concern_id = concern_id;
	}

	public Integer getIsConcern() {
		return isConcern;
	}

	public void setIsConcern(Integer isConcern) {
		this.isConcern = isConcern;
	}
}
