package de.thkoeln.web.projekt.holzrichter.controller;


import de.thkoeln.web.projekt.holzrichter.models.Post;
import de.thkoeln.web.projekt.holzrichter.models.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class APIController {
    private static final String FOLDER_UPLOAD = "C://Users//Admin//Desktop//web//images//";

    @Autowired
    private PostRepository postRepository;


    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return (List<Post>) postRepository.findAll();
    }


    @GetMapping("/posts/{id}")
    public Post getPostByID(@PathVariable(value = "id")Long postID){
        Post post = postRepository.findById(postID).orElse(new Post());
        return post;
    }


    @GetMapping(value = "posts/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageByID(@PathVariable(value = "id")Long postID){
        Path path = Paths.get(FOLDER_UPLOAD + postRepository.findById(postID).get().getImageName());
        try{
            return Files.readAllBytes(path);
        }
        catch (IOException exception){
            System.out.println(exception);
        }
        return null;
    }
}
