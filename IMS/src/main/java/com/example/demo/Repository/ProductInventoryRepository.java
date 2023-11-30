package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.ProductInventoryEntity;


@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventoryEntity , Integer> {

	List<ProductInventoryEntity> findByIsdeleted(boolean isdeleted);
	ProductInventoryEntity findByProductId(String productId);
	List<ProductInventoryEntity> findByProductNameAndIsdeleted(String productName, boolean isdeleted);
	List<ProductInventoryEntity> findByProductTypeAndIsdeleted(String productType, boolean isdeleted);
	
	@Query("SELECT p FROM ProductInventoryEntity p WHERE YEAR(p.outDate) = :year AND MONTH(p.outDate) = :month AND p.email = :email")
	List<ProductInventoryEntity> findByYearAndMonthAndEmail(@Param("year") int year, @Param("month") int month, @Param("email") String email);
	List<ProductInventoryEntity> findByProductName(String productName);
	List<ProductInventoryEntity> findByProductTypeAndEmailAndIsdeleted(String productType, String email, boolean isdeleted);
	List<ProductInventoryEntity> findByEmail(String email);
	List<ProductInventoryEntity> findByProductNameAndEmailAndIsdeleted(String productName, String email, boolean b);
	List<ProductInventoryEntity> findByProductNameAndEmail(String productName, String email);

	

}
