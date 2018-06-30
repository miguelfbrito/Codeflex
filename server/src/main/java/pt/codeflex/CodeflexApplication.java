package pt.codeflex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public Host fetchAndConnectHosts() {
		Host h1 = new Host("192.168.1.65", 22, "mbrito", new SSHClient(), "33:02:cb:3b:13:b1:bd:fa:66:ff:29:96:ea:ff:dc:78", false); 
		//connect(h1);

		return h1;
	}
	

	
	public void connect(Host host) {
		SSHClient ssh = host.getSsh();
		try {
			ssh.addHostKeyVerifier(host.getFingerprint());
			// https://stackoverflow.com/questions/9283556/sshj-keypair-login-to-ec2-instance
			//PKCS8KeyFile keyFile = new PKCS8KeyFile();
			//keyFile.init(new File("c:\\Users\\mbrito\\.ssh\\aws.pem"));

			ssh.connect(host.getIp(), host.getPort());
			ssh.authPublickey("mbrito");

			Session session = ssh.startSession();
			Command cmd = session.exec("rm -rf Submissions/*");
			// System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
			System.out.println("Removing previous submissions from host " + host.getIp());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
