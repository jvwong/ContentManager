package com.example.cm.cm_web.web;

import com.example.cm.cm_jcrrepository.repository.ArticleRepository;
import com.example.cm.cm_model.domain.Article;
import com.example.cm.cm_web.config.annotation.WebController;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.Instant;
import java.util.UUID;


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
	 * List of Article instances
	 * @param model model
	 * @return article list
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String articleList(Model model) {
		model.addAttribute("articleList", articleRepository.findAll());
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
			@Valid Article article,
			RedirectAttributes model,
			Errors errors) {

		if (errors.hasErrors()) {
			model.addFlashAttribute("errors", errors);
			return getFullViewName("/create");
		}

		// Audting is JPA only

		//get logged in username
		article.setId(UUID.randomUUID().toString());
		article.setCreatedBy("ertertert");
		article.setCreatedDate(Instant.now());

		articleRepository.create(article);

		if(articleRepository.exists(article.getId())){
			model.addAttribute("id", article.getId());
			model.addFlashAttribute("article", article);
			return "redirect:/articles/";
		}

		return getFullViewName("articleForm");
	}

//	/**
//	 * @param id article ID
//	 * @return logical view name
//	 * @throws IOException if there's an I/O exception
//	 */
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	public String getArticle(
//			@PathVariable String id,
//			Model model) {
//
//		Article article = articleRepository.findOne(Long.parseLong(id));
//		model.addAttribute("article", article);
//		return getFullViewName("articlePage");
//	}
//
	private String getFullViewName(String path){
		return "/articles/" + path;
	}
}
