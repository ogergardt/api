package com.app.repository;

import com.app.entity.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, Long>, PagingAndSortingRepository<Job, Long> {
}