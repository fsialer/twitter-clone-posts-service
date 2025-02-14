package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.ExistsPostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostResponse;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.PostUserResponse;
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
@RequestMapping("/v1/posts")
public class PostRestAdapter {
   private final PostInputPort postInputPort;
   private final PostRestMapper postRestMapper;
   private final ObjectMapper objectMapper;

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
    public Mono<ResponseEntity<PostResponse>> save(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody  CreatePostRequest rq
    ) {

        return postInputPort.save(postRestMapper.toPost(Long.valueOf(userId),rq))
                .flatMap(post -> {
                    String location = "/v1/posts/".concat(post.getId());
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

    @GetMapping("/{id}/verify")
    public Mono<ResponseEntity<ExistsPostResponse>> verify(@PathVariable("id") String id){
       return postInputPort.verify(id)
               .flatMap(exists->{
                   return Mono.just(ResponseEntity.ok().body(postRestMapper.toExistsPostResponse(exists)));
               });
    }


    @GetMapping("/me")
    public Flux<PostUserResponse> findAllByPost(@RequestHeader("X-User-Id") Long userId,
                                                @RequestParam(name = "size",required = false,defaultValue = "10") Long size,
                                                @RequestParam(name = "page",required = false,defaultValue = "0") Long page){
        return postRestMapper.toPostsUserResponse(postInputPort.findAllPostMe(userId,size,page));
    }

    @GetMapping("/recent")
    public Flux<PostUserResponse> findAllPostRecent(@RequestHeader("X-User-Id") Long userId,
                                                    @RequestParam(name = "size",required = false,defaultValue = "10") Long size,
                                                    @RequestParam(name = "page",required = false,defaultValue = "0") Long page){
       return postRestMapper.toPostsUserResponse(postInputPort.findAllPostRecent(userId,size,page));
    }

}
