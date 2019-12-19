package com.codeofthewebcom.salvo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score, Float> {
    List<Score> findByFinishDate(LocalDateTime finishDate);
}
