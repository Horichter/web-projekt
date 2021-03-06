package de.thkoeln.web.projekt.holzrichter.controller;

import de.thkoeln.web.projekt.holzrichter.models.Post;
import de.thkoeln.web.projekt.holzrichter.models.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private static final String FOLDER_UPLOAD = "C://Users//Admin//Desktop//web//images//";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String getIndex(Model model){
        List<Post> posts = (List<Post>) postRepository.findAll();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/newPost")
    public String newPost(){
        return "newPost";
    }

    @PostMapping("/newPost")
    public String formAddPost(Model model,
                              @RequestPart("date") String dateInput,
                              @RequestPart("title") String title,
                              @RequestPart("price") String price,
                              @RequestPart("description")String description,
                              @RequestPart("image") MultipartFile image,
                              RedirectAttributes redirectAttributes) throws ParseException, IOException {
        Post p = new Post();
        Date date = formatter.parse(dateInput);
        p.setSubmissionDate(date);
        p.setTitle(title);
        p.setPrice(Double.parseDouble(price));
        p.setDescription(description);
        byte[] imageByteArray = image.getBytes();
        Path path = Paths.get(FOLDER_UPLOAD + /*image.getOriginalFilename()*/ postRepository.count() + ".jpeg");
        p.setImageName(/*image.getOriginalFilename()*/postRepository.count() + ".jpeg");

        Files.write(path, imageByteArray);
        postRepository.save(p);

        redirectAttributes.addFlashAttribute("title", title);
        redirectAttributes.addFlashAttribute("message", "Post erfolgreich erstellt! Titel lautet:");

        return "redirect:/newPost";
    }

    @GetMapping("/details/{id}")
    public String getDetails(Model model, @PathVariable(value = "id")Long postID){
        Optional<Post> post = postRepository.findById(postID);
        model.addAttribute("post", post);
        return "/details";
    }
}