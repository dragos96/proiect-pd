package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CourseDto;
import com.example.demo.entities.Course;
import com.example.demo.entities.CourseAttendance;
import com.example.demo.entities.User;
import com.example.demo.entities.UserHelper;
import com.example.demo.repositories.CourseAttendanceRepository;
import com.example.demo.repositories.CourseRepository;
import com.example.demo.repositories.UserRepository;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private UserHelper userHelper;

	@Autowired
	private CourseAttendanceRepository courseAttendanceRepository;
	
	private boolean courseOwner(Principal principal, long id) {
		Optional<User> userOp = this.userHelper.getUserFromPrincipal(principal);
		if (!userOp.isPresent()) {
			return false;
		}
		User user = userOp.get();
		Course course = this.courseRepository.findById(id).get();
		List<Course> userCourses = this.courseRepository.findByOwnerId(user.getId());
		if (!userCourses.contains(course)) {
			System.out.println("BAD REQUEST COURSE DOESN'T BELONG TO OWNER - Principal");
			return false;
		}
		return true;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Course> findById(@PathVariable Long id, Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getPrincipal());

		if (!courseOwner(principal, id)) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(this.courseRepository.findById(id).get());
	}

	@GetMapping("/admin/all")
	public List<Course> findAllAdmin() {
		return (List<Course>) courseRepository.findAll();
	}
	@GetMapping("/moderator/all")
	public List<Course> findAllModerator(Principal principal) {
		Optional<User> userOp = this.userHelper.getUserFromPrincipal(principal);
		User user = userOp.get();
		List<Course> userCourses = this.courseRepository.findByOwnerId(user.getId());
		return userCourses;
	}
	@GetMapping("/user/all")
	public List<CourseDto> findAllStudent(Principal principal) {
		Optional<User> userOp = this.userHelper.getUserFromPrincipal(principal);
		User user = userOp.get();
		List<CourseAttendance> courseAttendances = courseAttendanceRepository.findByAssignerId(user.getId());
		List<CourseDto> courses = courseAttendances.stream().map(x -> x.getCourse()).map(x -> new CourseDto(x)).collect(Collectors.toList());
		
		return courses;
	}
	
	

	@GetMapping("/moderator/createdBy/{createdBy}")
	public ResponseEntity<List<Course>> getAllCoursesByOwnerId(@PathVariable Long createdBy, Principal principal)
			throws Exception {

		if (!userRepository.existsById(createdBy)) {
			throw new Exception("Not found User with id = " + createdBy);
		}

		List<Course> courses = courseRepository.findByOwnerId(createdBy);
		for (Course course : courses) {
			if (!courseOwner(principal, course.getId())) {
				return ResponseEntity.badRequest().build();
			}

		}
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	@PostMapping("/moderator/save")
	public ResponseEntity<Course> saveCourse(@RequestBody Course course, Principal principal) {

		Course courseSaved = courseRepository.save(course);
		return ResponseEntity.ok(courseSaved);

	}

	@PutMapping("/moderator/update")
	public ResponseEntity<Course> updateCourse(@RequestBody Course course, Principal principal) {

		if (!courseOwner(principal, course.getId())) {
			return ResponseEntity.badRequest().build();
		}

		Optional<Course> courseOp = courseRepository.findById(course.getId());
		if (!courseOp.isPresent()) {
			return ResponseEntity.badRequest().build();
		}

		Course courseSaved = courseRepository.save(course);
		return ResponseEntity.ok(courseSaved);

	}

	@DeleteMapping("/moderator/delete/{id}")
	public ResponseEntity<Course> deleteTema(@PathVariable("id") Long id, Principal principal) {

		if (!courseOwner(principal, id)) {
			return ResponseEntity.badRequest().build();
		}

		Optional<Course> opCourse = courseRepository.findById(id);
		if (!opCourse.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		Course course = opCourse.get();
		courseRepository.delete(course);
		return ResponseEntity.ok(course);

	}

}
