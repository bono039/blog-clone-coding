package me.bono039.springbootblog.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) {   // 뷰로 데이터 넘겨주는 모델 객체
        Person exPerson = new Person();
        exPerson.setId(1L);
        exPerson.setName("홍길동");
        exPerson.setAge(11);
        exPerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", exPerson); // Person 객체 처장
        model.addAttribute("today", LocalDate.now());

        return "example";   // example.html이라는 뷰 조회
    }

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}
