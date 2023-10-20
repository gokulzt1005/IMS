package com.example.demo.Dto;

import java.time.LocalDate;
import java.util.Date;

public class OutProductDto {
	
	private String productId;
    private String customerName;
	private String customerMobile;
    private int exitQuantity;
    private String TransactionType;
    private Date outDate;
    private boolean isdeleted;
    
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	public int getExitQuantity() {
		return exitQuantity;
	}
	public void setExitQuantity(int exitQuantity) {
		this.exitQuantity = exitQuantity;
	}
	public String getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public boolean isIsdeleted() {
		return isdeleted;
	}
	public void setIsdeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}
    
    

    
    

}
