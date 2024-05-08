package com.example.bookhub.product.controller;

import com.example.bookhub.product.dto.BookDto;
import com.example.bookhub.product.dto.BuyForm;
import com.example.bookhub.product.dto.GiftReceiverForm;
import com.example.bookhub.product.service.BookService;
import com.example.bookhub.product.service.BuyService;
import com.example.bookhub.product.service.GiftService;
import com.example.bookhub.product.vo.CouponProduced;
import com.example.bookhub.user.vo.UserDelivery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product/gift")
@RequiredArgsConstructor
@SessionAttributes({"buyForm"})
public class GiftController {

    private final GiftService giftService;
    private final BookService bookService;
    private final BuyService buyService;

    @PostMapping("")
    public String gift(BuyForm buyForm){
        giftService.setGiftYn(buyForm);
        return "product/gift/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/order")
    public String getOrder(
            BuyForm buyForm,
            Principal principal,
            Model model)
    {
        UserDelivery defaultUserDelivery = null;
        List<UserDelivery> userDeliveryList = buyService.getUserDeliveryByUserNo(principal.getName());
        for(UserDelivery userDelivery : userDeliveryList){
            if("Y".equals(userDelivery.getDefaultAddressYn())) {
                defaultUserDelivery = userDelivery;
            }
        }
        model.addAttribute("userDeliveryList", userDeliveryList);
        model.addAttribute("defaultUserDelivery", defaultUserDelivery);

        List<BookDto> buyBookList = new ArrayList<>();
        for(long buyBookNo : buyForm.getBuyBookNoList()){
            BookDto buyBook = bookService.getBookDetailByNo(buyBookNo);
            buyBookList.add(buyBook);
        }
        model.addAttribute("buyBookList", buyBookList);

        List<Integer> buyBookCountList = new ArrayList<>();
        for (int buyBookCount : buyForm.getBuyBookCountList()) {
            buyBookCountList.add(buyBookCount);
        }
        model.addAttribute("buyBookCountList", buyBookCountList);

        int point = buyService.getPointByUserNo(principal.getName());
        model.addAttribute("point", point);

        // BuyForm 객체 HttpSession에 저장
        model.addAttribute("buyForm", buyForm);

        return "product/gift/order";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/coupon")
    @ResponseBody
    public ResponseEntity<List<CouponProduced>> getCoupon(Principal principal){
        List<CouponProduced> couponList = buyService.getCouponsByUserNo(principal.getName());
        return ResponseEntity.ok(couponList);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/receiver/{giftReceiverNo}")
    public String receiver(@PathVariable("giftReceiverNo") long giftReceiverNo, Model model, Principal principal){

        UserDelivery defaultUserDelivery = null;
        List<UserDelivery> userDeliveryList = buyService.getUserDeliveryByUserNo(principal.getName());
        for(UserDelivery userDelivery : userDeliveryList){
            if("Y".equals(userDelivery.getDefaultAddressYn())) {
                defaultUserDelivery = userDelivery;
            }
        }

        model.addAttribute("userDeliveryList", userDeliveryList);
        model.addAttribute("defaultUserDelivery", defaultUserDelivery);
        model.addAttribute("giftReceiveNo", giftReceiverNo);

        return "product/gift/receiverDetail";
    }

    @PostMapping("/receiver")
    public String receiver(GiftReceiverForm giftReceiverForm, Principal principal){
        giftService.updateGiftReceiver(giftReceiverForm, principal.getName());

        return "/product/gift/success";
    }
}