package com.main.article;

import com.main.article.model.Article;
import com.main.article.repository.ArticleRepository;
import com.main.article.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        // Ініціалізація моків
        MockitoAnnotations.openMocks(this);

        article = new Article();
        article.setTitle("Test Article");
        article.setAuthor("Test Author");
        article.setContent("This is a test article.");
        article.setPublishedDate(LocalDate.now());
    }

    @Test
    void testCreateArticle() {
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article createdArticle = articleService.createArticle(article);

        assertNotNull(createdArticle);
        assertEquals(article.getTitle(), createdArticle.getTitle());
        assertEquals(article.getAuthor(), createdArticle.getAuthor());
    }

    @Test
    void testGetArticleById() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        Optional<Article> foundArticle = articleService.getArticleById(article.getId());

        assertTrue(foundArticle.isPresent());
        assertEquals(article.getTitle(), foundArticle.get().getTitle());
    }

    @Test
    void testGetArticleByIdNotFound() {
        when(articleRepository.findById(article.getId())).thenReturn(Optional.empty());

        Optional<Article> foundArticle = articleService.getArticleById(article.getId());

        assertFalse(foundArticle.isPresent());
    }

    @Test
    void testGetAllArticles() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articlePage = mock(Page.class);
        when(articleRepository.findAll(pageable)).thenReturn(articlePage);

        Page<Article> result = articleService.getAllArticles(0, 10);

        assertNotNull(result);
        verify(articleRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDeleteArticle() {
        doNothing().when(articleRepository).deleteById(anyLong());

        articleService.deleteArticle(article.getId());

        verify(articleRepository, times(1)).deleteById(article.getId());
    }
}
