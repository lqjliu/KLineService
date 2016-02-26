package com.bgj.autojobs;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bgj.analysis.LXXDDataAnalyst;
import com.bgj.analysis.MRZTDataAnalyst;
import com.bgj.analysis.YZZTDataAnalyst;
import com.bgj.analysis.ZJXGDataAnalyst;
import com.bgj.exception.KLineException;
import com.bgj.util.EventRecorder;
import com.bgj.util.StockMarketUtil;

public class StrategyAnalysisJob implements Job {
	private static Logger logger = Logger.getLogger(StrategyAnalysisJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String message = "开始分析当日策略数据";
		EventRecorder.recordEvent(this.getClass(), message);
		if (StockMarketUtil.isMarketRest()) {
			return;
		}

		try {
			Date today = new Date();
			MRZTDataAnalyst.analyse(today);
			YZZTDataAnalyst.analyse(today);
			LXXDDataAnalyst.analyse(today);
			ZJXGDataAnalyst.analyse(today);

		} catch (KLineException e) {
			logger.error("analyse MRZT throw", e);
		}
		message = "结束分析当日策略数据";
		EventRecorder.recordEvent(this.getClass(), message);
	}

	public static void main(String[] args) {
		StrategyAnalysisJob job = new StrategyAnalysisJob();
		try {
			job.execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}