package com.foodproject.fooddelivery.service;

import com.foodproject.fooddelivery.dto.PageProductDTO;
import com.foodproject.fooddelivery.dto.ProductDTO;
import com.foodproject.fooddelivery.entity.Product;
import com.foodproject.fooddelivery.mapper.ProductMapper;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.repository.CategoryRepository;
import com.foodproject.fooddelivery.repository.ProductRepository;
import com.foodproject.fooddelivery.service.imp.FileServiceImp;
import com.foodproject.fooddelivery.service.imp.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService implements ProductServiceImp {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FileServiceImp fileServiceImp;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseData addProduct(MultipartFile file,
                            String title,
                            Integer categoryId,
                            Integer price,
                            String description) {
        ResponseData responseData = new ResponseData();
        try{
            Product existProduct = productRepository.findByTitleAndCategoryId(title, categoryId);
            if(existProduct != null){
                responseData.setSuccess(false);
                responseData.setExist(true);
                responseData.setDescription("Product already exists");
                return responseData;
            }
            Product product = new Product();
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);
            product.setStatus(1);
            product.setCategory(categoryRepository.findById(categoryId).get());
            ResponseData responseUploadImage = fileServiceImp.uploadFileCloudinary(file);
            product.setImage(responseUploadImage.getData().toString());
            productRepository.save(product);
            responseData.setSuccess(true);
            responseData.setExist(false);
            responseData.setData(ProductMapper.toProductDTO(product));
        }catch (Exception e){
            responseData.setSuccess(false);
            responseData.setDescription("Fail");
        }
        return responseData;
    }

    @Override
    public List<ProductDTO> getHomePageProducts() {
        List<Product> products = productRepository.findAll();
        return ProductMapper.toProductDTOList(products);
    }

    @Override
    public PageProductDTO pageProducts(int page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<Product> productsPage = productRepository.findAll(pageable);
        List<ProductDTO> productDTOList = ProductMapper.toProductDTOList(productsPage.getContent());
        return new PageProductDTO(productDTOList,productsPage.getTotalPages(),productsPage.getTotalElements());
    }

    @Override
    public PageProductDTO getProductByCategoryIdAndNameAndPrice(Integer categoryId, String title, Integer priceFrom, Integer priceTo, String sortDirection, int page) {
        Sort sort=Sort.by("price");
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(page, 12, sort);
        Page<Product> listProduct=productRepository.findByCategoryIdAndTitleContainingAndPriceBetween(categoryId,title,priceFrom,priceTo,pageable);
        PageProductDTO pageProductDTO = new PageProductDTO(ProductMapper.toProductDTOList(listProduct.getContent()),listProduct.getTotalPages(),listProduct.getTotalElements());

        return pageProductDTO;
    }

    @Override
    public PageProductDTO getProductByCategoryIdAndTitleAndStatus(Integer categoryId, String title, Integer status, int page) {
        Pageable pageable = PageRequest.of(page,12);
        Page<Product> list = productRepository.findByCategoryIdAndTitleContainingAndStatus(categoryId,title,status,pageable);
        PageProductDTO pageProductDTO = new PageProductDTO(ProductMapper.toProductDTOList(list.getContent()),list.getTotalPages(),list.getTotalElements());
        return pageProductDTO;
    }

    @Override
    public ProductDTO getProductById(int id) {
        Product product=productRepository.findById(id);
        return ProductMapper.toProductDTO(product);
    }

    @Override
    public ResponseData updateProduct(MultipartFile file,
                                 int productId,
                                 String title,
                                 int categoryId,
                                 Integer price,
                                 String description,
                                 String oldImage) {
        ResponseData responseData = new ResponseData();
        Product product = productRepository.findById(productId);
        if(product==null) {
            responseData.setSuccess(false);
            responseData.setDescription("Product not found");
            return responseData;
        }
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setStatus(1);
        product.setCategory(categoryRepository.findById(categoryId));
        if(file != null && !file.isEmpty()){
            ResponseData responseUploadImage = fileServiceImp.uploadFileCloudinary(file);
            product.setImage(responseUploadImage.getData().toString());
        }
        else{
            product.setImage(oldImage);
        }
        productRepository.save(product);
        responseData.setSuccess(true);
        responseData.setDescription("Success");
        return responseData;
    }

    @Override
    public boolean deleteProduct(int id) {
        if(productRepository.existsById(id)){
            productRepository.updateProductStatusToDeleted(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean restoreProduct(int id) {
        if(productRepository.existsById(id)){
            productRepository.cancelDeletedProduct(id);
            return true;
        }
        return false;
    }
}
