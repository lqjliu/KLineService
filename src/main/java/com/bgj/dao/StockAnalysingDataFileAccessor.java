package com.bgj.dao;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.bgj.analysis.StockCurrentPriceHolder;
import com.bgj.exception.AppExceptionKeys;
import com.bgj.exception.KLineAppException;
import com.bgj.exception.KLineException;
import com.bgj.exception.KLineSysException;
import com.bgj.strategy.StrategyQueryStockBean;
import com.bgj.util.DateUtil;
import com.bgj.util.StrategyDirectoryUtil;
import com.common.util.FileUtil;

public class StockAnalysingDataFileAccessor implements StockAnalysingAccessor {
	private static Logger logger = Logger
			.getLogger(StockAnalysingDataFileAccessor.class);

	public void save(List<StockdailyinfoVO> list, String strategyName, Date date)
			throws KLineException {
		save(list, strategyName, date, null);
	}

	private File getStrategyFile(String strategyName, Date date)
			throws KLineSysException, IOException {
		return getStrategyFile(strategyName, date, true);
	}

	private File getStrategyFile(String strategyName, Date date,
			String additionalInfo) throws KLineSysException, IOException {
		return getStrategyFile(strategyName, date, true, additionalInfo);
	}

	private File getStrategyFile(String strategyName, Date date,
			boolean needCreate) throws KLineSysException, IOException {
		return getStrategyFile(strategyName, date, needCreate, null);
	}

	private File getStrategyFile(String strategyName, Date date,
			boolean needCreate, String additionalInfo)
			throws KLineSysException, IOException {
		String day = DateUtil.formatDay(date);
		String strategyPath = StrategyDirectoryUtil.prepareStrategyDirectory(
				strategyName, additionalInfo, DateUtil.getCurrentYear(date));
		String startegyAnalysisResultFile = strategyPath + strategyName + "_"
				+ day;
		File stategyFile = FileUtil.checkFile(startegyAnalysisResultFile, true,
				needCreate);
		return stategyFile;
	}

	public List<StrategyQueryStockBean> query(String strategyName, Date date)
			throws KLineException {
		return query(strategyName, date, null);
	}

	public List<StrategyQueryStockBean> query(String strategyName, Date date,
			String additionalInfo) throws KLineException {
		List<StrategyQueryStockBean> result = null;
		result = Cache.getInstance(strategyName).getStockFromCache(date,
				additionalInfo);
		if (result == null) {
			try {
				File strategyStockFile = getStrategyFile(strategyName, date,
						false, additionalInfo);
				if (strategyStockFile == null) {
					if (DateUtil.isToday(date)) {
						throw new KLineAppException(
								AppExceptionKeys.DATA_IS_PROCESSING);
					} else {
						throw new KLineAppException(
								AppExceptionKeys.SYSTEM_HAS_NO_DATA_OF_THIS_DAY);
					}
				}
				result = StockFileAccessingUtil
						.getStockInfoFromFile(strategyStockFile);

			} catch (IOException ex) {
				logger.error(
						"there is errow while reading monitor stocks from file",
						ex);
				throw new KLineSysException(
						"there is errow while reading monitor stocks from file",
						ex);
			}
			Cache.getInstance(strategyName).putStockIntoCache(date,
					additionalInfo, result);
		}

		assessLatestSPJ(result);
		return result;
	}

	private void assessLatestSPJ(List<StrategyQueryStockBean> result) {
		for (int i = 0; i < result.size(); i++) {
			StrategyQueryStockBean sqsb = (StrategyQueryStockBean) result
					.get(i);
			double currentPrice = StockCurrentPriceHolder.getInstance()
					.getLatestSPJ(sqsb.getStockId());
			sqsb.setLatestSpj(currentPrice);
		}
	}

	public void save(List<StockdailyinfoVO> list, String strategyName,
			Date date, String additionalInfo) throws KLineException {
		try {
			File stategyFile = getStrategyFile(strategyName, date,
					additionalInfo);
			StockFileAccessingUtil.writeStocksIntoFile(list, stategyFile);
		} catch (IOException ex) {
			logger.error("write strategy date throw", ex);
			throw new KLineSysException("write strategy date throw", ex);
		}

	}
}
