package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.LineItem;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@RequestMapping("/order/")
@Controller
@SessionAttributes({"cart","order"})
public class OrderController {
    @Autowired
    private Cart cart;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping("viewOrder")
    public String viewOrder(Model model,HttpSession session){
        Order order = (Order) model.getAttribute("order");
        String view, msg;
        if (order != null) {
            orderService.insertOrder(order);
            model.addAttribute("cart",null);
            msg = "Thank you, your order has been submitted.";
            //更改库存
            List<LineItem> lineItems = order.getLineItems();
            for (LineItem l:lineItems) {
                int stockQuantity = catalogService.getStockQuantity(l.getItemId());
                if(stockQuantity >= l.getQuantity()){
                    catalogService.updateStockQuantity(l.getItemId(),l.getQuantity());
                }
                else{

                }
            }
           view = "order/ViewOrder";
        } else {
            msg = "An error occurred processing your order (order was null).";
            view = "common/Error";
        }
        model.addAttribute("message",msg);
        return view;
    }


    @GetMapping("newOrder")
    public String newOrderForm(Model model, HttpSession session){
        Account account = (Account)session.getAttribute("account");
            Order order = new Order();
            order.initOrder(account, cart);
            model.addAttribute("order", order);
//            //库存判断
//            List<LineItem> lineItems = order.getLineItems();
//            for (LineItem l:lineItems
//                 ) {
//                int stockQuantity = catalogService.getStockQuantity(l.getItemId());
//                if(stockQuantity < l.getQuantity()){
//                    view = "cart/Cart";
//                    return view;
//                }
//            }
//
//            view = "order/NewOrderForm";

        return "order/NewOrderForm";
    }

    @PostMapping("confirmOrder")
    public String confirmOrder(@RequestParam Map<String,Object> params, HttpServletRequest request, Model model){
        String shippingAddressRequired = request.getParameter("shippingAddressRequired");
        String view;
        Order order = (Order)model.getAttribute("order");
        System.out.println("order is"+order);
        order.setCardType((String)params.get("cardType"));
        order.setCreditCard((String)params.get("creditCard"));
        order.setExpiryDate((String)params.get("expiryDate"));
        model.addAttribute("order",order);

        return "order/ConfirmOrder";
    }

    @PostMapping("shipOrder")
    public String shipOrder(Model model,HttpServletRequest request, HttpServletResponse response){
        String cardType = request.getParameter("cardType");
        String creditCard = request.getParameter("creditCard");
        String expiryDate = request.getParameter("expiryDate");

        HttpSession session = request.getSession();
        Order order = (Order)session.getAttribute("order");

        order.setCardType(cardType);
        order.setCreditCard(creditCard);
        order.setExpiryDate(expiryDate);
        //覆盖原来的order
        System.out.println("order is"+order);
        model.addAttribute("order",order);
        return "order/ShippingForm";
    }

    @PostMapping("shippingAddress")
    public String updateAddress(Model model,HttpServletRequest request, HttpServletResponse response){
        String shipToFirstName = request.getParameter("shipToFirstName");
        String shipToLastName = request.getParameter("shipToLastName");
        String shipAddress1 = request.getParameter("shipAddress1");
        String shipAddress2 = request.getParameter("shipAddress2");
        String shipCity = request.getParameter("shipCity");
        String shipState = request.getParameter("shipState");
        String shipZip = request.getParameter("shipZip");
        String shipCountry = request.getParameter("shipCountry");
        System.out.println(shipCountry);


        HttpSession session = request.getSession();
        Order order = (Order)session.getAttribute("order");

        order.setShipToFirstName(shipToFirstName);
        order.setShipToLastName(shipToLastName);
        order.setShipAddress1(shipAddress1);
        order.setShipAddress2(shipAddress2);
        order.setShipCity(shipCity);
        order.setShipState(shipState);
        order.setShipZip(shipZip);
        order.setShipCountry(shipCountry);
        //覆盖原来的order
        System.out.println("order is"+order);
        model.addAttribute("order",order);
        return "order/ConfirmOrder";
    }




}