package com.rmks.website.service;

import com.rmks.website.model.News;
import java.util.List;

public interface NewsService {
    long getNewsCount();
    List<News> getAllNews();
    List<News> getLatestNews(int count);
    News getNewsById(Long id);
    News saveNews(News news);
    void deleteNews(Long id);
}