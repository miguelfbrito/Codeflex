package pt.codeflex.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ThreadConfig {
	@Bean
	public TaskExecutor threadPoolTaskExecutor() {
		
		//https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html#setCorePoolSize-int
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(10);
		executor.setThreadNamePrefix("default_task_executor_thread");
		executor.initialize();
		return executor;
	}
	
}