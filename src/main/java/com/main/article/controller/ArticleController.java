package com.main.article.controller;

import com.main.article.model.Article;
import com.main.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Article Controller", description = "API для управління статтями")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "Створення нової статті", description = "Додає нову статтю в базу даних")
    @PostMapping("/create")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @Operation(summary = "Отримання статті за ID", description = "Повертає статтю за її унікальним ідентифікатором")
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(
            @Parameter(description = "ID статті", example = "1") @PathVariable Long id) {
        Optional<Article> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Видалення статті", description = "Видаляє статтю за її ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "ID статті", example = "1") @PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Отримання всіх статей з пагінацією", description = "Повертає список статей на сторінці")
    @GetMapping("/getAll&Pagination")
    public ResponseEntity<Page<Article>> getAllArticles(
            @Parameter(description = "Номер сторінки", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Кількість статей на сторінці", example = "10") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(articleService.getAllArticles(page, size));
    }

    @Operation(summary = "Отримання всіх статей у вигляді окремих сторінок", description = "Розбиває всі статті на сторінки")
    @GetMapping("/getAllLists")
    public ResponseEntity<List<List<Article>>> getAllArticlesPaginated(
            @Parameter(description = "Кількість статей у групі", example = "5") @RequestParam(defaultValue = "5") int size) {
        List<List<Article>> paginatedArticles = articleService.getAllArticlesPaginated(size);
        return ResponseEntity.ok(paginatedArticles);
    }

}
