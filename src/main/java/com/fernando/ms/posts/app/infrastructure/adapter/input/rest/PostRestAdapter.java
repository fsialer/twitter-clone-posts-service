package com.fernando.ms.posts.app.infrastructure.adapter.input.rest;

import com.fernando.ms.posts.app.application.ports.input.PostDataInputPort;
import com.fernando.ms.posts.app.application.ports.input.PostInputPort;
import com.fernando.ms.posts.app.application.ports.input.PostMediaInputPort;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostDataRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostMediaRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.mapper.PostRestMapper;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreateMediaRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostDataRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.CreatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.request.UpdatePostRequest;
import com.fernando.ms.posts.app.infrastructure.adapter.input.rest.models.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/posts")
@Tag(name = "Posts", description = "Operations related to post")
public class PostRestAdapter {
   private final PostInputPort postInputPort;
   private final PostDataInputPort postDataInputPort;
   private final PostRestMapper postRestMapper;
   private final PostMediaInputPort postMediaInputPort;
   private final PostDataRestMapper postDataRestMapper;
   private final PostMediaRestMapper postMediaRestMapper;

    @GetMapping
    @Operation(summary = "Find all posts")
    @ApiResponse(responseCode = "200", description = "Found all posts")
    public Flux<PostResponse> findAll(){
        return  postRestMapper.toPostsResponse(postInputPort.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find post by id")
    @ApiResponse(responseCode = "200", description = "Found post by id")
    public Mono<ResponseEntity<PostAuthorResponse>> findById(@PathVariable String id){
        return postInputPort.findById(id)
                .flatMap(post-> Mono.just(ResponseEntity.ok(postRestMapper.toPostAuthorResponse(post))));
    }

    @PostMapping
    @Operation(summary = "Save post by user")
    @ApiResponse(responseCode = "201", description = "Saved post by user")
    public Mono<ResponseEntity<PostResponse>> save(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody  CreatePostRequest rq
    ) {

        return postInputPort.save(postRestMapper.toPost(userId,rq))
                .flatMap(post -> {
                    String location = "/v1/posts/".concat(post.getId());
                    return Mono.just(ResponseEntity.created(URI.create(location)).body(postRestMapper.toPostResponse(post)));
                });
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update post by id")
    @ApiResponse(responseCode = "200", description = "Updated post by id")
    public Mono<ResponseEntity<PostResponse>> update(@PathVariable("id") String id,@Valid @RequestBody UpdatePostRequest rq){
       return postInputPort.update(id,postRestMapper.toPost(rq))
               .flatMap(post-> Mono.just(ResponseEntity.ok().body(postRestMapper.toPostResponse(post))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete post by id")
    @ApiResponse(responseCode ="204", description = "Deleted post by id")
    public Mono<Void> delete(@PathVariable("id") String id){
       return postInputPort.delete(id);
    }

    @GetMapping("/{id}/verify")
    @Operation(summary = "Verify post by id")
    @ApiResponse(responseCode ="200", description = "Exists post by id")
    public Mono<ResponseEntity<ExistsPostResponse>> verify(@PathVariable("id") String id){
       return postInputPort.verify(id)
               .flatMap(exists->Mono.just(ResponseEntity.ok().body(postRestMapper.toExistsPostResponse(exists))));
    }

    @PostMapping("/data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Save post data by user")
    @ApiResponse(responseCode ="201", description = "Saved post data by user")
    public Mono<Void> saveData(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody CreatePostDataRequest rq
    ) {
        return postDataInputPort.save(postDataRestMapper.toPostData(userId,rq));
    }

    @DeleteMapping("/data/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode ="204", description = "Deleted post data by id")
    @Operation(summary = "Delete post data by id")
    public Mono<Void> deleteData(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("postId") String postId
    ) {
        return postDataInputPort.delete(postId,userId);
    }

    @PostMapping("/media")
    @Operation(summary = "Generate sas url for media")
    @ApiResponse(responseCode ="200", description = "Generated sas url")
    public Flux<PostMediaResponse> generateSasUrl(@Valid @RequestBody CreateMediaRequest rq){
        return postMediaRestMapper.toFluxPostMediaResponse(postMediaInputPort.generateSasUrl(postMediaRestMapper.toListString(rq)));
    }

    @GetMapping("/recent")
    @Operation(summary = "Find Post recent by user authenticated")
    @ApiResponse(responseCode ="200", description = "List post recent")
    public Flux<PostAuthorResponse> findPostRecent(@RequestHeader("X-User-Id") String userId,
                                                   @RequestParam("page") @DefaultValue("0") int page,
                                                   @RequestParam("size") @DefaultValue("20")  int size){
        return postRestMapper.toFluxPostAuthorResponse(postInputPort.recent(userId,page,size));
    }

    @GetMapping("/me")
    @Operation(summary = "Find Post recent by user authenticated")
    @ApiResponse(responseCode ="200", description = "List post recent")
    public Flux<PostAuthorResponse> findPostMe(@RequestHeader("X-User-Id") String userId,
                                                   @RequestParam("page") @DefaultValue("0") int page,
                                                   @RequestParam("size") @DefaultValue("20")  int size){
        return postRestMapper.toFluxPostAuthorResponse(postInputPort.me(userId,page,size));
    }

    @GetMapping("/count")
    @Operation(summary = "Count of post published by user")
    @ApiResponse(responseCode = "200",description = "Count post published")
    public Mono<CountPostResponse> countPostByUserId(@RequestHeader("X-User-Id") String userId){
        return postInputPort.countPostByUser(userId).flatMap(postRestMapper::toCountPostResponse);
    }

    @GetMapping("/data/count/{postId}")
    @Operation(summary = "Count of postdata by post")
    @ApiResponse(responseCode = "200",description = "Count postdata")
    public Mono<CountPostDataResponse> countPostDataByPostId(@PathVariable("postId") String postId){
        return postDataInputPort.countPostDataByPost(postId).flatMap(postDataRestMapper::toCountPostDataResponse);
    }

    @GetMapping("/data/{postId}/exists")
    @Operation(summary = "Verify exists of postdata")
    @ApiResponse(responseCode = "200",description = "Exists Postdata")
    public Mono<ExistsPostDataResponse> verifyExistsPostDataByPostIdAndUserId(@RequestHeader("X-User-Id")  String userId,@PathVariable("postId") String postId){
        return postDataInputPort.verifyExistsPostData(postId,userId).flatMap(postDataRestMapper::toExistsPostDataResponse);
    }


}
