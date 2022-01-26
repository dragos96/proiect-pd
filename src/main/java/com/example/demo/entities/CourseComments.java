package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course_comments")
public class CourseComments {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "course_attendance_id", nullable = false)
	@JsonIgnore
	private CourseAttendance courseAttendance;
	
	private String text;

	public CourseComments() {
		
	}

	public CourseComments(Long id, User user, CourseAttendance courseAttendance, String text) {
		super();
		this.id = id;
		this.user = user;
		this.courseAttendance = courseAttendance;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	
	
	public CourseAttendance getCourseAttendance() {
		return courseAttendance;
	}

	public void setCourseAttendance(CourseAttendance courseAttendance) {
		this.courseAttendance = courseAttendance;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
	

}
