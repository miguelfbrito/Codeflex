package pt.codeflex.dto;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

public class Host {

	private String ip;
	private int port;
	private String user;
	private String fingerprint;
	private SSHClient ssh;
	private boolean beingUsed;
	private Date lastDateAttributed;
	
	
	public Host(String ip, int port, String user, SSHClient ssh, String fingerprint, boolean beingUsed) {
		super();
		this.ip = ip;
		this.port = port;
		this.beingUsed = beingUsed;
		this.user = user;
		this.ssh = ssh;
		this.lastDateAttributed = Calendar.getInstance().getTime();
		this.fingerprint = fingerprint;
	}

	public Host(String ip, String user, SSHClient ssh, boolean beingUsed) {
		super();
		this.ip = ip;
		this.user = user;
		this.ssh = ssh;
		this.beingUsed = beingUsed;
		this.lastDateAttributed = Calendar.getInstance().getTime();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public SSHClient getSsh() {
		return ssh;
	}

	public void setSsh(SSHClient ssh) {
		this.ssh = ssh;
	}

	public boolean isBeingUsed() {
		return beingUsed;
	}

	public void setBeingUsed(boolean beingUsed) {
		this.beingUsed = beingUsed;
	}

	public Date getLastDateAttributed() {
		return lastDateAttributed;
	}

	public void setLastDateAttributed(Date lastDateAttributed) {
		this.lastDateAttributed = lastDateAttributed;
	}

	@Override
	public String toString() {
		return "Host [ip=" + ip + ", user=" + user + ", ssh=" + ssh + ", beingUsed=" + beingUsed
				+ ", lastDateAttributed=" + lastDateAttributed + "]";
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	
	public double getCpuUsage() {
		Session session;
		String output = "";
		try {
			session = this.ssh.startSession();
			Command cmd;
			String command = "mpstat -P ALL 1 1 | awk '{print $12}'";
			cmd = session.exec(command);
			output = IOUtils.readFully(cmd.getInputStream()).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// gets the first value of usage, representing total CPU usage
		output = output.split("%idle")[1].split("\n")[1].replace(",", ".");

		return (double) 100 - Double.valueOf(output);
	}

	public int getMemoryUsage() {
		Session session;
		String output = "";
		try {
			session = this.ssh.startSession();
			Command cmd;
			String command = "free -m | tail -2 | awk '{print $4}'";
			cmd = session.exec(command);
			session.join();
			output = IOUtils.readFully(cmd.getInputStream()).toString();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// gets the first value out of 2. The first value represents the free memory, the second represents the free swap.
		output = output.split("\n")[0];
		
		return Integer.valueOf(output);
	}

	
}
