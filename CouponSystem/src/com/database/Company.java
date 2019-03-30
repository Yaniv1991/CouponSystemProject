package com.database;

import java.util.ArrayList;
import java.util.List;

public class Company {
private long id;
private String compName,password,email;
List<Coupon> coupons = new ArrayList<>();
public Company() {
	super();
	// TODO Auto-generated constructor stub
}
public Company(long id, String compName, String password, String email, List<Coupon> coupons) {
	super();
	this.id = id;
	this.compName = compName;
	this.password = password;
	this.email = email;
	this.coupons = coupons;
}
public Company(long id) {
	super();
	this.id = id;
}
@Override
public String toString() {
	return "Company [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email + ", coupons="
			+ coupons + "]";
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
	Company other = (Company) obj;
	if (id != other.id)
		return false;
	return true;
}

public void addCoupon(Coupon coupon) {
	coupons.add(coupon);
}
public void removeCoupon(Coupon coupon) {
	coupons.remove(coupon);
}

}
