package com.bgj.dao;

public class StockstrategyinfoVO implements java.io.Serializable{
  public static final String TABLE_NAME = "stockstrategyinfo";
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
  private boolean hasStrategy;
  public boolean isHasStrategy(){
    return hasStrategy;
  }
  private boolean whereStrategy;
  public boolean isWhereStrategy(){
    return this.whereStrategy;
  }
  public void setWhereStrategy(boolean whereStrategy){
    this.whereStrategy = whereStrategy;
  }
  private String strategy;
  public void setStrategy(String strategy){
    this.hasStrategy = true;
    this.strategy = strategy;
  }
  public String getStrategy(){
    return strategy;
  }
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
  private boolean hasKpj;
  public boolean isHasKpj(){
    return hasKpj;
  }
  private boolean whereKpj;
  public boolean isWhereKpj(){
    return this.whereKpj;
  }
  public void setWhereKpj(boolean whereKpj){
    this.whereKpj = whereKpj;
  }
  private double kpj;
  public void setKpj(double kpj){
    this.hasKpj = true;
    this.kpj = kpj;
  }
  public double getKpj(){
    return kpj;
  }
  private boolean hasDqj;
  public boolean isHasDqj(){
    return hasDqj;
  }
  private boolean whereDqj;
  public boolean isWhereDqj(){
    return this.whereDqj;
  }
  public void setWhereDqj(boolean whereDqj){
    this.whereDqj = whereDqj;
  }
  private double dqj;
  public void setDqj(double dqj){
    this.hasDqj = true;
    this.dqj = dqj;
  }
  public double getDqj(){
    return dqj;
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
  private boolean hasComments;
  public boolean isHasComments(){
    return hasComments;
  }
  private boolean whereComments;
  public boolean isWhereComments(){
    return this.whereComments;
  }
  public void setWhereComments(boolean whereComments){
    this.whereComments = whereComments;
  }
  private String comments;
  public void setComments(String comments){
    this.hasComments = true;
    this.comments = comments;
  }
  public String getComments(){
    return comments;
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
