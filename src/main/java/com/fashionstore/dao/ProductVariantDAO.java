package com.fashionstore.dao;


import java.util.List;

import com.fashionstore.model.ProductVariant;

public interface ProductVariantDAO {
	boolean addProductVariant(ProductVariant productVariant);
	boolean updateProductVariant(ProductVariant productVariant);
	boolean deleteProductVariant(int variantId);
	ProductVariant getVariantById(int variantId);
	ProductVariant getVariantByProductIdAndSize(int productid,String size);
	List<ProductVariant> getVariantByProductId(int productId);
	List<ProductVariant> getAllVariants();
	boolean updateStock(int variantId,int stockQuantity);
	boolean isStockAvailable(int variantId,int requiredQuantity);
}
