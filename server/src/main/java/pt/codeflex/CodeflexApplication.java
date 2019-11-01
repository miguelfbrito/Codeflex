package pt.codeflex;

import java.io.File;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.schmizz.sshj.SSHClient;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Host fetchAndConnectHosts() {
        Host h1 = new Host("172.25.0.3", 22, "root", new SSHClient(),
                "", false);

      //  System.out.println("Trying to connect");
        //System.out.println(h1);

        connect(h1);



        return h1;
    }

    public void connect(Host host) {
        SSHClient ssh = host.getSsh();
        try {
            System.out.println("Trying to connect");

            // TODO : deal with this
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(host.getIp(), host.getPort());
            ssh.authPassword("root", "root");

            // Amazon EC2
            // https://stackoverflow.com/questions/9283556/sshj-keypair-login-to-ec2-instance
            // PKCS8KeyFile keyFile = new PKCS8KeyFile();
            // keyFile.init(new File("~/aws.pem"));
            // ssh.auth("ubuntu", new AuthPublickey(keyFile));

            // ssh.authPublickey("root");
            ssh.startSession().exec("rm -rf " + PATH_SERVER + File.separator + "*");
            ssh.startSession().exec("touch ~/created_from_java.txt");
            ssh.startSession().exec("mkdir Submissions");

            System.out.println("Chegou aqui!");

            LOGGER.log(Level.FINE, "Removing previous submissions from host.");
        } catch (IOException e) {
            System.out.println("EXCECAO");
            e.printStackTrace();
        }

    }

}
