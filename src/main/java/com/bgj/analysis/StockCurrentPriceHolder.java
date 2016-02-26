package com.bgj.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bgj.dao.GhlhDAO;
import com.bgj.dao.StockFileAccessingUtil;
import com.bgj.dao.StockdailyinfoVO;
import com.bgj.strategy.StrategyQueryStockBean;
import com.bgj.util.Constants;
import com.bgj.util.DateUtil;
import com.bgj.util.StrateFilePath;
import com.common.util.OSUtil;

public class StockCurrentPriceHolder {
	private static Logger logger = Logger.getLogger(GhlhDAO.class);

	private static StockCurrentPriceHolder instance = new StockCurrentPriceHolder();

	public static StockCurrentPriceHolder getInstance() {
		return instance;
	}

	private long lastModified;

	private Map<String, StrategyQueryStockBean> map;

	private StockCurrentPriceHolder() {
	}

	private void initMap(File latestSpjFile) {
		try {
			List<StrategyQueryStockBean> list = StockFileAccessingUtil
					.getStockInfoFromFile(latestSpjFile);
			map = new HashMap<String, StrategyQueryStockBean>();
			for (int i = 0; i < list.size(); i++) {
				StrategyQueryStockBean sqsb = list.get(i);
				map.put(sqsb.getStockId(), sqsb);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initTodayLatestCurrentPrice() {
		File latestSpjFile = getLatestSpjFile();
		Date now = new Date();
		List<StockdailyinfoVO> list = listStockDailyInfo(now);
		try {
			StockFileAccessingUtil.writeStocksIntoFile(list, latestSpjFile);
		} catch (IOException e) {
			logger.error("Init latestSPJ file throw exception : ", e);
		}
	}

	public static void main(String[] args) {
		StockCurrentPriceHolder.getInstance().initTodayLatestCurrentPrice();
		System.out.println(StockCurrentPriceHolder.getInstance().getLatestSPJ(
				"000016"));
	}

	private File getLatestSpjFile() {
		String latestSpjFileName = StrateFilePath.getInstance().getRootPath()
				+ OSUtil.getOSSeparator() + Constants.WEB_INF
				+ OSUtil.getOSSeparator() + Constants.CLASSES
				+ OSUtil.getOSSeparator() + Constants.LATEST_SPJ;

		File latestSpjFile = new File(latestSpjFileName);
		if (!latestSpjFile.exists()) {
			try {
				latestSpjFile.createNewFile();
			} catch (IOException e) {
				logger.error("Init latestSPJ file throw exception : ", e);
			}
		}
		return latestSpjFile;
	}

	private List<StockdailyinfoVO> listStockDailyInfo(Date now) {
		String today = DateUtil.formatDay(now);
		String sql = "SELECT * FROM stockdailyinfo WHERE DATE LIKE '" + today
				+ "%'";
		List<StockdailyinfoVO> list = GhlhDAO.list(sql,
				"com.bgj.dao.StockdailyinfoVO");
		return list;
	}

	public double getLatestSPJ(String stockId) {
		File latestSpjFile = getLatestSpjFile();
		if (map == null || lastModified != latestSpjFile.lastModified()) {
			synchronized (this) {
				initMap(latestSpjFile);
				latestSpjFile = getLatestSpjFile();
				lastModified = latestSpjFile.lastModified();
			}
		}
		double result = 0;
		if (map.get(stockId) != null) {
			result = map.get(stockId).getDqj();
		}
		return result;
	}
}
