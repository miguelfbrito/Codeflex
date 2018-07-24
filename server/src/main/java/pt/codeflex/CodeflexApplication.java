package pt.codeflex;

import java.io.File;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.schmizz.sshj.SSHClient;
import pt.codeflex.models.Host;

import static pt.codeflex.evaluatesubmissions.EvaluateConstants.PATH_SERVER;

@SpringBootApplication
public class CodeflexApplication {

	public static final Logger LOGGER = Logger.getLogger(CodeflexApplication.class.getName());

	public static void main(String[] args) {

		Handler handlerObj = new ConsoleHandler();
		handlerObj.setLevel(Level.ALL);
		LOGGER.addHandler(handlerObj);
		LOGGER.setLevel(Level.ALL);
		LOGGER.setUseParentHandlers(false);

		LOGGER.log(Level.FINE, "Starting application!");
		SpringApplication.run(CodeflexApplication.class, args);
		
		System.out.println("FILE SEPARATOR " + File.separator);

	}
	
	
	

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Host fetchAndConnectHosts() {
		Host h1 = new Host("192.168.1.65", 22, "mbrito", new SSHClient(),
				"33:02:cb:3b:13:b1:bd:fa:66:ff:29:96:ea:ff:dc:78", false);
		connect(h1);

		return h1;
	}

	public void connect(Host host) {
		SSHClient ssh = host.getSsh();
		try {
			ssh.addHostKeyVerifier(host.getFingerprint());
			ssh.connect(host.getIp(), host.getPort());

			// Amazon EC2
			// https://stackoverflow.com/questions/9283556/sshj-keypair-login-to-ec2-instance
			// PKCS8KeyFile keyFile = new PKCS8KeyFile();
			// keyFile.init(new File("~/aws.pem"));
			// ssh.auth("ubuntu", new AuthPublickey(keyFile));

			ssh.authPublickey("mbrito");
			ssh.startSession().exec("rm -rf " + PATH_SERVER + File.separator + "*");

			LOGGER.log(Level.FINE, "Removing previous submissions from host.");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
