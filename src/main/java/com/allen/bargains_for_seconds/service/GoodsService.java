package com.allen.bargains_for_seconds.service;

import java.util.List;

import com.allen.bargains_for_seconds.dao.GoodsDao;
import com.allen.bargains_for_seconds.domain.MiaoshaGoods;
import com.allen.bargains_for_seconds.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GoodsService {
	
	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int res = goodsDao.reduceStock(g);
		return res > 0;
	}
	
	
	
}
