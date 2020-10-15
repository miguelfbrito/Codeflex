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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import static pt.codeflex.auth.security.SecurityConstants.ACCESSIBLE;

import javax.sql.DataSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	private UserDetailsService userDetailsService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private DataSource dataSource;

	@Autowired
	public WebSecurity(DataSource dataSource, UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.dataSource = dataSource;
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Comment to disable authentication
		/*
		http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, ACCESSIBLE).permitAll()
				.anyRequest().authenticated().and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()))
				// this disables session creation on Spring Security
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		 */
	}


	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

		/*
		// Temporarily disable authentication

		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, 1 from users where username=?")
				.authoritiesByUsernameQuery(
						"select u.username, r.name from users_roles ur, users u, role r where u.username = ? and ur.users_id = u.id and ur.role_id = r.id")
				.passwordEncoder(bCryptPasswordEncoder);

		 */
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

	// @Bean
	// public FilterRegistrationBean corsFilter() {
	// UrlBasedCorsConfigurationSource source = new
	// UrlBasedCorsConfigurationSource();
	// CorsConfiguration config = new CorsConfiguration();
	// config.setAllowCredentials(true);
	// config.addAllowedOrigin("*");
	//
	// config.addAllowedHeader("*");
	// config.addAllowedMethod("*");
	// source.registerCorsConfiguration("/**", config);
	//
	// FilterRegistrationBean bean = new FilterRegistrationBean(new
	// CorsFilter(source));
	// bean.setOrder(0);
	// return bean;
	//
	// }

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		System.out.println("setting cors configuration");
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.applyPermitDefaultValues();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}