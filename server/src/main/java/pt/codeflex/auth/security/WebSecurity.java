package pt.codeflex.auth.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import static pt.codeflex.auth.security.SecurityConstants.SIGN_UP_URL;

import java.util.Arrays;
import java.util.Collections;

import static pt.codeflex.auth.security.SecurityConstants.ACESSABLE;

import javax.sql.DataSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	private UserDetailsService userDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	public WebSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, ACESSABLE).permitAll()
				.anyRequest().authenticated().and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				// this disables session creation on Spring Security
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, 1 from users where username=?")
				.authoritiesByUsernameQuery(
						"select u.username, r.name from users_roles ur, users u, role r where u.username = ? and ur.users_id = u.id and ur.role_id = r.id")
				.passwordEncoder(bCryptPasswordEncoder);
	}

	// @Bean
	// CorsConfigurationSource corsConfigurationSource() {
	//
	// CorsConfiguration configuration = new CorsConfiguration();
	// configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000",
	// "http://localhost:3000"));
	// configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
	// "OPTIONS"));
	// UrlBasedCorsConfigurationSource source = new
	// UrlBasedCorsConfigurationSource();
	// source.registerCorsConfiguration("/**", configuration);
	// return source;
	// }

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		System.out.println("setting cors configuration");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

}