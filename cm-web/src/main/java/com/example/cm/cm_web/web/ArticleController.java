package com.example.cm.cm_web.web;

import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_repository.repository.ArticleRepository;
import com.example.cm.cm_web.config.annotation.WebController;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;


/**
 * @author Jeffrey V Wong
 */
@WebController
@RequestMapping(value = "/articles")
public class ArticleController {
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
	
	private ArticleRepository articleRepository;

	@Autowired
	public ArticleController(ArticleRepository articleRepository) {
	    this.articleRepository = articleRepository;
	}
	
	/**
	 * Create Article
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createArticle(
			@Valid Article article,
			RedirectAttributes model,
			Errors errors) {

		if (errors.hasErrors()) {
			logger.info("Article errors encountered");
			model.addFlashAttribute("errors", errors);
			return getFullViewName("/create");
		}

		Article saved = articleRepository.save(article);

		if(saved != null){
			logger.info("Saving: {}", saved.toString());
			model.addAttribute("id", saved.getId());
			model.addFlashAttribute("article", saved);
			return "redirect:/articles/{id}";
		}

		return getFullViewName("articleForm");
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
	 * @param model model
	 * @return article list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String articleList(Model model) {
		model.addAttribute("articleList", articleRepository.findAll());
		return getFullViewName("articleList");		
	}
	
	/**
	 * @param id article ID
	 * @return logical view name
	 * @throws IOException if there's an I/O exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getArticle(
			@PathVariable String id,
			Model model) {

		Article article = articleRepository.findOne(Long.parseLong(id));
		model.addAttribute("article", article);
		return getFullViewName("articlePage");
	}
	
	private String getFullViewName(String path){
		return "/articles/" + path;
	}
}
