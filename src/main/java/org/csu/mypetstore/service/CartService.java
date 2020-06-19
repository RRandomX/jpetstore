package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.persistence.CartMapper;
import org.csu.mypetstore.persistence.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ItemMapper itemMapper;

    public void insertItem(String userId, String itemId, int quantity,boolean isInStock){cartMapper.insertItem(userId,itemId,quantity,isInStock);}

    public void updataQuantity(String userId, String itemId, int quantity){cartMapper.updataQuantity(userId,itemId,quantity);}

    public void updataInStock(String userId, String itemId, boolean inStock){cartMapper.updataInStock(userId,itemId,inStock);}

    public void removeItem(String userId, String itemId){cartMapper.removeItem(userId,itemId);}

    public List<CartItem> getCartItems(String userId){
        List<Item> itemList = cartMapper.getCartItems(userId);

        List<CartItem> cartItemList = new ArrayList<>();

        for (Item item:itemList
        ) {
            CartItem cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(item.getQuantity());
            cartItem.setInStock(item.isInStock());
            cartItemList.add(cartItem);

        }

        return cartItemList;

    }

}
