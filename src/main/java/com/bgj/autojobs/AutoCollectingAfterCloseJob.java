package com.bgj.autojobs;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bgj.analysis.LXXDDataAnalyst;
import com.bgj.analysis.MRZTDataAnalyst;
import com.bgj.analysis.QSGDataAnalyst;
import com.bgj.analysis.StockCurrentPriceHolder;
import com.bgj.analysis.YZZTDataAnalyst;
import com.bgj.analysis.ZJXGDataAnalyst;
import com.bgj.collecting.DataCollector;
import com.bgj.dao.GhlhDAO;
import com.bgj.exception.KLineException;
import com.bgj.util.DateUtil;
import com.bgj.util.EventRecorder;
import com.bgj.util.MailUtil;
import com.bgj.util.StockMarketUtil;
import com.common.db.ConnectionPool;

public class AutoCollectingAfterCloseJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoCollectingAfterCloseJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		NotificationMail notification = new NotificationMail();
		EventRecorder.recordEvent(this.getClass(), "��ʼ�̺���");
		if (StockMarketUtil.isMarketRest()) {
			EventRecorder.recordEvent(this.getClass(),
					"�����������,�����̺���");
			return;
		}
		EventRecorder.recordEvent(this.getClass(), "Start to collect data");
		collectStockDailyInfo();
		EventRecorder.recordEvent(this.getClass(), "Finish to collect data");

		EventRecorder.recordEvent(this.getClass(),
				"Start to collect latest SPJ");
		StockCurrentPriceHolder.getInstance().initTodayLatestCurrentPrice();
		System.out.println(StockCurrentPriceHolder.getInstance().getLatestSPJ(
				"000016"));
		EventRecorder.recordEvent(this.getClass(),
				"Start to collect latest SPJ");

		try {
			Date today = new Date();
			EventRecorder.recordEvent(this.getClass(), "Start to analyse MRZT");
			MRZTDataAnalyst.analyse(today);
			EventRecorder
					.recordEvent(this.getClass(), "Finish to analyse MRZT");

//			EventRecorder.recordEvent(this.getClass(), "Start to analyse YZZT");
//			YZZTDataAnalyst.analyse(today);
//			EventRecorder
//					.recordEvent(this.getClass(), "Finish to analyse YZZT");
//
//			EventRecorder.recordEvent(this.getClass(), "Start to analyse LXXD");
//			LXXDDataAnalyst.analyse(today);
//			EventRecorder
//					.recordEvent(this.getClass(), "Finish to analyse LXXD");
//
//			EventRecorder.recordEvent(this.getClass(), "Start to analyse ZJXG");
//			ZJXGDataAnalyst.analyse(today);
//			EventRecorder
//					.recordEvent(this.getClass(), "Finish to analyse ZJXG");
//			EventRecorder.recordEvent(ManulAnalysisJob.class,
//					"Start to analyse QSG");
//			QSGDataAnalyst.analyse(today);
//			EventRecorder.recordEvent(ManulAnalysisJob.class,
//					"Finish to analyse QSG");

			notification.setSuccessful(true);
		} catch (KLineException e) {
			notification.setFailedCause(e);
			notification.setSuccessful(false);
			logger.error("analyse Strategy throw", e);
		}

		try {
			MailUtil.send(notification);
		} catch (Exception e) {
			logger.error("Send Notification : ", e);
		}
		EventRecorder.recordEvent(this.getClass(), "�����̺���");
	}

	private void collectStockDailyInfo() {
		Date now = new Date();
		collectStockDailyInfo(now);
	}

	private void collectStockDailyInfo(Date now) {
		EventRecorder.recordEvent(this.getClass(), "��ʼ�ռ���������");
		String today = DateUtil.formatDay(now);
		for (int i = 0; i < 5; i++) {
			new DataCollector().collectDailyInfo(now, false);
			String sql = "SELECT COUNT(*) FROM stockdailyinfo WHERE "
					+ "(stockid = '000001' OR stockid = '600036' OR stockid = '000002') AND DATE LIKE '"
					+ today + "%'";
			String value = GhlhDAO.selectSingleValue(sql);
			int count = Integer.parseInt(value);
			if (count != 0) {
				EventRecorder.recordEvent(this.getClass(), "�� " + (i + 1)
						+ "�ռ��ɹ�");
				break;
			} else {
				if (i == 4) {
					EventRecorder.recordEvent(this.getClass(), "�� " + (i + 1)
							+ "�ռ� ����û�гɹ�!");
				}
			}
		}
		EventRecorder.recordEvent(this.getClass(), "�����ռ���������");
	}

	public static void main(String[] args) {
		// if (args == null || args.length < 3) {
		// System.out.println("Please input the parameters!");
		// System.exit(0);
		// }
		// String db_url = "kline123.mysql.rds.aliyuncs.com";
		// db_url = args[0];
		// if (db_url == null && db_url.equals("")) {
		// System.out.println("Please input the db url!");
		// System.exit(0);
		// }
		// System.out.println("db_url = " + db_url);
		// ConnectionPool.setDBURL(db_url);


		ConnectionPool.setFULLDBURL(fulldbURL);
		AutoCollectingAfterCloseJob job = new AutoCollectingAfterCloseJob();
		job.collectStockDailyInfo();

	}

}
