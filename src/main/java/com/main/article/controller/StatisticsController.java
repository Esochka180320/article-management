package com.main.article.controller;

import com.main.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@Tag(name = "Article Controller", description = "API для управління статтями")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private ArticleService articleService;

    @Operation(summary = "Отримання кількості опублікованих статей за останні 7 днів",
            description = "Повертає кількість опублікованих статей, згрупованих за датою публікації протягом останніх 7 днів.")
    @GetMapping("/publishedCount7")
    public ResponseEntity<Map<LocalDate, Long>> getPublishedArticlesCount() {
        Map<LocalDate, Long> result = articleService.getArticleCountByDayForLast7Days();
        return ResponseEntity.ok(result);
    }
}
