package pt.codeflex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import pt.codeflex.models.Host;

@SpringBootApplication
public class CodeflexApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeflexApplication.class, args);

	}

	@Bean
	public List<Host> fetchAndConnectHosts() {

		List<Host> hostList = new ArrayList<>();
		// 188.82.211.214
		Host h1 = new Host("188.82.211.214", 4022, "mbrito", new SSHClient(), false);
		Host h2 = new Host("188.82.211.214", 4023, "mbrito", new SSHClient(), false);

		connect(h1);
		connect(h2);

		hostList.add(h1);
		hostList.add(h2);

		return hostList;
	}

	public void connect(Host host) {
		SSHClient ssh = host.getSsh();
		try {
			// ssh.loadKnownHosts();
			ssh.addHostKeyVerifier("33:02:cb:3b:13:b1:bd:fa:66:ff:29:96:ea:ff:dc:78");
			ssh.connect(host.getIp(), host.getPort());
			ssh.authPublickey(host.getUser());

			Session session = ssh.startSession();
			Command cmd = session.exec("rm -rf /home/" + host.getUser() + "/Desktop/Submissions/*");
			// System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
			System.out.println("Removing previous submissions from host " + host.getIp());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
