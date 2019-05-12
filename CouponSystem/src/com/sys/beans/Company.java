//Ok

package com.sys.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Company javabean class.<br>
 * Contains getters and setters for all attributes,<br>
 * and the methods {@code toString} and {@code equals} (as well as {@code hashCode} for the equals method).
 * @authors Yaniv Chen & Gil Gouetta
 *
 */

public class Company {
private int id;
private String name,password,email;
private List<Coupon> coupons = new ArrayList<>();
public Company() {
	super();
	// 
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public List<Coupon> getCoupons() {
	return coupons;
}
public void setCoupons(List<Coupon> coupons) {
	this.coupons = coupons;
}
public Company(int id, String compName, String password, String email, List<Coupon> coupons) {
	super();
	this.id = id;
	this.name = compName;
	this.password = password;
	this.email = email;
	this.coupons = coupons;
}
public Company(int id) {
	super();
	this.id = id;
}
@Override
public String toString() {
	return "Company [id=" + id + ", compName=" + name + ", password=" + password + ", email=" + email + ", coupons="
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
