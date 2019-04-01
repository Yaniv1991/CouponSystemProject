//Ok

package com.database;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private int id;
	private String firstName,lastName, password,email;
	private List<Coupon> coupons = new ArrayList<>();
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Customer(int id, String custName, String password, List<Coupon> coupons) {
		super();
		this.id = id;
		this.firstName = custName;
		this.password = password;
		this.coupons = coupons;
	}

	public Customer(int id, String firstName, String lastName, String password, String email, List<Coupon> coupons) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.coupons = coupons;
	}

	public Customer() {
		super();
	}

	public Customer(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", email=" + email + ", coupons=" + coupons + "]";
	}

	public void addCoupon(Coupon coupon) {
		coupons.add(coupon);
	}
	public void removeCoupon(Coupon coupon) {
		coupons.remove(coupon);
	}

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
		Customer other = (Customer) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
