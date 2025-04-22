package com.fernando.ms.posts.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//mport org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
public class TwitterClonePostsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterClonePostsServiceApplication.class, args);
	}

}
