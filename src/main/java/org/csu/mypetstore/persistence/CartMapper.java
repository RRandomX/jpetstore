package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    void insertItem(String userId,String itemId,int quantity,boolean inStock);

    void updataQuantity(String userId,String itemId,int quantity);

    void updataInStock(String userId,String itemId,boolean instock);

    void removeItem(String userId,String itemId);

    List<Item> getCartItems(String userId);
}
