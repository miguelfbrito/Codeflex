package pt.codeflex.config;

// http://javasampleapproach.com/spring-framework/spring-security/use-spring-security-jdbc-authentication-mysql-spring-boot

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/index", "/demo/**").permitAll().antMatchers("/block")
				.hasRole("USER").and().formLogin().loginPage("/account/login");
		
		http.exceptionHandling().accessDeniedPage("/403");
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery("select username,password from users where username=?");
		
//		auth.jdbcAuthentication().dataSource((javax.sql.DataSource) dataSource)
//				.usersByUsernameQuery("select username,password from users where username=?");
		// .authoritiesByUsernameQuery("select username, role from user_roles where
		// username=?");
	}
}