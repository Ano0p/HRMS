package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post newPost = (Post) postRepository.save(post);

        PostDto newPostDto = mapToDto(newPost);
        return newPostDto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> content = posts.getContent();
        List<PostDto> contents = content.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(contents);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements((int) posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );

        return mapToDto(post);
    }



//    @Override
//    public PostDto updatePost(PostDto postDto, long id) {
//        return null;
//    }

    Post mapToEntity(PostDto postDto){

        Post post = mapper.map(postDto, Post.class);

//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }

    PostDto mapToDto(Post post){

        PostDto postDto = mapper.map(post, PostDto.class);
//
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
         return postDto;
    }
}
