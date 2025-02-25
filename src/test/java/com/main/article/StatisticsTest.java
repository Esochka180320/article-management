package com.main.article;

import com.main.article.repository.ArticleRepository;
import com.main.article.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class StatisticsTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        // Ініціалізація моків
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetArticleCountByDayForLast7Days() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        when(articleRepository.countArticlesByPublishedDate(startDate, today))
                .thenReturn(List.of(new Object[]{today.minusDays(1), 5L}, new Object[]{today, 3L}));

        Map<LocalDate, Long> articleCount = articleService.getArticleCountByDayForLast7Days();

        assertNotNull(articleCount);
        assertEquals(5L, articleCount.get(today.minusDays(1)));
        assertEquals(3L, articleCount.get(today));
        assertEquals(0L, articleCount.get(today.minusDays(2)));
    }

}
