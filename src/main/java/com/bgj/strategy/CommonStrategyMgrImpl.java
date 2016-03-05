package com.bgj.strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bgj.dao.GhlhDAO;
import com.bgj.dao.StockAnalysingAccessor;
import com.bgj.dao.StockAnalysingDataFileAccessor;
import com.bgj.dao.StockstrategyinfoVO;
import com.bgj.exception.KLineException;
import com.bgj.util.Config;
import com.bgj.util.DateUtil;

public class CommonStrategyMgrImpl implements StrategyMgr {
	private static CommonStrategyMgrImpl instance = new CommonStrategyMgrImpl();

	public static CommonStrategyMgrImpl getInstance() {
		return instance;
	}

	private CommonStrategyMgrImpl() {

	}

	public List<StrategyQueryStockBean> queryStocks(Date date,
			String strategyName) throws KLineException {
		if (Config.getInstance().isDevIntegration()) {
			StockAnalysingAccessor cars = new StockAnalysingDataFileAccessor();
			return cars.query(strategyName, date);
		} else {
			String sDate = DateUtil.formatDay(date);
			String sql = "SELECT * FROM stockstrategyinfo WHERE DATE = '"
					+ sDate + "' AND strategy = 'ZTB' ORDER BY zdf";
			List<StockstrategyinfoVO> list = GhlhDAO.list(sql,
					"com.bgj.dao.StockstrategyinfoVO");
			return convertToStockBean(list);
		}
	}

	private List<StrategyQueryStockBean> convertToStockBean(
			List<StockstrategyinfoVO> list) {
		List<StrategyQueryStockBean> result = new ArrayList<StrategyQueryStockBean>();
		for (StockstrategyinfoVO sVO : list) {
			StrategyQueryStockBean sqsb = new StrategyQueryStockBean();
			sqsb.setStockId(sVO.getStockid());
			sqsb.setName(sVO.getName());
			sqsb.setKpj(sVO.getKpj());
			sqsb.setDqj(sVO.getDqj());
			sqsb.setZde(sVO.getZde());
			sqsb.setZdf(sVO.getZdf());
			sqsb.setHsl(sVO.getHsl());
			sqsb.setZf(sVO.getZf());
			result.add(sqsb);
		}
		return result;
	}
}
