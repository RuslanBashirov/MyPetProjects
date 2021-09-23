package org.bashirov.auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.bashirov.auction.entity.Comment;
import org.bashirov.auction.entity.Painting;
import org.bashirov.auction.dao.CommentDao;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentDao dao;

    @Transactional
    @GetMapping("/{commentId}")
    public String getCertainCommentPage(Model model, @PathVariable int commentId){
        model.addAttribute("comment", dao.getComment(commentId));
        return "comment/comment";
    }

    @Transactional
    @GetMapping("/new/{paintingId}")
    public String newComment(Model model, @PathVariable int paintingId,
                             @ModelAttribute("newComment") Comment newComment){
        model.addAttribute("paintingId", paintingId);
        return "comment/new";
    }

    @Transactional
    @PostMapping("/create/{paintingId}")
    public String createComment(@ModelAttribute("newComment") @Valid Comment newComment,
                                @PathVariable int paintingId, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "redirect:/comment/new"+paintingId;

        Painting painting = dao.getPainting(paintingId);
        painting.add(newComment);
        dao.save(newComment);

        return "redirect:/painting/"+paintingId;
    }

    @Transactional
    @GetMapping("/edit/{commentId}")
    public String editCertainComment(Model model, @PathVariable int commentId,
                       @ModelAttribute("comment") Comment comment){
        model.addAttribute("comment", dao.getComment(commentId));
        return "comment/edit";
    }

    @Transactional
    @PatchMapping("/{commentId}")
    public String updateCertainComment(@ModelAttribute("comment") @Valid Comment updatedComment,
                         BindingResult bindingResult, @PathVariable int commentId){
        if (bindingResult.hasErrors())
            return "redirect:/comment/edit/"+commentId;

        dao.update(commentId, updatedComment);
        return "redirect:/painting/paintings";
    }

    @Transactional
    @DeleteMapping("/{commentId}")
    public String deleteCertainComment(@PathVariable("commentId") int commentId){
        dao.delete(commentId);
        return "redirect:/painting/paintings";
    }

}
