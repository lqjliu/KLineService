package com.common.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
	public static File checkFile(String strategyDirectoryPath, boolean isFile)
			throws IOException {
		return checkFile(strategyDirectoryPath, isFile, true);
	}

	public static File checkFile(String strategyDirectoryPath, boolean isFile,
			boolean needCreate) throws IOException {
		File strategyDirectory = new File(strategyDirectoryPath);
		if (!strategyDirectory.exists()) {
			if (needCreate) {
				if (isFile) {
					strategyDirectory.createNewFile();
				} else {
					strategyDirectory.mkdirs();
				}
			} else {
				strategyDirectory = null;
			}
		}
		return strategyDirectory;
	}
}
