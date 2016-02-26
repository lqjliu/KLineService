package com.bgj.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.bgj.strategy.StrategyQueryStockBean;

public class StockFileAccessingUtil {

	public static String convertToLine(StockdailyinfoVO sVO) {
		String line = sVO.getStockid() + " # ";
		line += (sVO.getName() + " # ");
		line += (sVO.getTodayopenprice() + "#");
		line += (sVO.getCurrentprice() + "#");
		line += (sVO.getZdf() + "#");
		line += (sVO.getZde() + "#");
		line += (sVO.getZf() + "#");
		line += sVO.getHsl();
		return line;
	}

	public static void writeStocksIntoFile(List<StockdailyinfoVO> list,
			File stategyFile) throws IOException {
		FileWriter fileWriter = new FileWriter(stategyFile);
	
		for (int i = 0; i < list.size(); i++) {
			StockdailyinfoVO sVO = list.get(i);
			String line = convertToLine(sVO);
			fileWriter.write(line + "\r\n");
		}
		fileWriter.close();
	}

	public static StrategyQueryStockBean parseStrategyQueryStockBean(String line) {
		Pattern pattern = Pattern.compile("#");
		String[] strategyStockInfo = pattern.split(line);
		StrategyQueryStockBean result = new StrategyQueryStockBean();
		result.setStockId(strategyStockInfo[0].trim());
		result.setName(strategyStockInfo[1].trim());
		result.setKpj(Double.parseDouble(strategyStockInfo[2].trim()));
		result.setDqj(Double.parseDouble(strategyStockInfo[3].trim()));
		result.setZdf(Double.parseDouble(strategyStockInfo[4].trim()));
		result.setZde(Double.parseDouble(strategyStockInfo[5].trim()));
		result.setZf(Double.parseDouble(strategyStockInfo[6].trim()));
		result.setHsl(Double.parseDouble(strategyStockInfo[7].trim()));
		return result;
	}

	public static List<StrategyQueryStockBean> getStockInfoFromFile(
			File strategyStockFile) throws FileNotFoundException, IOException {
		List<StrategyQueryStockBean> result = new ArrayList<StrategyQueryStockBean>();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(strategyStockFile)));
		String line = bufferedReader.readLine();
		while (line != null && !line.trim().equals("")) {
			StrategyQueryStockBean msb = new StrategyQueryStockBean();
			msb = parseStrategyQueryStockBean(line);
			result.add(msb);
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
		return result;
	}

	
	public static void main(String[] args){
		File file = new File("c:\\YZZT_2015-05-04");
		try {
			List list = getStockInfoFromFile(file);
			System.out.println(list.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
