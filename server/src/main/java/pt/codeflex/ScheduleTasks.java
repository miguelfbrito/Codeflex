package pt.codeflex;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.codeflex.databasemodels.Submissions;
import pt.codeflex.models.Host;
import pt.codeflex.models.SubmissionWithTestCase;
import pt.codeflex.models.TasksBeingEvaluated;

@EnableScheduling
@Component
public class ScheduleTasks {

	@Autowired
	private List<Host> hostList;

	@Autowired
	private TasksBeingEvaluated tasksBeingEvaluated;

//	@Scheduled(fixedDelay = 5000)
//	public void bar() {
//		System.out.println(hostList.get(0).getCpuUsage());
//		System.out.println(hostList.get(0).getMemoryUsage());
//	}
}
