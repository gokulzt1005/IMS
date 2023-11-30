package com.example.demo.Entity;


import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="db_database")
public class ProductInventoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "productId", unique = true)
	private String productId;
	
	@Column(name="email")
	private String email;
	
	@Column(name="productName")
	private String productName;
	
	@Column(name="productType")
	private String productType;
	
	@Column(name="employeeName")
	private String employeeName;
	
	@Column(name="customerName")
	private String customerName;
	
	@Column(name="customerMobile")
	private String customerMobile;
	
	@Column(name="processorName")
	private String processorName;
	
	@Column(name="SSDCapacity")
	private String SSDCapacity;
	
	@Column(name="ram")
	private String ram;
	
	@Column(name="expandableMemory")
	private String expandableMemory;		
		
	@Column(name="TransactionType")
	private String TransactionType;
	
	@Column(name="inDate")
	private LocalDate inDate;
	
	@Column(name="outDate")
	private LocalDate outDate;
	
	@Column(name="Description")
	private String Description;
	
	@Column(name="exitQuantity")
	private int exitQuantity;
	
	@Column(name="totalQuantity")
	private int totalQuantity;
	
	@Column(name="prices")
	private double prices;
	
	@Column(name="isdeleted")
	private boolean isdeleted;

	public ProductInventoryEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	public LocalDate getInDate() {
		return inDate;
	}

	public void setInDate(LocalDate inDate) {
		this.inDate = inDate;
	}

	public LocalDate getOutDate() {
		return outDate;
	}

	public void setOutDate(LocalDate outDate) {
		this.outDate = outDate;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getExitQuantity() {
		return exitQuantity;
	}

	public void setExitQuantity(int exitQuantity) {
		this.exitQuantity = exitQuantity;
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

	public boolean isIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(boolean isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	
	
	
	

}