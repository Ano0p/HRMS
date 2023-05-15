package com.myblog.controller;

import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.service.PostService;
import com.myblog.service.impl.PostServiceImpl;
import com.myblog.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")

public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    //http://localhost:8080/api/posts
    @PostMapping
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldError());
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);

    }

    //http://localhost:8080/api/posts
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam (value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam (value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam (value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam (value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false)String sortDir
    ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);

    }

    //http://localhost:8080/api/posts/100
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        PostDto dto = postService.getPostById(id);
        return ResponseEntity.ok(dto);
    }
//    @PutMapping("/api/v1/posts/{id}")
//    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,
//                                              @PathVariable(name = "id") long id){
//        PostDto postResponse = postService.updatePost(postDto, id);
//        return new ResponseEntity<>(postResponse, HttpStatus.OK);
//    }
//    // delete post rest api
//    @DeleteMapping("/api/v1/posts/{id}")
//    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
//        postService.deletePostById(id);
//        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
//    }
}

