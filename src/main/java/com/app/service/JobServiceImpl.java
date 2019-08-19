package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.entity.Job;
import com.app.exception.JobNotFoundException;
import com.app.repository.JobRepository;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
	private JobRepository jobRepository;

	@Override
	public Page<Job> findAll(Pageable pageable) {
		return jobRepository.findAll(pageable);
	}

	@Override
	public Job findById(Long id) {
        if(jobRepository.exists(id)) {
            return jobRepository.findOne(id);
        } else {
            throw new JobNotFoundException();
        }
	}

}
