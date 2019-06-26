package com.neu.cloudassign1.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MyBasicAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private DataSource dataSource;

	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
        return new BCryptPasswordEncoder();
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select email as username,password as password ,true from user where email = ?")
		.authoritiesByUsernameQuery("SELECT email as username, 'ROLE_USER' FROM user WHERE email=?")
		.passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/hello").permitAll()
		.antMatchers("/user/register").permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()
        .csrf().disable();
        
	}
	
	
}
