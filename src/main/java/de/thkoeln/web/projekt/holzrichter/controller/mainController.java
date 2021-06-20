package de.thkoeln.web.projekt.holzrichter.controller;

import de.thkoeln.web.projekt.holzrichter.models.Post;
import de.thkoeln.web.projekt.holzrichter.models.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class mainController {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

    @GetMapping("/newPost")
    public String newPost(){
        return "NewPost";
    }

    @PostMapping("/newPost")
    public String formAddPost(Model model,
                              @RequestPart("date") String dateInput,
                              @RequestPart("title") String title,
                              @RequestPart("price") String price,
                              @RequestPart("description")String description,
                              RedirectAttributes redirectAttributes) throws ParseException {
        Post p = new Post();
        Date date = formatter.parse(dateInput);
        System.out.println(date);
        p.setSubmissionDate(date);
        p.setTitle(title);
        p.setPrice(Double.parseDouble(price));
        p.setDescription(description);

        postRepository.save(p);

        redirectAttributes.addFlashAttribute("title", title);
        redirectAttributes.addFlashAttribute("message", "Post erfolgreich erstellt! Titel lautet:");

        return "redirect:/newPost";
    }

    @GetMapping("/all")
    public String getAll(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "all";
    }

}