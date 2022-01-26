package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.CourseItem;

@Repository
public interface CourseItemRepository extends CrudRepository<CourseItem, Long> {
	
	List<CourseItem> findByCourseId(Long courseId);
	

}
