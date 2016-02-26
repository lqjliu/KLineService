package com.bgj.util;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.bgj.exception.KLineSysException;
import com.common.util.FileUtil;
import com.common.util.OSUtil;

public class StrategyDirectoryUtil {
	private static Logger logger = Logger
			.getLogger(StrategyDirectoryUtil.class);

	public static String prepareStrategyDirectory(String stategyName,long year)
			throws KLineSysException {
		return prepareStrategyDirectory(stategyName, null, year);
	}

	public static String prepareStrategyDirectory(String stategyName,
			String additionalInfo, long year) throws KLineSysException {
		String strategyDirectoryPath = StrateFilePath.getInstance()
				.getRootPath()
				+ OSUtil.getOSSeparator()
				+ Constants.WEB_INF
				+ OSUtil.getOSSeparator()
				+ Constants.CLASSES
				+ OSUtil.getOSSeparator()
				+ stategyName
				+ OSUtil.getOSSeparator() + year;

		if (additionalInfo != null && !additionalInfo.equals("")) {
			strategyDirectoryPath += (OSUtil.getOSSeparator() + additionalInfo);
		}
		try {
			FileUtil.checkFile(strategyDirectoryPath, false);
		} catch (IOException e) {
			logger.error("access File throw", e);
			throw new KLineSysException("access File throw", e);
		}
		return strategyDirectoryPath + OSUtil.getOSSeparator();
	}

}
