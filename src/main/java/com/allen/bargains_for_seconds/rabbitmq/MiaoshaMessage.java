package com.allen.bargains_for_seconds.rabbitmq;


import com.allen.bargains_for_seconds.domain.User;

public class MiaoshaMessage {
	private User user;
	private long goodsId;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
}
