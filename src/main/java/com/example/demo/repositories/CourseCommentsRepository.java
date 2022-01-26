package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.CourseComments;

public interface CourseCommentsRepository extends CrudRepository<CourseComments, Long> {
	
//	@Query("SELECT CourseComments from CourseComments as CourseComments where CourseComments.user.id = :userId")
    List<CourseComments> findByUserId(Long userId);
	
//	@Query("SELECT CourseComments from CourseComments as CourseComments where CourseComments.courseAttendnce.id = :userId")
	List<CourseComments> findByCourseAttendanceId(Long courseId);

}
