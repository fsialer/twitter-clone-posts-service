package com.fernanando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fernanando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernanando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostRestAdapter {
   private final PostInputPort postInputPort;
   private final PostRestMapper postRestMapper;

   @GetMapping
    public Flux<PostResponse> findAll(){
        return  postRestMapper.toPostsResponse(postInputPort.findAll());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostResponse>> findById(@PathVariable String id){
        return postInputPort.findById(id)
                .flatMap(post->{
                    return Mono.just(ResponseEntity.ok(postRestMapper.toPostResponse(post)));
                });
    }

}
