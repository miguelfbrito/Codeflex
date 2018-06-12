package pt.codeflex;

import java.io.File;
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
import net.schmizz.sshj.userauth.keyprovider.PKCS8KeyFile;
import net.schmizz.sshj.userauth.method.AuthPublickey;
import pt.codeflex.models.Host;

@SpringBootApplication
public class CodeflexApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeflexApplication.class, args);

	}

	@Bean
	public List<Host> fetchAndConnectHosts() {

		List<Host> hostList = new ArrayList<>();
		
		Host h1 = new Host("ec2-35-180-109-76.eu-west-3.compute.amazonaws.com", 22, "mbrito", new SSHClient(), "20:0c:b2:25:49:45:59:58:75:c5:2b:c5:84:02:a7:a9", false); 
		Host h2 = new Host("ec2-35-180-31-60.eu-west-3.compute.amazonaws.com", 22, "mbrito", new SSHClient(), "14:fc:81:d4:92:ab:21:05:ab:81:a2:6f:19:a2:78:c0", false); 


		connect(h1);
		connect(h2);

		hostList.add(h1);
		hostList.add(h2);

		return hostList;
	}

	public void connect(Host host) {
		SSHClient ssh = host.getSsh();
		try {
			
			ssh.addHostKeyVerifier(host.getFingerprint());
			
			// https://stackoverflow.com/questions/9283556/sshj-keypair-login-to-ec2-instance
			PKCS8KeyFile keyFile = new PKCS8KeyFile();
			keyFile.init(new File("c:\\Users\\mbrito\\.ssh\\aws.pem"));

			ssh.connect(host.getIp(), host.getPort());
			ssh.auth("ubuntu", new AuthPublickey(keyFile));

			Session session = ssh.startSession();
			Command cmd = session.exec("rm -rf Submissions/*");
			// System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
			System.out.println("Removing previous submissions from host " + host.getIp());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
