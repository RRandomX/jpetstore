package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    //这个适用于查询一个大类里的内容，用于点击图片后的展示
    List<Product> getProductListByCategory(String categoryId);

    Product getProduct(String productId);

    //搜索用到
    List<Product> searchProductList(String keywords);
}
