package com.example.demo.Dto;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;

public class InProductDto {
	
	private String productId;
	private String productName;
	private String productType;
	private String employeeName;
	private String customerName;
	private String processorName;
	private String SSDCapacity;
	private String ram;
	private String expandableMemory;
	private String TransactionType;
	private Date inDate;
	private String Description;
	private int totalQuantity;
	private double prices;
	
	public InProductDto() {
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getEmployeeName() {
		return employeeName;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	public String getSSDCapacity() {
		return SSDCapacity;
	}

	public void setSSDCapacity(String sSDCapacity) {
		SSDCapacity = sSDCapacity;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getExpandableMemory() {
		return expandableMemory;
	}

	public void setExpandableMemory(String expandableMemory) {
		this.expandableMemory = expandableMemory;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public double getPrices() {
		return prices;
	}

	public void setPrices(double prices) {
		this.prices = prices;
	}
	


	
	
	

}
