package me.bono039.springbootblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import me.bono039.springbootblog.config.error.ErrorCode;
import me.bono039.springbootblog.domain.Article;
import me.bono039.springbootblog.domain.User;
import me.bono039.springbootblog.dto.AddArticleRequest;
import me.bono039.springbootblog.dto.UpdateArticleRequest;
import me.bono039.springbootblog.repository.BlogRepository;
import me.bono039.springbootblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.configuration.IMockitoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));    // 테스트 유저 지정
    }

    // [블로그 글 생성 API 테스트 코드]
    @DisplayName("addArticle: 아티클 추가 성공 !")
    @Test
    public void addArticle() throws Exception {

        // [given] 블로그 글 추가에 필요한 요청 객체 생성
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체를 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // [when] 블로그 글 추가 API에 요청 보냄 (Type : JSON)
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
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

    // [블로그 글 목록 조회 API 테스트 코드]
    @DisplayName("findAllArticles: 아티클 목록 조회 성공 !")
    @Test
    public void findAllArticles() throws Exception {
        // [given] 블로그 글 저장
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();

        // [when] 목록 조회 API 호출
        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 반환받은 값 中 0번째 요소의 content와 title이 저장된 값과 같은지 확인
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 반환받은 값 中 0번째 요소의 content와 title이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    // [블로그 글 단건 조회 API 테스트 코드]
    @DisplayName("findArticle: 아티클 단건 조회 성공 !")
    @Test
    public void findArticle() throws Exception {
        // [given] 블로그 글 저장
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        // [when] 저장한 블로그 글의 id 값으로 API 호출
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 반환받은 content와 title이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }

    // [블로그 글 삭제 API 테스트 코드]
    @DisplayName("deleteArticle: 아티클 삭제 성공 !")
    @Test
    public void deleteArticle() throws Exception {
        // [given] 블로그 글 저장
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        // [when] 저장한 블로그 글의 id 값으로 삭제 API 호출
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 블로그 글 리스트 전체 조회해 조회한 배열 크기가 0인지 확인
        List<Article> articles = blogRepository.findAll();

        assertThat(articles.isEmpty());
    }

    // [블로그 글 수정 API 테스트 코드]
    @DisplayName("updateArticle: 아티클 수정 성공 !")
    @Test
    public void updateArticle() throws Exception {
        // [given] 블로그 글 저장하고, 글 수정에 필요한 요청 객체 생성
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // [when] UPDATE API로 수정 요청 보냄. (요청 타입은 JSON이고, given절에서 미리 만든 객체를 요청 본문으로 함께 보냄)
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));


        // [then]
        // 1. 응답 코드가 200 OK
        // 2. 블로그 글 id로 조회한 후 값이 수정되었는지 확인
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    // [글 만드는 메서드]
    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }

    // [검증 로직 테스트할 테스트 코드]
    @DisplayName("addArticle: 아티클 추가 시 title이 null이면 실패")
    @Test
    public void addArticleNullValidation() throws Exception {
        // [given] 블로그 글 추가에 필요한 요청 객체 생성
        final String url = "/api/articles";
        final String title = null;  // title은 null로
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // [when] 블로그 글 추가 API에 요청 보냄
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // 요청 타입 : JSON
                .principal(principal)
                .content(requestBody));

        // [then] 응답 코드가 400 Bad Request인지 확인
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("addArticle: 아티클 추가 시 title이 10자 넘으면 실패")
    @Test
    public void addArticleSizeValidation() throws Exception {
        // [given] 블로그 글 추가에 필요한 요청 객체 생성
        Faker faker = new Faker();

        final String url = "/api/articles";
        final String title = faker.lorem().characters(11);  // title에 11자의 문자 들어가게 설정
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // [when] 블로그 글 추가 API에 요청 보냄.
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)  // 요청 타입 : JSON
                .principal(principal)
                .content(requestBody));

        // [then] 응답 코드가 400 Bad Request인지 확인
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("findArticle: 잘못된 HTTP 메서드로 아티클 조회하려고 하면 조회 실패")
    @Test
    public void invalidHttpMethod() throws Exception {
        // [given]
        final String url = "/api/articles/{id}";

        // [when]
        final ResultActions resultActions = mockMvc.perform(post(url, 1));

        // [then]
        resultActions
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }

    @DisplayName("findArticle: 존재하지 않는 아티클 조회하려고 하면 조회 실패")
    @Test
    public void findArticleInvalidArticle() throws Exception {
        // [given]
        final String url = "/api/articles/{id}";
        final long invalidId = 1;

        // [when]
        final ResultActions resultActions = mockMvc.perform(get(url, invalidId));

        // [then]
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));
    }
}