package pt.codeflex.models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.codeflex.utils.Hash;

@Entity
public class Tournament {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	private Users owner;

	@Column(length = 50, unique = true)
	private String name;

	@Column(length = 10000)
	private String description;

	private Date startingDate;

	private Date endingDate;

	private String link;

	private String code;

	@JsonProperty
	private boolean showWebsite;

	@JsonProperty
	private boolean open;

	public Tournament() {
	}

	public Tournament(String name, String description, Date startingDate, Date endingDate, String code, Users owner,
			boolean showWebsite) {
		this.name = name;
		this.startingDate = startingDate;
		this.description = description;
		this.endingDate = endingDate;
		if (code != null) {
			String hash = Hash.getHash(name + startingDate.toString() + endingDate.toString(), "MD5");
			this.link = hash;
		}
		this.showWebsite = showWebsite;
		this.owner = owner;
		this.code = code;
		long currentDate = Calendar.getInstance().getTimeInMillis();
		this.open = (startingDate.getTime() <= currentDate && endingDate.getTime() >= currentDate) ? true : false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getShowWebsite() {
		return showWebsite;
	}

	public void setShowWebsite(boolean showWebsite) {
		this.showWebsite = showWebsite;
	}

	public boolean getOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public String toString() {
		return "Tournament [id=" + id + ", name=" + name + ", description=" + description + ", startingDate="
				+ startingDate + ", endingDate=" + endingDate + ", link=" + link + ", code=" + code + ", showWebsite="
				+ showWebsite + ", open=" + open + ", owner=" + owner + "]";
	}

	public Users getOwner() {
		return owner;
	}

	public void setOwner(Users owner) {
		this.owner = owner;
	}

}
