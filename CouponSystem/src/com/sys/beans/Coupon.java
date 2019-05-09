//Ok
package com.sys.beans;

import java.time.LocalDate;

public class Coupon {
	private int id,companyId,categoryId;
	public int getCategoryId() {
		return categoryId;  //Ok, this should give me the id of the category enum. should i set it from the outside??
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	private int amount;
	private String title, description;
	private Category category;

	private double price;
	private LocalDate startDate, endDate;
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
		return "Coupon [id=" + id + ", companyId=" + companyId + ", amount=" + amount + ", title=" + title
				+ ", description=" + description + ", category=" + category + ", price=" + price + ", startDate="
				+ startDate + ", endDate=" + endDate + ", image=" + image + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
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
	}


	public Coupon(int id) {
		super();
		this.id = id;
	}

	public Coupon(int id, int companyId, int categoryId, int amount, String title, String description,
			Category category, double price, LocalDate startDate, LocalDate endDate, String image) {
		super();
		this.id = id;
		this.companyId = companyId;
		this.categoryId = categoryId;
		this.amount = amount;
		this.title = title;
		this.description = description;
		this.category = category;
		this.price = price;
		this.startDate = startDate;
		this.endDate = endDate;
		this.image = image;
	}
	

//	@Override
//	public String create() {
//		// ("insert into coupon "
//				// + "(id,title,start_date,end_date,amount,type,message,price,image) "
//				// + "values (?,?,?,?,?,?,?,?,?)");
//		
//		return "INSERT INTO coupon "
//				+ "(id,title,start_date,end_date,amount,type,message,price,image)"
//				+ "VALUES(" +id+ ",'"+ title + "'," + startDate + "," + endDate + ","
//				+amount + ",'" + couponType + "','" + message + "'," + price + ",'" 
//				+ image + "')" ;
//	}
//
//	@Override
//	public String read() {
//		return "SELECT * FROM coupon WHERE id = " + id;
//	}
//
//	@Override
//	public String update() {
////		("update coupon set "
////		+ "title = ?, Start_date = ?, end_date = ?,"
////		+ "amount = ? , type = ? , message = ?,"
////		+ "price = ? ,image = ? where id = ?")
//		
//		return "update coupon set "
//				+ "title = ''" + title + ", Start_date =" + getSqlStartDate() 
//				+", end_date = " + getSqlEndDate() 
//				+ ",amount = "+ amount + " , type = '" 
//				+getCouponType() + "', message = '" + message +"',"
//				+ "price = " + price
//				+ " ,image = '" +image+"'"
//				+ " where id = " + id;
//	}
//
//	@Override
//	public String delete() {
//		return "DELETE FROM coupon WHERE id = " + id;
//	}

}
