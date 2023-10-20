package com.example.demo.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.InProductDto;
import com.example.demo.Dto.OutProductDto;
import com.example.demo.Entity.ProductInventoryEntity;
import com.example.demo.Repository.ProductInventoryRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class ProductInventoryService {

	
	@Autowired
    private ProductInventoryRepository ProductRepository;

 
    public ProductInventoryEntity createQRCode(ProductInventoryEntity qrCodeEntity) {
        return ProductRepository.save(qrCodeEntity);
    }

    public byte[] generateQRCode(String content) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    public boolean isValidContent(String scannedContent) {
        try {
            String urlRegex = "^(http|https)://.*$";
            if (scannedContent.matches(urlRegex)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    
    

    public ResponseEntity<Map<String, Object>> AddProduct(InProductDto inProductDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> userData = new LinkedHashMap<>();

        try {
            ProductInventoryEntity inProduct = convertDtoToEntity(inProductDto);
            
            if (inProduct == null) {
                throw new IllegalArgumentException("Required fields are missing in the input");
            }

            ProductInventoryEntity savedEntity = ProductRepository.save(inProduct);

            userData.put("userName", savedEntity.getProductId());
            userData.put("boolean", true);
            
            dataList.add(userData);
            
            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully");

            response.put("data", dataList);
            response.put("meta", meta);
            response.put("pagination", new LinkedHashMap<>());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            userData.put("boolean", false);
            userData.put("message", "Duplicate productId. Failed to register");

            dataList.add(userData);

            response.put("data", dataList);
            response.put("meta", new LinkedHashMap<>());
            response.put("pagination", new LinkedHashMap<>());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            userData.put("boolean", false);
            userData.put("message", "Internal server error");

            dataList.add(userData);

            response.put("data", dataList);
         
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    private ProductInventoryEntity convertDtoToEntity(InProductDto inProductDto) {
        // Check if the required fields are missing in the input
        if (inProductDto.getProductId() == null ||
            inProductDto.getProductName() == null ||
            inProductDto.getProductType() == null ||
            inProductDto.getEmployeeName() == null ||
            inProductDto.getInDate() == null ||
            inProductDto.getDescription() == null ||
            inProductDto.getPrices() <= 0.0) {
            return null;
        }
        
        ProductInventoryEntity inProduct = new ProductInventoryEntity();
        inProduct.setProductId(inProductDto.getProductId());
        inProduct.setProductName(inProductDto.getProductName());
        inProduct.setProductType(inProductDto.getProductType());
        inProduct.setProcessorName(inProductDto.getProcessorName());
        inProduct.setSSDCapacity(inProductDto.getSSDCapacity());
        inProduct.setRam(inProductDto.getRam());
        inProduct.setExpandableMemory(inProductDto.getExpandableMemory());
        inProduct.setEmployeeName(inProductDto.getEmployeeName());
        
        // Set the fixed value "in" for TransactionType
        inProduct.setTransactionType("in");
        
        // Set other properties based on the input DTO
        inProduct.setInDate(inProductDto.getInDate());
        inProduct.setDescription(inProductDto.getDescription());
        inProduct.setTotalQuantity(1);
        inProduct.setPrices(inProductDto.getPrices());
        
        return inProduct;
    }
    
    
    
    
    public ResponseEntity<Map<String, Object>> updateProduct(String productId, OutProductDto updatedProductDto) {
        Map<String, Object> response = new LinkedHashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        if (updatedProductDto.getOutDate() == null || updatedProductDto.getCustomerName() == null ||
        		updatedProductDto.getTransactionType() == null) {
            response.put("data", dataList);
            response.put("meta", createMeta(HttpStatus.BAD_REQUEST.value(), "Insert the values"));
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.badRequest().body(response);
        }

        ProductInventoryEntity outProduct = ProductRepository.findByProductId(productId);

        if (outProduct == null) {
            response.put("data", dataList);
            response.put("meta", createMeta(HttpStatus.NOT_FOUND.value(), "Product with ID " + productId + " not found"));
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.notFound().build();
        }

        if (outProduct.isIsdeleted()) {
            response.put("data", dataList);
            response.put("meta", createMeta(HttpStatus.BAD_REQUEST.value(), "This Product Is " + productId + " already Sale"));
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.badRequest().body(response);
        }

        String requestedTransactionType = updatedProductDto.getTransactionType();

        // Customize transaction type based on your conditions
        if ("sales".equalsIgnoreCase(requestedTransactionType)) {
            outProduct.setTransactionType("out"); // Convert "sales" to "out"
        } else if ("service".equalsIgnoreCase(requestedTransactionType)) {
            outProduct.setTransactionType("service"); // Store "service" as is
        }

        outProduct.setCustomerName(updatedProductDto.getCustomerName());
        outProduct.setCustomerMobile(updatedProductDto.getCustomerMobile());
        outProduct.setOutDate(updatedProductDto.getOutDate());
        outProduct.setExitQuantity(1);

        outProduct.setIsdeleted(true);

        try {
            ProductRepository.save(outProduct);
            Map<String, Object> data = new HashMap<>();
            data.put("customerName", updatedProductDto.getCustomerName());
            data.put("outDate", updatedProductDto.getOutDate());
            data.put("isdeleted", outProduct.isIsdeleted());

            dataList.add(data);

            response.put("data", dataList);
            response.put("meta", createMeta(HttpStatus.OK.value(), "Successfully updated the product"));
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("data", dataList);
            response.put("meta", createMeta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"));
            response.put("pagination", new LinkedHashMap<>());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
        private Map<String, Object> createMeta(int statusCode, String message) {
            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("statusCode", statusCode);
            meta.put("message", message);
            return meta;
        }

    
    
        public Map<String, Object> calculate() {
            Map<String, Object> response = new LinkedHashMap<>();
            List<ProductInventoryEntity> products = ProductRepository.findAll();

            int totalExitQuantity = 0;
            int totalTotalQuantity = 0;
            double totalPrices = 0.0;

            for (ProductInventoryEntity product : products) {
                totalExitQuantity += product.getExitQuantity();
                totalTotalQuantity += product.getTotalQuantity();
                totalPrices += product.getPrices();
            }

            int saleQuantity = totalTotalQuantity - totalExitQuantity;

            List<Map<String, Object>> dataList = new ArrayList<>();
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("Total Quantity", totalTotalQuantity);
            data.put("prices", totalPrices);
            data.put("In Hand", saleQuantity);
            dataList.add(data);

            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("status_code", 200);
            meta.put("message", "Successfully");

            response.put("data", dataList);
            response.put("meta", meta);
            response.put("pagination", new LinkedHashMap<>());

            return response;
        }
        
        
        public Map<String, Object> getProductCounts(String productName) {
            Map<String, Object> response = new LinkedHashMap<>();
            List<ProductInventoryEntity> entities = ProductRepository.findByProductName(productName);

            int totalCount = entities.size();
            int availableCount = (int) entities.stream().filter(entity -> !entity.isIsdeleted()).count();

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("Total Count", totalCount);
            data.put("Available Count", availableCount);

            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("statusCode", 200);
            meta.put("message", "Successfully retrieved counts for products");

            response.put("data", data);
            response.put("meta", meta);
            response.put("pagination", new LinkedHashMap<>());

            return response;
        }
    
        
    
    public List<ProductInventoryEntity> saleProduct() {
        return ProductRepository.findByIsdeleted(true);
    }

    public List<ProductInventoryEntity> UnsaleProduct() {
        return ProductRepository.findByIsdeleted(false);
    }

    public List<String> getProductTypesByProductName(String productName) {
        List<ProductInventoryEntity> productEntities = ProductRepository.findByProductNameAndIsdeleted(productName, false);
        List<String> productTypes = new ArrayList<>();

        for (ProductInventoryEntity entity : productEntities) {
            productTypes.add(entity.getProductType());
        }

        return productTypes;
    }


	public List<ProductInventoryEntity> getProductTypeAndIsdeleted(String productType, boolean isdeleted) {
        return ProductRepository.findByProductTypeAndIsdeleted(productType, isdeleted);
	}
	
	public List<ProductInventoryEntity> getProductTypeAndIsdeleted(String productType) {
        return ProductRepository.findByProductTypeAndIsdeleted(productType, false);
    }

	public List<ProductInventoryEntity> getsProduct(ProductInventoryEntity inProductDto) {
		// TODO Auto-generated method stub
		return ProductRepository.findAll();
	}
	
	

    
    
}