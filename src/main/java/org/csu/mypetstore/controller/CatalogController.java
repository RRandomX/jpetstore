package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/catalog/")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/view")//进入主界面
    public String view(){
        return "catalog/main";
    }

    @GetMapping("/viewCategory")//点击图片等进入大类
    public String viewCategory(String categoryId,Model model){
        if(categoryId != null){
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category",category);
            model.addAttribute("productList",productList);
            return "catalog/category";//返回的是一个地址
        }
        return "catalog/main";
    }

    @GetMapping("viewProduct")
    public String viewProduct(String productId,Model model){
        if(productId != null){
            Product product = catalogService.getProduct(productId);
            List<Item> itemList = catalogService.getItemListByProduct(productId);
            product = catalogService.getProduct(productId);
            model.addAttribute("product",product);
            model.addAttribute("itemList",itemList);
            return "catalog/product";//返回的是一个地址
        }
        return "catalog/category";
    }

    @GetMapping("viewItem")
    public String viewItem(String itemId,Model model){
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();
        model.addAttribute("item",item);
        model.addAttribute("product",product);
        return "catalog/item";//返回的是一个地址
    }

    @PostMapping("searchProducts")
    public String searchProducts(String keyword,Model model){
        if(keyword == null || keyword.length() < 1){
            String msg = "Please enter a keyword to search for, then press the search button.";
            model.addAttribute("msg",msg);
            return "common/error";//返回的是一个地址
        }else {
            List<Product> productList = catalogService.searchProductList(keyword.toLowerCase());
            processProductDescription(productList);
            model.addAttribute("productList",productList);
            return "catalog/search_product";//返回的是一个地址
        }
    }
    private void processProductDescription(Product product){
        String [] temp = product.getDescription().split("\"");
        product.setDescriptionImage(temp[1]);
        product.setDescriptionText(temp[2].substring(1));
    }
    private void processProductDescription(List<Product> productList){
        for(Product product : productList) {
            processProductDescription(product);
        }
    }

//下面这个功能还没实现
//    public void clear() {
//        keyword = null;
//
//        categoryId = null;
//        category = null;
//        categoryList = null;
//
//        productId = null;
//        product = null;
//        productList = null;
//
//        itemId = null;
//        item = null;
//        itemList = null;
//    }

}
