package pt.codeflex.models;

import java.util.Calendar;
import java.util.Date;

import net.schmizz.sshj.SSHClient;

public class Host {

	private String ip;
	private int port;
	private String user;
	private SSHClient ssh;
	private boolean beingUsed;
	private Date lastDateAttributed;
	
	public Host(String ip, int port, String user, SSHClient ssh, boolean beingUsed) {
		super();
		this.ip = ip;
		this.port = port;
		this.beingUsed = beingUsed;
		this.user = user;
		this.ssh = ssh;
		this.lastDateAttributed = Calendar.getInstance().getTime();
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

	
}
