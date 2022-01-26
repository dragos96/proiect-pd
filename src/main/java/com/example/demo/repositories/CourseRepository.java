package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
	
	List<Course> findByOwnerId(Long userId);
	

}
