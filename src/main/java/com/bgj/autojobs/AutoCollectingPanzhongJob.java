package com.bgj.autojobs;

import java.util.Calendar;
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

public class AutoCollectingPanzhongJob implements Job {
	private static Logger logger = Logger
			.getLogger(AutoCollectingPanzhongJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		long time = Calendar.getInstance().getTimeInMillis();
		EventRecorder.recordEvent(this.getClass(), "开始盘后处理");
		if (StockMarketUtil.isMarketRest()) {
			EventRecorder.recordEvent(this.getClass(),
					"今天假日休市,结束盘后处理");
			return;
		}
		EventRecorder.recordEvent(this.getClass(), "Start to collect data");
		collectStockDailyInfo();
		EventRecorder.recordEvent(this.getClass(), "Finish to collect data");
		logger.info("It took "
				+ ((Calendar.getInstance().getTimeInMillis() - time) / 1000)
				+ " seconds");
		System.out.println("It took "
				+ ((Calendar.getInstance().getTimeInMillis() - time) / 1000)
				+ " seconds");

	}

	private void collectStockDailyInfo() {
		Date now = new Date();
		collectStockDailyInfo(now);
	}

	private void collectStockDailyInfo(Date now) {
		//EventRecorder.recordEvent(this.getClass(), "开始收集收盘数据");
		String today = DateUtil.formatDayWithHourAndMinute(now);
		for (int i = 0; i < 5; i++) {
			new DataCollector().collectDailyInfo(now, true);
			String sql = "SELECT COUNT(*) FROM stockdailyinfo_panzhong WHERE "
					+ "(stockid = '000001' OR stockid = '600036' OR stockid = '000002') AND DATE LIKE '"
					+ today + "%'";
			String value = GhlhDAO.selectSingleValue(sql);
			int count = Integer.parseInt(value);
			if (count != 0) {
				EventRecorder.recordEvent(this.getClass(), "第 " + (i + 1)
						+ "收集成功");
				break;
			} else {
				if (i == 4) {
					EventRecorder.recordEvent(this.getClass(), "第 " + (i + 1)
							+ "收集 还是没有成功!");
				}
			}
		}
		//EventRecorder.recordEvent(this.getClass(), "结束收集收盘数据");
	}

	public static void main(String[] args) {
		long times = Calendar.getInstance().getTimeInMillis();
		// ConnectionPool
		// .setFULLDBURL("jdbc:mysql://583fc8005bba7.sh.cdb.myqcloud.com:7337/bgj?user=bgj&password=KLine123&useUnicode=true&characterEncoding=UTF-8");

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
		ConnectionPool.setDBURL("10.66.137.11");
		AutoCollectingPanzhongJob job = new AutoCollectingPanzhongJob();
		try {
			System.out.println("first time execute the job");
			job.execute(null);
			System.out.println("first time execute the job end");
			
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("second time execute the job");
			job.execute(null);
			System.out.println("second time execute the job end");
			
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		AutoCollectingAfterCloseJob job1 = new AutoCollectingAfterCloseJob();
		try {
			System.out.println("close job");
			job1.execute(null);
			System.out.println("close job is end");

		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
