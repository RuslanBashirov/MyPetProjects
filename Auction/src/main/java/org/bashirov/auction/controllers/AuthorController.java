package org.bashirov.auction.controllers;

import org.bashirov.auction.entity.Author;
import org.bashirov.auction.dao.AuthorDao;
import org.bashirov.auction.entity.Painting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorDao dao;

    @Transactional
    @GetMapping("/authors")
    public String getAllAuthorsPage(Model model){
        model.addAttribute("authorList", dao.getListOfAuthors());
        return "author/authorList";
    }

    @Transactional
    @GetMapping("/{authorId}")
    public String getCertainAuthorPage(Model model, @PathVariable int authorId){
        Author author = dao.getAuthor(authorId);
        List<Painting> listOfPaintingsOfAuthor = author.getPaintings();
        model.addAttribute("listOfPaintingsOfAuthor", listOfPaintingsOfAuthor);
        model.addAttribute("author", author);
        model.addAttribute("authorId", authorId);
        return "author/author";
    }

    @Transactional
    @GetMapping("/new")
    public String newAuthor(@ModelAttribute("newAuthor") Author newAuthor){
        return "author/new";
    }

    @Transactional
    @PostMapping("/create")
    public String createAuthor(@ModelAttribute("newAuthor") @Valid Author newAuthor,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "author/new";

        dao.save(newAuthor);
        return "redirect:/author/authors";
    }

    @Transactional
    @GetMapping("/edit/{authorId}")
    public String editCertainAuthor(Model model, @ModelAttribute("author") Author author,
                       @PathVariable int authorId){
        model.addAttribute("authorId", authorId);
        model.addAttribute("author", author);
        return "author/edit";
    }

    @Transactional
    @PatchMapping("/{authorId}")
    public String updateCertainAuthor(@ModelAttribute("author") @Valid Author updatedAuthor,
                         BindingResult bindingResult, @PathVariable int authorId){
        if (bindingResult.hasErrors()) return "author/edit";

        dao.update(authorId, updatedAuthor);

        return "redirect:/author/"+authorId;
    }

    @Transactional
    @DeleteMapping("/{authorId}")
    public String deleteCertainAuthor(@PathVariable("authorId") int authorId) {
        dao.delete(authorId);
        return "redirect:/author/authors";
    }
}
