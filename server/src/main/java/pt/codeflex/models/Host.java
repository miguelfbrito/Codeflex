package pt.codeflex.models;

import net.schmizz.sshj.SSHClient;

public class Host {

	private String ip;

	private String user;
	private SSHClient ssh;

	public Host(String ip, String user, SSHClient ssh) {
		super();
		this.ip = ip;
		this.user = user;
		this.ssh = ssh;
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

	@Override
	public String toString() {
		return "Host [ip=" + ip + ", user=" + user + ", ssh=" + ssh + "]";
	}
}
