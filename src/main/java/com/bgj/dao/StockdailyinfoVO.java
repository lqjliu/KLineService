package com.bgj.dao;

public class StockdailyinfoVO implements java.io.Serializable{
  public static String TABLE_NAME = "stockdailyinfo";
  private boolean hasStockid;
  public boolean isHasStockid(){
    return hasStockid;
  }
  private boolean whereStockid;
  public boolean isWhereStockid(){
    return this.whereStockid;
  }
  public void setWhereStockid(boolean whereStockid){
    this.whereStockid = whereStockid;
  }
  private String stockid;
  public void setStockid(String stockid){
    this.hasStockid = true;
    this.stockid = stockid;
  }
  public String getStockid(){
    return stockid;
  }
  private boolean hasDate;
  public boolean isHasDate(){
    return hasDate;
  }
  private boolean whereDate;
  public boolean isWhereDate(){
    return this.whereDate;
  }
  public void setWhereDate(boolean whereDate){
    this.whereDate = whereDate;
  }
  private java.util.Date date;
  public void setDate(java.util.Date date){
    this.hasDate = true;
    this.date = date;
  }
  public java.util.Date getDate(){
    return date;
  }
  private boolean hasName;
  public boolean isHasName(){
    return hasName;
  }
  private boolean whereName;
  public boolean isWhereName(){
    return this.whereName;
  }
  public void setWhereName(boolean whereName){
    this.whereName = whereName;
  }
  private String name;
  public void setName(String name){
    this.hasName = true;
    this.name = name;
  }
  public String getName(){
    return name;
  }
  private boolean hasCurrentprice;
  public boolean isHasCurrentprice(){
    return hasCurrentprice;
  }
  private boolean whereCurrentprice;
  public boolean isWhereCurrentprice(){
    return this.whereCurrentprice;
  }
  public void setWhereCurrentprice(boolean whereCurrentprice){
    this.whereCurrentprice = whereCurrentprice;
  }
  private double currentprice;
  public void setCurrentprice(double currentprice){
    this.hasCurrentprice = true;
    this.currentprice = currentprice;
  }
  public double getCurrentprice(){
    return currentprice;
  }
  private boolean hasZde;
  public boolean isHasZde(){
    return hasZde;
  }
  private boolean whereZde;
  public boolean isWhereZde(){
    return this.whereZde;
  }
  public void setWhereZde(boolean whereZde){
    this.whereZde = whereZde;
  }
  private double zde;
  public void setZde(double zde){
    this.hasZde = true;
    this.zde = zde;
  }
  public double getZde(){
    return zde;
  }
  private boolean hasZdf;
  public boolean isHasZdf(){
    return hasZdf;
  }
  private boolean whereZdf;
  public boolean isWhereZdf(){
    return this.whereZdf;
  }
  public void setWhereZdf(boolean whereZdf){
    this.whereZdf = whereZdf;
  }
  private double zdf;
  public void setZdf(double zdf){
    this.hasZdf = true;
    this.zdf = zdf;
  }
  public double getZdf(){
    return zdf;
  }
  private boolean hasZf;
  public boolean isHasZf(){
    return hasZf;
  }
  private boolean whereZf;
  public boolean isWhereZf(){
    return this.whereZf;
  }
  public void setWhereZf(boolean whereZf){
    this.whereZf = whereZf;
  }
  private double zf;
  public void setZf(double zf){
    this.hasZf = true;
    this.zf = zf;
  }
  public double getZf(){
    return zf;
  }
  private boolean hasHsl;
  public boolean isHasHsl(){
    return hasHsl;
  }
  private boolean whereHsl;
  public boolean isWhereHsl(){
    return this.whereHsl;
  }
  public void setWhereHsl(boolean whereHsl){
    this.whereHsl = whereHsl;
  }
  private double hsl;
  public void setHsl(double hsl){
    this.hasHsl = true;
    this.hsl = hsl;
  }
  public double getHsl(){
    return hsl;
  }
  private boolean hasTodayopenprice;
  public boolean isHasTodayopenprice(){
    return hasTodayopenprice;
  }
  private boolean whereTodayopenprice;
  public boolean isWhereTodayopenprice(){
    return this.whereTodayopenprice;
  }
  public void setWhereTodayopenprice(boolean whereTodayopenprice){
    this.whereTodayopenprice = whereTodayopenprice;
  }
  private double todayopenprice;
  public void setTodayopenprice(double todayopenprice){
    this.hasTodayopenprice = true;
    this.todayopenprice = todayopenprice;
  }
  public double getTodayopenprice(){
    return todayopenprice;
  }
  private boolean hasYesterdaycloseprice;
  public boolean isHasYesterdaycloseprice(){
    return hasYesterdaycloseprice;
  }
  private boolean whereYesterdaycloseprice;
  public boolean isWhereYesterdaycloseprice(){
    return this.whereYesterdaycloseprice;
  }
  public void setWhereYesterdaycloseprice(boolean whereYesterdaycloseprice){
    this.whereYesterdaycloseprice = whereYesterdaycloseprice;
  }
  private double yesterdaycloseprice;
  public void setYesterdaycloseprice(double yesterdaycloseprice){
    this.hasYesterdaycloseprice = true;
    this.yesterdaycloseprice = yesterdaycloseprice;
  }
  public double getYesterdaycloseprice(){
    return yesterdaycloseprice;
  }
  private boolean hasHighestprice;
  public boolean isHasHighestprice(){
    return hasHighestprice;
  }
  private boolean whereHighestprice;
  public boolean isWhereHighestprice(){
    return this.whereHighestprice;
  }
  public void setWhereHighestprice(boolean whereHighestprice){
    this.whereHighestprice = whereHighestprice;
  }
  private double highestprice;
  public void setHighestprice(double highestprice){
    this.hasHighestprice = true;
    this.highestprice = highestprice;
  }
  public double getHighestprice(){
    return highestprice;
  }
  private boolean hasLowestprice;
  public boolean isHasLowestprice(){
    return hasLowestprice;
  }
  private boolean whereLowestprice;
  public boolean isWhereLowestprice(){
    return this.whereLowestprice;
  }
  public void setWhereLowestprice(boolean whereLowestprice){
    this.whereLowestprice = whereLowestprice;
  }
  private double lowestprice;
  public void setLowestprice(double lowestprice){
    this.hasLowestprice = true;
    this.lowestprice = lowestprice;
  }
  public double getLowestprice(){
    return lowestprice;
  }
  private boolean hasCje;
  public boolean isHasCje(){
    return hasCje;
  }
  private boolean whereCje;
  public boolean isWhereCje(){
    return this.whereCje;
  }
  public void setWhereCje(boolean whereCje){
    this.whereCje = whereCje;
  }
  private double cje;
  public void setCje(double cje){
    this.hasCje = true;
    this.cje = cje;
  }
  public double getCje(){
    return cje;
  }
  private boolean hasCjl;
  public boolean isHasCjl(){
    return hasCjl;
  }
  private boolean whereCjl;
  public boolean isWhereCjl(){
    return this.whereCjl;
  }
  public void setWhereCjl(boolean whereCjl){
    this.whereCjl = whereCjl;
  }
  private double cjl;
  public void setCjl(double cjl){
    this.hasCjl = true;
    this.cjl = cjl;
  }
  public double getCjl(){
    return cjl;
  }
  private boolean hasCreatedtime;
  public boolean isHasCreatedtime(){
    return hasCreatedtime;
  }
  private boolean whereCreatedtime;
  public boolean isWhereCreatedtime(){
    return this.whereCreatedtime;
  }
  public void setWhereCreatedtime(boolean whereCreatedtime){
    this.whereCreatedtime = whereCreatedtime;
  }
  private java.util.Date createdtime;
  public void setCreatedtime(java.util.Date createdtime){
    this.hasCreatedtime = true;
    this.createdtime = createdtime;
  }
  public java.util.Date getCreatedtime(){
    return createdtime;
  }
  private boolean hasLastmodifiedtime;
  public boolean isHasLastmodifiedtime(){
    return hasLastmodifiedtime;
  }
  private boolean whereLastmodifiedtime;
  public boolean isWhereLastmodifiedtime(){
    return this.whereLastmodifiedtime;
  }
  public void setWhereLastmodifiedtime(boolean whereLastmodifiedtime){
    this.whereLastmodifiedtime = whereLastmodifiedtime;
  }
  private java.util.Date lastmodifiedtime;
  public void setLastmodifiedtime(java.util.Date lastmodifiedtime){
    this.hasLastmodifiedtime = true;
    this.lastmodifiedtime = lastmodifiedtime;
  }
  public java.util.Date getLastmodifiedtime(){
    return lastmodifiedtime;
  }
}
