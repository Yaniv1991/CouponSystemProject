//Ok
package com.sys.beans;

import java.time.LocalDate;

/**
 * 
 * Coupon java bean class.<br>
 * Contains getters and setters for all attributes,<br>
 * and the methods {@code toString} and {@code equals} (as well as {@code hashCode} for the equals method).
// * 
 * @authors Yaniv Chen & Gil Gouetta
 * 
 */

public class Coupon {
	private int id,companyId,categoryId;
	public int getCategoryId() {
		return categoryId;
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
		if(endDate != null) {
			if(endDate.isAfter(startDate)) {
				throw new IllegalArgumentException("Start date cannot be after end date");
			}
		}
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		if(startDate != null) {
			if(startDate.isAfter(endDate)) {
				throw new IllegalArgumentException("End date cannot be before start date");
			}
		}
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
		setStartDate(startDate);
		setEndDate(endDate);
		this.image = image;
	}
}
