package com.bgj.autojobs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.bgj.analysis.LXXDDataAnalyst;
import com.bgj.analysis.MRZTDataAnalyst;
import com.bgj.analysis.YZZTDataAnalyst;
import com.bgj.analysis.ZJXGDataAnalyst;
import com.bgj.exception.KLineException;
import com.bgj.util.DateUtil;
import com.bgj.util.EventRecorder;
import com.bgj.util.StockMarketUtil;
import com.bgj.util.StrateFilePath;
import com.common.db.ConnectionPool;

public class ManulAnalysisJob {
	private static Logger logger = Logger.getLogger(ManulAnalysisJob.class);

	public static void main(String[] args) {
		if (args == null || args.length < 4) {
			System.out.println("Please input the parameters!");
			System.exit(0);
		}
		String db_url = "kline123.mysql.rds.aliyuncs.com";
		db_url = args[0];
		if (db_url == null && db_url.equals("")) {
			System.out.println("Please input the db url!");
			System.exit(0);
		}
		System.out.println("db_url = " + db_url);
		ConnectionPool.setDBURL(db_url);
		
		
		String rootPath = "C:\\workspace_klineservice\\KLineService\\src\\main\\webapp";
		rootPath = args[1];
		if (rootPath == null && rootPath.equals("")) {
			System.out.println("Please input the root Path!");
			System.exit(0);
		}
		System.out.println("rootPath = " + rootPath);
		StrateFilePath.getInstance().setRootPath(rootPath);
		
		String sbeginDate = args[2];
		String sendDate = args[3];
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date beginDate = null;
		Date endDate = null;
		try {
			beginDate = df.parse(sbeginDate);
			endDate = df.parse(sendDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Date today = beginDate;
		try {
			while (!today.after(endDate)) {
				if (!StockMarketUtil.isMarketOpen(today)) {
					EventRecorder.recordEvent(ManulAnalysisJob.class,
							 today + " Market is closed, so need not analysis data");
					today = DateUtil.getNextDay(today);
					continue;
				}
				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Start to analyse Strategies of " + today);

				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Start to analyse MRZT");
				MRZTDataAnalyst.analyse(today);
				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Finish to analyse MRZT");

				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Start to analyse YZZT");
				YZZTDataAnalyst.analyse(today);
				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Finish to analyse YZZT");

				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Start to analyse LXXD");
				LXXDDataAnalyst.analyse(today);
				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Finish to analyse LXXD");

				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Start to analyse ZJXG");
				ZJXGDataAnalyst.analyse(today);
				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Finish to analyse ZJXG");

				EventRecorder.recordEvent(ManulAnalysisJob.class,
						"Finish to analyse Strategies of " + today);

				today = DateUtil.getNextDay(today);
			}
		} catch (KLineException e) {
			logger.error("analyse Strategy of date: " + today + " throw ", e);
		}

	}
}
