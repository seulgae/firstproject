package com.example.firstproject.controller;

import com.example.firstproject.entity.Article;
import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j // 로깅을 위한 골뱅이(어노테이션)
//1. 컨트롤러 생성.
public class ArticleController {

    @Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동 연결!
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    //3. 브라우저에서 접속하는 URL 주소
    public String newArticleForm(){
        return "articles/new";
        //2. View 페이지 정보
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){
        log.info(form.toString());
        // System.out.println(form.toString()); -> 로깅기능으로 대체!

        // 1. Dto를 변환! Entity!
        Article article = form.toEntity();
        log.info(article.toString());
        //System.out.println(article.toString());

        // 2. Repository에게 Entity를 DB안에 저장하게 함!
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
        //System.out.println(saved.toString());

        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model){
        log.info("id = "+ id);

        //1. id로 데이터를 가져옴!
        Article articeEntity = articleRepository.findById(id).orElse(null);
        //id 값을 통해서 찾았는데 만약에 없다면 null을 반환해라.

        //2. 가져온 데이터를 모델에 등록!
        model.addAttribute("article", articeEntity);

        //3. 보여줄 페이지를 설정!
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){
        // 1. 모든 Article을 가져온다!
        // 데이터 타입이 맞지않아 빨간줄 뜨는데 해결방법 3가지
        // 1. 우리에게 익숙한 Array List 사용
        List<Article> articleEntityList = articleRepository.findAll();
        // 2. Iterable 타입으로 맞춰준다.
        // Iterable<Article> articleEntityList = articleRepository.findAll();
        // 3. 캐스팅 해주는 방법.
        // List<Article> articleEntityList = (List<Article>)articleRepository.findAll();

        // 2. 가져온 Article 묶음을 뷰로 전달!
        model.addAttribute("articleList", articleEntityList);

        // 3. 뷰 페이지를 설정!
        return "articles/index"; // articles/index.mustache
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터를 가져오기!
        Article articleEntity =articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록
        model.addAttribute("article", articleEntity);

        // 뷰 페이지 설정
        return "articles/edit";
    }
}
