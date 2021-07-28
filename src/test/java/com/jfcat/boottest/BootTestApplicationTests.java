package com.jfcat.boottest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class BootTestApplicationTests {

    @Test
    void contextLoads() {

        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("张散疯");
        list.add("王五");

        Stream<String> limit = list.stream()
                .limit(2);
        limit.forEach(System.out::println);

    }

}
