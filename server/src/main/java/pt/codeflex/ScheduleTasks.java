package pt.codeflex;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.codeflex.models.Host;

@EnableScheduling
@Component
public class ScheduleTasks {

	@Autowired
	private List<Host> hostList;
	
	@Scheduled(fixedDelay = 5000)
	public void bar() {
		System.out.println("Hello");
		
	
		System.out.println(hostList.get(0).getCpuUsage());
		System.out.println(hostList.get(0).getMemoryUsage());
	}
}
