package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public Mono<ResponseEntity<PostResponse>> save(@Valid @RequestBody  CreatePostRequest rq){
        return postInputPort.save(postRestMapper.toPost(rq))
                .flatMap(post -> {
                    String location = "/posts/".concat(post.getId().toString());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(postRestMapper.toPostResponse(post)));
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostResponse>> update(@PathVariable("id") String id,@Valid @RequestBody UpdatePostRequest rq){
       return postInputPort.update(id,postRestMapper.toPost(rq))
               .flatMap(post->{
                   return Mono.just(ResponseEntity.ok().body(postRestMapper.toPostResponse(post)));
               });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id){
       return postInputPort.delete(id);
    }

}
