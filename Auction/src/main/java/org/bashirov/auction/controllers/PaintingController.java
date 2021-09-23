package org.bashirov.auction.controllers;

import org.bashirov.auction.entity.Painting;
import org.bashirov.auction.dao.PaintingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.bashirov.auction.entity.Author;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/painting")
public class PaintingController {

    @Autowired
    private PaintingDao dao;

    @Transactional
    @GetMapping("/paintings")
    public String getAllPaintingsPage(Model model){
        model.addAttribute("paintingList", dao.getListOfPaintings());
        return "painting/paintingList";
    }

    @Transactional
    @GetMapping("/{paintingId}")
    public String getCertainPainting(Model model, @PathVariable int paintingId){
        final Painting painting = dao.getPainting(paintingId);
        model.addAttribute("commentList", painting.getComments());
        model.addAttribute("painting", painting);
        model.addAttribute("paintingId", paintingId);

        return "painting/painting";
    }

    @Transactional
    @GetMapping("/new/{authorId}")
    public String newPainting(Model model, @PathVariable int authorId,
                              @ModelAttribute Painting newPainting){
        model.addAttribute("authorId", authorId);
        model.addAttribute("newPainting", newPainting);
        return "painting/new";
    }


    @Transactional
    @PostMapping("/create/{authorId}")
    public String createPainting(@PathVariable("authorId") int authorId,
                                 @ModelAttribute("newPainting") @Valid Painting newPainting,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/painting/new/" + authorId;
        }

        Author author = dao.getAuthor(authorId);
        author.add(newPainting);
        dao.save(newPainting);

        return "redirect:/author/authors";
    }

    @Transactional
    @GetMapping("/edit/{paintingId}")
    public String editCertainPainting(Model model, @ModelAttribute("painting") Painting painting,
                       @PathVariable int paintingId){
        model.addAttribute("paintingId", paintingId);
        model.addAttribute("currBestPrice", painting.getBestPrice());
        model.addAttribute("painting", painting);
        return "painting/edit";
    }

    @Transactional
    @PatchMapping("/{paintingId}")
    public String updateCertainPainting(@ModelAttribute("painting") @Valid Painting updatedPainting,
                         BindingResult bindingResult, @PathVariable int paintingId) {
        if (bindingResult.hasErrors()) return "painting/edit";

        dao.update(paintingId, updatedPainting);
        return "redirect:/painting/"+paintingId;
    }

    @Transactional
    @DeleteMapping("/{paintingName}")
    public String deleteCertainPainting(@PathVariable int paintingName){
        dao.delete(paintingName);
        return "redirect:/painting/paintings";
    }

}
