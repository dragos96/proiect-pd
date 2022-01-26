package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.CourseAttendance;
import com.example.demo.entities.CourseItem;

public interface CourseAttendanceRepository extends CrudRepository<CourseAttendance, Long> {
	List<CourseAttendance> findByUserId(Long userId);
	List<CourseAttendance> findByAssignerId(Long userId);
	List<CourseAttendance> findByAssignerIdAndCourseId(Long assignerId, Long courseId);
	CourseAttendance findByUserIdAndCourseId(Long userId, Long courseId);
	
	List<CourseAttendance> findByCourseId(Long courseId);
}
