package com.main.article.service;

import com.main.article.model.Article;
import com.main.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public Page<Article> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }

    public List<List<Article>> getAllArticlesPaginated(int size) {
        List<Article> allArticles = articleRepository.findAll();


        int totalArticles = allArticles.size();
        int pageCount = (totalArticles + size - 1) / size;

        List<List<Article>> paginatedArticles = new ArrayList<>();

        for (int i = 0; i < pageCount; i++) {
            int start = i * size;
            int end = Math.min(start + size, totalArticles);
            paginatedArticles.add(allArticles.subList(start, end));
        }

        return paginatedArticles;
    }

    public Map<LocalDate, Long> getArticleCountByDayForLast7Days() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minus(7, ChronoUnit.DAYS);

        List<Object[]> result = articleRepository.countArticlesByPublishedDate(startDate, today);

        Map<LocalDate, Long> countMap = new HashMap<>();
        for (Object[] row : result) {
            LocalDate date = (LocalDate) row[0];
            Long count = (Long) row[1];
            countMap.put(date, count);
        }

        for (LocalDate date = startDate; date.isBefore(today.plus(1, ChronoUnit.DAYS)); date = date.plus(1, ChronoUnit.DAYS)) {
            countMap.putIfAbsent(date, 0L);
        }

        return countMap;
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
