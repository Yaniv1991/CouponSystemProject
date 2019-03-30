package com.database;

import java.util.Date;

public class Coupon implements SqlInteractor {
	private long id;
	private int amount;
	private String title, message;
	private CouponType couponType;

	private double price;
	private Date startDate, endDate;
	private String image;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coupon other = (Coupon) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", amount=" + amount + ", title=" + title + ", message=" + message + ", couponType="
				+ couponType + ", price=" + price + ", startDate=" + startDate + ", endDate=" + endDate + ", image="
				+ image + "]";
	}
	
	private java.sql.Date getSqlStartDate(){
		return (java.sql.Date) this.startDate;
	}
	private java.sql.Date getSqlEndDate(){
		return (java.sql.Date) this.endDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CouponType getCouponType() {
		return couponType;
	}

	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Coupon() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String create() {
		// ("insert into coupon "
				// + "(id,title,start_date,end_date,amount,type,message,price,image) "
				// + "values (?,?,?,?,?,?,?,?,?)");
		
		return "INSERT INTO coupon "
				+ "(id,title,start_date,end_date,amount,type,message,price,image)"
				+ "VALUES(" +id+ ",'"+ title + "'," + startDate + "," + endDate + ","
				+amount + ",'" + couponType + "','" + message + "'," + price + ",'" 
				+ image + "')" ;
	}

	@Override
	public String read() {
		// TODO Auto-generated method stub
		return "SELECT * FROM coupon WHERE id = " + id;
	}

	@Override
	public String update() {
		// TODO Auto-generated method stub
//		("update coupon set "
//		+ "title = ?, Start_date = ?, end_date = ?,"
//		+ "amount = ? , type = ? , message = ?,"
//		+ "price = ? ,image = ? where id = ?")
		
		return "update coupon set "
				+ "title = ''" + title + ", Start_date =" + getSqlStartDate() 
				+", end_date = " + getSqlEndDate() 
				+ ",amount = "+ amount + " , type = '" 
				+getCouponType() + "', message = '" + message +"',"
				+ "price = " + price
				+ " ,image = '" +image+"'"
				+ " where id = " + id;
	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		return "DELETE FROM coupon WHERE id = " + id;
	}

}
