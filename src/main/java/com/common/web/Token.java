package com.common.web;

public class Token {
	private int tokenType = TokenUtil.TICKET_TYPE_WECHAT;

	public int getTokenType() {
		return tokenType;
	}

	public void setTokenType(int tokenType) {
		this.tokenType = tokenType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private String openId;
	private String nickName;
	private int sex;
	private String province;
	private String city;
	private String country;

	/**
	 * Ticket length;
	 * 
	 * @return
	 */
	public int getTicketLength() {
		int result = 0;
		if (openId != null && !openId.equals("")) {
			result += (openId.getBytes().length + 3);
		}
		if (nickName != null && !nickName.equals("")) {
			result += (nickName.getBytes().length + 3);
		}
		if (province != null && !province.equals("")) {
			result += (province.getBytes().length + 3);
		}
		if (city != null && !city.equals("")) {
			result += (city.getBytes().length + 3);
		}
		if (country != null && !country.equals("")) {
			result += (country.getBytes().length + 3);
		}
		result++; // for sex

		return result;

	}

	public String toString() {
		return "tokenType = " + tokenType + " openId = " + openId
				+ " nickName = " + nickName + " province = " + province
				+ " city = " + city + " country = " + country + " sex = " + sex;
	}

	public boolean equals(Object object) {
		if (object == null || !(object instanceof Token)) {
			return false;
		}
		return this.toString().equals(object.toString());
	}

}
