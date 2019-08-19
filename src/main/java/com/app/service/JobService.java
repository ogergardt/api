package com.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.entity.Job;


	@Service
	public interface JobService {
	  Page<Job> findAll(Pageable pageable);
	  Job findById(Long id);
	}
