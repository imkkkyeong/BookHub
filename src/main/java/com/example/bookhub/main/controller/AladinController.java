package com.example.bookhub.main.controller;

import com.example.bookhub.main.service.AladinService;
import com.example.bookhub.main.service.SearchService;
import com.example.bookhub.main.vo.Aladin;
import com.example.bookhub.main.vo.Book;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;



@RequiredArgsConstructor
@Controller
public class AladinController {
    private final AladinService aladinService;
    private final SearchService searchService;


    //책 정보를 받아와서 mysql에 저장하는 요청 처리
    @GetMapping("/aladin")
    @ResponseBody
    public Aladin getAladinResponse() {
        aladinService.fetchItemsFromAladin();
        return null;
    }


    @GetMapping("/search")
    public String searchBooks(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Book> search = searchService.getSearchList(keyword);
        model.addAttribute("Search", search); // 모델에 책 정보를 담아서 HTML로 전달.
        return "main/searchList"; // searchList.html로 반환
    }

}
/*
    @GetMapping("/search")
    public String searchBooks1(Model model) {
        List<Book> search = searchService.getSearchBooks();
        if (!search.isEmpty()) {
            model.addAttribute("search", search);
        } else {
            model.addAttribute("noSearchResults", true);
        }
        return "main/searchList";
    }
  */


/*api에서 데이터 불러와서 html에서 바로 보이게 하는 코드
    @GetMapping("/search")
    public String searchBooksAndRenderHtml(@RequestParam("query") String query, Model model) {
        List<Book> books = searchService.searchBooks(query);
        model.addAttribute("books", books); // searchService 인스턴스를 통해 메서드 호출
        return "main/searchList"; // searchlist.html로 반환
    }
*/




