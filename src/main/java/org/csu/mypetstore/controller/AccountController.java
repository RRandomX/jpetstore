package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Category;
import org.csu.mypetstore.domain.Product;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.SessionAttribute;

@RequestMapping("/account/")
@SessionAttributes({"account","myList","authenticated"})
@Controller
public class AccountController{
    @Autowired
    private AccountService accountService;
    @Autowired
    private CatalogService catalogService;

    private static final List<String> LANGUAGE_LIST;
    private static final List<String> CATEGORY_LIST;

    static {
        List<String> langList = new ArrayList<String>();
        langList.add("ENGLISH");
        langList.add("CHINESE");
        LANGUAGE_LIST = Collections.unmodifiableList(langList);

        List<String> catList = new ArrayList<String>();
        catList.add("FISH");
        catList.add("DOGS");
        catList.add("REPTILES");
        catList.add("CATS");
        catList.add("BIRDS");

        CATEGORY_LIST = Collections.unmodifiableList(catList);
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
    @GetMapping("signonForm")//点击top里的signin，进入登录界面
    public String signonForm(){
        return "account/signon";//返回一个地址
    }

    @PostMapping("signon")//登录
    public String signon(String username, String password, String vrifyCode, Model model, HttpSession session) {
        Account account = accountService.getAccount(username, password);
        String text = (String) session.getAttribute("v");
        if (text==null || !text.equalsIgnoreCase(vrifyCode)) {//equalsIgnoreCase意思是不考虑大小写
            model.addAttribute("msg", "验证码输入错误!");
            return "account/signon";
        }
        else if (account == null) {
            String msg = "Invalid username or password.  Signon failed.";
            model.addAttribute("msg", msg);
            return "account/signon";
        }else {
            account.setPassword(null);
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", account);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            return "catalog/main";
        }
    }


    @GetMapping("signoff")
    public String signoff(Model model){
        Account loginAccount = new Account();
        List<Product> myList = null;
        boolean authenticated = false;
        model.addAttribute("account",loginAccount);
        model.addAttribute("myList",myList);
        model.addAttribute("authenticated",authenticated);
        return "catalog/main";//返回一个地址
    }

    @GetMapping("editAccountForm")//这个是点击修改account信息时的跳转
    public String editAccountForm(@SessionAttribute("account") Account account , Model model) {
        model.addAttribute("account", account);
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "account/edit_account";
    }
//  public Resolution editAccountForm() {
//        return new ForwardResolution(EDIT_ACCOUNT);
//  }
    @PostMapping("editAccount")//这个是点击修改account后点击save account information时的跳转
    public String editAccount(Account account, String repeatedPassword, Model model) {
        if (account.getPassword() == null || account.getPassword().length() == 0 || repeatedPassword == null || repeatedPassword.length() == 0) {
            String msg = "密码不能为空";
            model.addAttribute("msg", msg);
            return "account/edit_account";
        } else if (!account.getPassword().equals(repeatedPassword)) {
            String msg = "两次密码不一致";
            model.addAttribute("msg", msg);
            return "account/edit_account";
        } else {
            accountService.updateAccount(account);
            account = accountService.getAccount(account.getUsername());
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated = true;
            model.addAttribute("account", account);
            model.addAttribute("myList", myList);
            model.addAttribute("authenticated", authenticated);
            String msg = "修改成功";
            model.addAttribute("msg", msg);
            return "account/edit_account";
            //return "redirect:/catalog/index";
        }
    }
//    public Resolution editAccount() {
//        accountService.updateAccount(account);
//        account = accountService.getAccount(account.getUsername());
//        myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
//        return new RedirectResolution(CatalogActionBean.class);
//    }


    @GetMapping("newAccountForm")
    public String newAccountForm(Model model){
        model.addAttribute("newAccount",new Account());
        model.addAttribute("LANGUAGE_LIST", LANGUAGE_LIST);
        model.addAttribute("CATEGORY_LIST", CATEGORY_LIST);
        return "account/new_account";
    }
    //    public Resolution newAccountForm() {
//        return new ForwardResolution(NEW_ACCOUNT);
//    }
}