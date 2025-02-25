package com.main.article.repository;

import com.main.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a.publishedDate, COUNT(a) FROM Article a WHERE a.publishedDate BETWEEN :startDate AND :endDate GROUP BY a.publishedDate")
    List<Object[]> countArticlesByPublishedDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
