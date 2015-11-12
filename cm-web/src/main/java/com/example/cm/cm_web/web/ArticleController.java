package com.example.cm.cm_web.web;

import com.example.cm.cm_docrepository.service.ArticleService;
import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_web.config.annotation.WebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;


/**
 * @author Jeffrey V Wong
 */
@WebController
@RequestMapping(value = "/articles")
public class ArticleController {

	private ArticleService articleService;

	@Autowired
	public ArticleController(ArticleService articleService) {
	    this.articleService = articleService;
	}

	/**
	 * List of Article instances
	 * @param model model
	 * @return article list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getList(Model model) {
		model.addAttribute("articleList", articleService.getList());
		return getFullViewName("articleList");
	}

	/**
	 * Create Article
	 * @return logical view name
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String createArticleForm(Model model) {

		Article article = new Article();
		model.addAttribute("article", article);
		return getFullViewName("articleForm");
	}

	/**
	 * Create Article
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createArticle(
			Principal principal,
			@Valid Article article,
			RedirectAttributes model,
			Errors errors) {

		if (errors.hasErrors()) {
			model.addFlashAttribute("errors", errors);
			return getFullViewName("/create");
		}

		//get logged in username
		article.setCreatedBy(principal.getName());
		Article saved = articleService.save(article);

		if(saved != null && articleService.exists(saved.getId())){
			model.addAttribute("id", saved.getId());
			model.addFlashAttribute("article", article);
			return "redirect:/articles/{id}";
		}

		return getFullViewName("articleForm");
	}

	/**
	 * @param id article UUID
	 * @return logical view name
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getArticle(
			@PathVariable Long id,
			Model model) {

		Article article = articleService.findOne(id);
		model.addAttribute("article", article);
		return getFullViewName("articlePage");
	}

	private String getFullViewName(String path){
		return "/articles/" + path;
	}
}
