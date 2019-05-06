package com.test;

import java.util.Scanner;

import com.sys.exception.CouponSystemException;

public interface Action {
String getDescription();
String inputData(Scanner sc,String property) throws CouponSystemException;
void act() throws CouponSystemException;
}
