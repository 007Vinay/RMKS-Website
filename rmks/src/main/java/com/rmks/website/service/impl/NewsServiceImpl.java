package com.rmks.website.service.impl;

import com.rmks.website.model.News;
import com.rmks.website.repository.NewsRepository;
import com.rmks.website.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public long getNewsCount(){
        return newsRepository.count();
    }

    @Override
    public List<News> getAllNews() {
        return newsRepository.findAllByOrderByPublishDateDesc();
    }

    @Override
    public List<News> getLatestNews(int count) {
        return newsRepository.findByActiveTrue(
                PageRequest.of(0, count, Sort.by(Sort.Direction.DESC, "publishDate"))
        ).getContent();
    }

    @Override
    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
    }

    @Override
    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}