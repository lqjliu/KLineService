package com.bgj.analysis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bgj.dao.StockdailyinfoDAO;
import com.bgj.dao.StockdailyinfoVO;
import com.bgj.exception.KLineException;
import com.bgj.util.DateUtil;
import com.bgj.util.MathUtil;

public class MNDataAnalyst {

	public final static int DAY_SIZE = 26;

	public static void analyse(Date date) throws KLineException {
		String strategyName = "MN";
		List<StockdailyinfoVO> list = DataAnalystUtil
				.getTodayProcessStockList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				StockdailyinfoVO sqb = (StockdailyinfoVO) list.get(i);
				String stockId = sqb.getStockid();
				List stockDailyInfo = StockdailyinfoDAO.getDaysInfo(stockId,
						DAY_SIZE, date);
				boolean reachCondition = true;
				for (int currentDay = 0; currentDay < 1; currentDay++) {
					MaBean maBean = calculateMA(stockDailyInfo, currentDay);

					StockdailyinfoVO sdiVO = (StockdailyinfoVO) stockDailyInfo
							.get(currentDay);
					double maPercentage = 0;
					if (maBean.getMa10() > maBean.getMa5()) {
						maPercentage = (maBean.getMa10() - maBean.getMa5())
								/ maBean.getMa5() * 100;
					} else {
						maPercentage = (maBean.getMa5() - maBean.getMa10())
								/ maBean.getMa10() * 100;

					}
					// if (sdiVO.getZdf() > 3
					// && sdiVO.getTodayopenprice() != 0 && maPercentage > 2.8
					// && ((sdiVO.getTodayopenprice() < maBean.getMa5()
					// && sdiVO.getCurrentprice() > maBean.getMa10() && maBean
					// .getMa5() <= maBean.getMa10()) || (sdiVO
					// .getTodayopenprice() < maBean.getMa10()
					// && sdiVO.getCurrentprice() > maBean.getMa5() && maBean
					// .getMa5() >= maBean.getMa10()))) {
					// KLineUtil
					// .saveUrlAs(sdiVO.getStockid(), "crossupma5ma10\\"
					// + DateUtil.formatDay(startDate),
					// sdiVO.getStockid());
					//
					// }
				}
			}
		}
	}

	private static MaBean calculateMA(List stockDailyInfo, int currentDay) {
		double ma5Sum = 0;
		int ma5SumTimes = 0;
		double ma10Sum = 0;
		int ma10SumTimes = 0;
		double currentPrice = 0;
		MaBean result = new MaBean();
		boolean dataNotEnough = false;
		for (int j = currentDay; j < stockDailyInfo.size(); j++) {
			StockdailyinfoVO sdiVO = (StockdailyinfoVO) stockDailyInfo.get(j);
			if (sdiVO.getZde() == 0) {
				continue;
			}
			result.setStockId(sdiVO.getStockid());
			if (sdiVO.getCurrentprice() != 0 && currentPrice == 0) {
				currentPrice = sdiVO.getCurrentprice();
			}
			if (sdiVO.getCurrentprice() != 0 && ma5SumTimes < 5) {
				ma5Sum += sdiVO.getCurrentprice();
				ma5SumTimes++;
			}
			if (sdiVO.getCurrentprice() != 0 && ma10SumTimes < 10) {
				ma10Sum += sdiVO.getCurrentprice();
				ma10SumTimes++;
			}
			if (ma10SumTimes >= 10) {
				break;
			}
		}
		if (ma10SumTimes >= 10) {
			double ma5 = MathUtil.formatDoubleWith2(ma5Sum / 5);
			double ma10 = MathUtil.formatDoubleWith2(ma10Sum / 10);

			result.setCurrentPrice(currentPrice);
			result.setMa10(ma10);
			result.setMa5(ma5);
		} else {
			result.setNoEnoughData(true);
		}
		return result;
	}

	public static void main(String[] args) {
		Date now = new Date();
		// try {
		// //analyse(now);
		// } catch (KLineException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		analyseMultipleDay();

	}

	private static void analyseMultipleDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 9);
		calendar.set(Calendar.DAY_OF_MONTH, 22);
		Date eachDay = calendar.getTime();
		Date now = new Date();
		while (eachDay.before(now)) {
			System.out.println("processing : " + eachDay);
			try {
				analyse(eachDay);
			} catch (KLineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			eachDay = DateUtil.getNextDay(eachDay);
		}
	}
}
