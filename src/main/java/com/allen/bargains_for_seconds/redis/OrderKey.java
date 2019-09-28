package com.allen.bargains_for_seconds.redis;

public class OrderKey extends BasePrefix{

	private OrderKey(String prefix) {
		super(prefix);
	}
	public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");

}
