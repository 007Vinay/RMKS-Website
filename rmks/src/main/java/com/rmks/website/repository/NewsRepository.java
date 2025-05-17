package com.rmks.website.repository;

import com.rmks.website.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByPublishDateDesc();
    Page<News> findByActiveTrue(Pageable pageable);
}