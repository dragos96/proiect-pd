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

import com.example.demo.dto.CommentDto;
import com.example.demo.entities.CourseAttendance;
import com.example.demo.entities.CourseComments;
import com.example.demo.entities.User;
import com.example.demo.entities.UserHelper;
import com.example.demo.repositories.CourseAttendanceRepository;
import com.example.demo.repositories.CourseCommentsRepository;

@RestController
@RequestMapping("/api/comments")
public class CourseCommentsController {
	
	
	@Autowired
	private UserHelper userHelper;
	
	@Autowired
	CourseCommentsRepository courseCommentsRepository;
	
	
	@Autowired
	private CourseAttendanceRepository courseAttendanceRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<CourseComments>> getAll()  {
		
		List<CourseComments> courseComments = (List<CourseComments>) courseCommentsRepository.findAll();
		return new ResponseEntity<>(courseComments, HttpStatus.OK);
	}
	
	@PostMapping("/save/{id}")
	public ResponseEntity<CourseComments> saveCourseComments(@RequestBody CourseComments courseComments, Principal principal, @PathVariable("id") Long id){
		
		
		Optional<CourseComments> opCourseComments = courseCommentsRepository.findById(id);
		if(!opCourseComments.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		CourseComments courseCommentsSaved = courseCommentsRepository.save(courseComments);
		return ResponseEntity.ok(courseCommentsSaved);
		
	}

	@PostMapping("/save")
	public ResponseEntity<CourseComments> saveCourseComments(@RequestBody CommentDto commentDto, Principal principal){
		
		
		Optional<User> userOp = this.userHelper.getUserFromPrincipal(principal);
		User user = userOp.get();
		
		CourseComments courseComment = new CourseComments();
		CourseAttendance courseAttendance = this.courseAttendanceRepository.findByUserIdAndCourseId(user.getId(), commentDto.getCourseId());
		
		
		courseComment.setUser(user);
		courseComment.setText(commentDto.getText());
		courseComment.setCourseAttendance(courseAttendance);
		
		
		CourseComments courseCommentsSaved = courseCommentsRepository.save(courseComment);
		return ResponseEntity.ok(courseCommentsSaved);
		
	}

	
	@DeleteMapping("/delete/{courseId}")
	public ResponseEntity<CourseComments> deleteCourseComments(@PathVariable("courseId") Long courseId, Principal principal){
		
		Optional<CourseComments> opCC = courseCommentsRepository.findById(courseId);
		if(!opCC.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		CourseComments cc = opCC.get();
		courseCommentsRepository.delete(cc);
		return ResponseEntity.ok(cc);
		
	}


}
