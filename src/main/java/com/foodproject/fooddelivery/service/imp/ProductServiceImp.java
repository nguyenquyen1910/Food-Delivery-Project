package com.foodproject.fooddelivery.service.imp;

import com.foodproject.fooddelivery.dto.PageProductDTO;
import com.foodproject.fooddelivery.dto.ProductDTO;
import com.foodproject.fooddelivery.payload.ResponseData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductServiceImp {
    ResponseData addProduct(MultipartFile file,
                            String title,
                            Integer categoryId,
                            Integer price,
                            String description);

    List<ProductDTO> getHomePageProducts();
    PageProductDTO pageProducts(int page);
    PageProductDTO getProductByCategoryIdAndNameAndPrice(Integer categoryId,
                                                           String title,
                                                           Integer priceFrom,
                                                           Integer priceTo,
                                                           String sortDirection,
                                                           int page);

    PageProductDTO getProductByCategoryIdAndTitleAndStatus(Integer categoryId, String title, Integer status,int page);

    ProductDTO getProductById(int id);
    ResponseData updateProduct(MultipartFile file,
                          int productId,
                          String title,
                          int categoryId,
                          Integer price,
                          String description,
                          String oldImage);
    boolean deleteProduct(int id);
    boolean restoreProduct(int id);
}
