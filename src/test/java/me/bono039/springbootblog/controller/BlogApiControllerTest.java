package me.bono039.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bono039.springbootblog.domain.Article;
import me.bono039.springbootblog.dto.AddArticleRequest;
import me.bono039.springbootblog.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
<<<<<<< HEAD
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
=======
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
>>>>>>> 052b6c1b225930535c71df9a306851f025a8ea9e
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest         // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc   // MockMVC 생성 및 자동 구성
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;    // 직렬화, 역직렬화 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                                    .build();
        blogRepository.deleteAll();
    }

    // 블로그 글 생성 API 테스트 코드
    @DisplayName("addArticle: 블로그 글 추가 성공")
    @Test
    public void addArticle() throws Exception {
<<<<<<< HEAD
        // [given] 블로그 글 추가에 필요한 요청 객체 생성
=======
        //  [given] 블로그 글 추가에 필요한 요청 객체 생성
>>>>>>> 052b6c1b225930535c71df9a306851f025a8ea9e
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체를 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        // [when] 블로그 글 추가 API에 요청 보냄 (Type : JSON)
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // [then]
        // 1) 응답 코드가 201 Created인지 확인
        // 2) 블로그를 전체 조회해 크기가 1인지 확인
        // 3) 실제로 저장된 데이터와 요청 값 비교
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);   // 크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    // 블로그 글 목록 조회 API 테스트 코드
    @DisplayName("findAllArticles: 블로그 글 목록 조회 성공")
    @Test
    public void findAllArticles() throws Exception {
        // [given] 블로그 글 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // [when] 목록 조회 API 호출
<<<<<<< HEAD
=======
        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 반환받은 값 中 0번째 요소의 content와 title이 저장된 값과 같은지 확인
>>>>>>> 052b6c1b225930535c71df9a306851f025a8ea9e
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // [then]
<<<<<<< HEAD
        // 1. 응답 코드가 200 OK
        // 2. 반환받은 값 中 0번째 요소의 content와 title이 저장된 값과 같은지 확인
=======
>>>>>>> 052b6c1b225930535c71df9a306851f025a8ea9e
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }
<<<<<<< HEAD

    // 블로그 글 단건 조회 API 테스트 코드
    @DisplayName("findArticle: 블로그 글 단건 조회 성공")
    @Test
    public void findArticle() throws Exception {
        // [given] 블로그 글 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                                            .title(title)
                                            .content(content)
                                            .build());

        // [when] 저장한 블로그 글의 id 값으로 API 호출
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 반환받은 content와 title이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    // 블로그 글 삭제 API 테스트 코드
    @DisplayName("deleteArticle: 블로그 글 삭제 성공")
    @Test
    public void deleteArticle() throws Exception {
        // [given] 블로그 글 저장
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // [when] 저장한 블로그 글의 id 값으로 삭제 API 호출
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 블로그 글 리스트 전체 조회해 조회한 배열 크기가 0인지 확인
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.isEmpty());
    }
=======
>>>>>>> 052b6c1b225930535c71df9a306851f025a8ea9e
}