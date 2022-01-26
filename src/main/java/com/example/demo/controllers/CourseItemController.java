package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Course;
import com.example.demo.entities.CourseItem;
import com.example.demo.entities.User;
import com.example.demo.repositories.CourseItemRepository;
import com.example.demo.repositories.CourseRepository;

@RestController
@RequestMapping("/api/courseItems")
public class CourseItemController {
	
	@Autowired
	CourseItemRepository courseItemRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/all")
	public List<CourseItem> findAll() {
		return (List<CourseItem>) courseItemRepository.findAll();
	}
	
	@GetMapping("/ownedBy/{courseId}")
	public ResponseEntity<List<CourseItem>> getAllCoursesByOwnerId(@PathVariable Long courseId) throws Exception {
		
		 if (!courseRepository.existsById(courseId)) {
		      throw new Exception("Not found Course with id = " + courseId);
		    }

		List<CourseItem> courseItems = courseItemRepository.findByCourseId(courseId);
		return new ResponseEntity<>(courseItems, HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<CourseItem> saveCourseItem(@RequestBody CourseItem courseItem, Principal principal){
		
		
		CourseItem courseItemSaved = courseItemRepository.save(courseItem);
		return ResponseEntity.ok(courseItemSaved);
		
	}
	@PostMapping("/save-to-course/{idCourse}")
	public ResponseEntity<CourseItem> saveCourseItemToCourse(@RequestBody CourseItem courseItem, Principal principal, @PathVariable("idCourse") long idCourse){
		
		Optional<Course> courseOp = this.courseRepository.findById(idCourse);
		if(!courseOp.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		Course theCourse = courseOp.get();
		courseItem.setCourse(theCourse);
		
		CourseItem courseItemSaved = courseItemRepository.save(courseItem);
		return ResponseEntity.ok(courseItemSaved);
		
	}
	
	@DeleteMapping("/delete/{courseId}")
	public ResponseEntity<CourseItem> deleteCourseItem(@PathVariable("courseId") Long courseId, Principal principal){
		
		Optional<CourseItem> opCI = courseItemRepository.findById(courseId);
		if(!opCI.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		CourseItem ci = opCI.get();
		courseItemRepository.delete(ci);
		return ResponseEntity.ok(ci);
		
	}


}
