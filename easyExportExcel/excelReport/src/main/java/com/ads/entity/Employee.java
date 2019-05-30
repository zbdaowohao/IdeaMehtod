package com.ads.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Employee {

	private String employeeId;
	private String employeeName;
	private String gender;
	private String idCard;
	private String birthDay;
	private String phone;
	private Date createDate;

}
