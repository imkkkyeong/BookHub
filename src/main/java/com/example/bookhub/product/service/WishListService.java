package com.example.bookhub.product.service;

import com.example.bookhub.product.mapper.WishListMapper;
import com.example.bookhub.user.mapper.UserMapper;
import com.example.bookhub.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final UserMapper userMapper;
    private final WishListMapper wishListMapper;

    public void addWishList(List<Long> bookNoList, String userId) {
        User user = userMapper.selectUserById(userId);

        for (Long bookNo : bookNoList) {
            createWishList(bookNo, userId);
        }
    }

    public void createWishList(long bookNo, String userId) {
        Map map = getUserAndMap(bookNo, userId);
        wishListMapper.createWishList(map);
    }

    public void deleteWishList(long bookNo, String userId) {
        Map map = getUserAndMap(bookNo, userId);
        wishListMapper.deleteWishList(map);
    }

    public Map<String, Object> getUserAndMap(long bookNo, String userId){
        User user = userMapper.selectUserById(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("bookNo", bookNo);
        map.put("userNo", user.getNo());
        return map;
    }

    public String getWishListYn(long bookNo, String userId) {
        long userNo = 0;
        String wishListYn = "N";

        if(!"guest".equals(userId)) {
            User user = userMapper.selectUserById(userId);
            userNo = user.getNo();
        }

        Optional<Long> optional = wishListMapper.getWishListYn(bookNo, userNo);
        System.out.println(optional);
        if(!optional.isEmpty())
            wishListYn = "Y";
        return wishListYn;
    }
}
