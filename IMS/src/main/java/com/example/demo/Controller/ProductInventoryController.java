package com.example.demo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Dto.InProductDto;
import com.example.demo.Dto.OutProductDto;
import com.example.demo.Entity.ProductInventoryEntity;
import com.example.demo.Service.ProductInventoryService;
import com.google.zxing.WriterException;


@RestController
@RequestMapping("/v1/api")
public class ProductInventoryController {
	
	@Autowired
    private ProductInventoryService ProductService;
	
	@PostMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] generateAndSaveQRCode(@RequestBody ProductInventoryEntity qrCodeEntity) throws WriterException, IOException {
	    ProductInventoryEntity createdEntity = ProductService.createQRCode(qrCodeEntity);
	    int entityId = createdEntity.getId();
	    String content = "WMSGUDU202300-" + entityId; 
	    return ProductService.generateQRCode(content);
	}
	
	
	@PostMapping(path = "/scan")
	public ResponseEntity<Map<String, Object>> scanQRCode(@RequestBody String scannedContent) {
	    Map<String, Object> response = new HashMap<>();

	    if (ProductService.isValidContent(scannedContent)) {
	        response.put("message", "QR code scanned successfully.");
	        response.put("data", new ArrayList<String>()); 
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("message", "Invalid QR code content.");
	        response.put("data", null);
	        return ResponseEntity.badRequest().body(response);
	    }
	}
	
	
	@GetMapping("/total_qnty")
	public ResponseEntity<Map<String, Object>> calculate() {
        try {
            Map<String, Object> summary = ProductService.calculate();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
		@PostMapping("/add")
	    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody InProductDto inProductDto) {
	        return ProductService.AddProduct(inProductDto);
	    }
		
		@PutMapping("/{productId}")
		public ResponseEntity<Map<String, Object>> updateProduct(
		            @PathVariable String productId,
		            @RequestBody OutProductDto updatedProductDto) {
		    return ProductService.updateProduct(productId, updatedProductDto);
	   }	 
	
	
		@GetMapping("/sale")
		public ResponseEntity<Map<String, Object>> getSale() {
		    List<ProductInventoryEntity> sale = ProductService.saleProduct();
		    Map<String, Object> response = new LinkedHashMap<>();
		    
		    Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully");;
		    
		    
		    response.put("data", sale);
		    response.put("meta", meta);
		    response.put("pagination", new LinkedHashMap<>()); 
		    return ResponseEntity.ok(response);
		}


	    @GetMapping("/unsale")
	    public ResponseEntity<Map<String, Object>> unSale() {
		    List<ProductInventoryEntity> sale = ProductService.UnsaleProduct();
		    Map<String, Object> response = new LinkedHashMap<>();
		    
		    Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully");;
		    
		    
		    response.put("data", sale);
		    response.put("meta", meta);
		    response.put("pagination", new LinkedHashMap<>()); 
		    return ResponseEntity.ok(response);
		}

	    
	    @GetMapping("/product/{productName}")
	    public ResponseEntity<Map<String, Object>> getProductTypesByProductName(@PathVariable String productName) {
	        try {
	            List<String> productTypes = ProductService.getProductTypesByProductName(productName);

	            if (productTypes.isEmpty()) {
	                return ResponseEntity.notFound().build();
	            }

	            Map<String, Object> response = new LinkedHashMap<>();
	            List<Map<String, String>> data = new ArrayList<>();

	            Set<String> uniqueProductTypes = new HashSet<>();

	            for (String productType : productTypes) {
	                if (uniqueProductTypes.add(productType)) {
	                    Map<String, String> productTypeMap = new LinkedHashMap<>();
	                    productTypeMap.put("productType", productType);
	                    data.add(productTypeMap);
	                }
	            }

	            Map<String, Object> meta = new LinkedHashMap<>();
	            meta.put("status_code", 200);
	            meta.put("message", "Successfully");

	            response.put("data", data);
	            response.put("meta", meta);
	            response.put("pagination", new LinkedHashMap<>());
	            
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            Map<String, Object> errorResponse = new LinkedHashMap<>();
	            Map<String, Object> meta = new LinkedHashMap<>();
	            meta.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
	            meta.put("message", "Error retrieving product types: " + e.getMessage());
	            errorResponse.put("meta", meta);
	            errorResponse.put("data", Collections.emptyList());
	            errorResponse.put("pagination", new LinkedHashMap<>());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(errorResponse);
	        }
	    }

	    
	    
	    @GetMapping("/{productType}")
	    public ResponseEntity<Map<String, Object>> getByProductType(@PathVariable String productType) {
	        List<ProductInventoryEntity> entities = ProductService.getProductTypeAndIsdeleted(productType, false);

	        if (entities.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }

	        Map<String, Object> response = new LinkedHashMap<>();
	        Map<String, Object> meta = new LinkedHashMap<>();
	        meta.put("status_code", 200);
	        meta.put("message", "Successfully");

	        response.put("data", entities);
	        response.put("meta", meta);
	        response.put("pagination", new LinkedHashMap<>());

	        return ResponseEntity.ok(response);
	    }
	    
	    @GetMapping("/total")
	    public ResponseEntity<Map<String, Object>> getProductCounts(@RequestParam String productName) {
	        try {
	            Map<String, Object> response = ProductService.getProductCounts(productName);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            Map<String, Object> errorResponse = new LinkedHashMap<>();
	            Map<String, Object> meta = new LinkedHashMap<>();
	            meta.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
	            meta.put("message", "Error retrieving product counts: " + e.getMessage());
	            errorResponse.put("meta", meta);
	            errorResponse.put("data", Collections.emptyMap());
	            errorResponse.put("pagination", new LinkedHashMap<>());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(errorResponse);
	        }
	    }

	    
	    @GetMapping("/get/{productType}")
	    public ResponseEntity<Map<String, Object>> getProductInfoByProductType(@PathVariable String productType) {
	        try {
	            List<ProductInventoryEntity> productEntities = ProductService.getProductTypeAndIsdeleted(productType);

	            if (productEntities.isEmpty()) {
	                return ResponseEntity.notFound().build();
	            }

	            Map<String, Object> response = new LinkedHashMap<>();
	            List<Map<String, Object>> data = new ArrayList<>();

	            for (ProductInventoryEntity entity : productEntities) {
	                Map<String, Object> productInfoMap = new LinkedHashMap<>();
	                productInfoMap.put("productId", entity.getProductId());
	                productInfoMap.put("productName", entity.getProductName());
	                productInfoMap.put("productType", entity.getProductType());
	                productInfoMap.put("Description", entity.getDescription());
	                productInfoMap.put("processorName", entity.getProcessorName());
	                productInfoMap.put("ssdcapacity", entity.getSSDCapacity());
	                productInfoMap.put("ram", entity.getRam());
	                productInfoMap.put("expandableMemory", entity.getExpandableMemory());
	                productInfoMap.put("prices", entity.getPrices());
	                data.add(productInfoMap);
	            }

	            Map<String, Object> meta = new LinkedHashMap<>();
	            meta.put("status_code", 200);
	            meta.put("message", "Successfully");

	            response.put("data", data);
	            response.put("meta", meta);
	            response.put("pagination", new LinkedHashMap<>());

	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            Map<String, Object> errorResponse = new LinkedHashMap<>();
	            Map<String, Object> meta = new LinkedHashMap<>();
	            meta.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
	            meta.put("message", "Error retrieving product information: " + e.getMessage());
	            errorResponse.put("meta", meta);
	            errorResponse.put("data", Collections.emptyList());
	            errorResponse.put("pagination", new LinkedHashMap<>());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(errorResponse);
	        }
	    }
   
	    @GetMapping("/getall")
	    public List<ProductInventoryEntity> allProduct(ProductInventoryEntity inProductDto) {
	        return ProductService.getsProduct(inProductDto);
	    }
	  
	

	
}