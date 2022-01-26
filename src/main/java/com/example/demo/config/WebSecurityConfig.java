package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;

import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.security.jwt.AuthTokenFilter;
import com.example.demo.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	 @Autowired
	 UserDetailsServiceImpl userDetailsService;
	 
	 @Autowired
	  private AuthEntryPointJwt unauthorizedHandler;

	  @Bean
	  public AuthTokenFilter authenticationJwtTokenFilter() {
	    return new AuthTokenFilter();
	  }

	  @Override
	  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
	    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	  }

	  @Bean
	  @Override
	  public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	  }

	  @Bean
	  public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  }

	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http.cors().and().csrf().disable()
	      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
	      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
	      .antMatchers("/api/test/**").permitAll()
//	      .antMatchers("/api/courses/**").authenticated()
	      
	      .antMatchers(HttpMethod.GET, "/api/courseItems/**").authenticated()
	      .antMatchers(HttpMethod.POST,"/api/courseItems/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.DELETE,"/api/courseItems/**").hasAnyAuthority("ROLE_MODERATOR")

	      
	      .antMatchers(HttpMethod.GET, "/api/comments/**").authenticated()
	      .antMatchers(HttpMethod.POST,"/api/comments/**").hasAnyAuthority("ROLE_MODERATOR", "ROLE_USER")
	      .antMatchers(HttpMethod.DELETE,"/api/comments/**").hasAnyAuthority("ROLE_MODERATOR")
	      
//	      .antMatchers(HttpMethod.GET, "/api/courses/**").authenticated()
	      .antMatchers(HttpMethod.POST,"/api/courses/moderator/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.PUT,"/api/courses/moderator/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.DELETE,"/api/courses/moderator/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.GET,"/api/courses/moderator/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.GET,"/api/courses/admin/**").hasAnyAuthority("ROLE_ADMIN")
	      .antMatchers(HttpMethod.GET,"/api/courses/user/**").hasAnyAuthority("ROLE_USER")

	      .antMatchers("/api/users/**").hasAuthority("ROLE_ADMIN")
	      .antMatchers(HttpMethod.GET, "/api/courseAttendance/**").authenticated()
	      .antMatchers(HttpMethod.POST,"/api/courseAttendance/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.PUT,"/api/courseAttendance/**").hasAnyAuthority("ROLE_MODERATOR")
	      .antMatchers(HttpMethod.DELETE,"/api/courseAttendance/**").hasAuthority("ROLE_MODERATOR")

//	      .antMatchers("/api/courseItems/**").authenticated()
	      .anyRequest().authenticated();

	    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	  }
}
