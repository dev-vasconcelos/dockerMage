package br.com.lasse.DockerMage.model;

import java.util.Date;

public class Image {

	private long id;

	private Date created;

	private String idDocker;

	private String repoTag;

	private float size;

	private float virtualSize;

	@Override
	public String toString() {
		return "Image [id=" + id + ", created=" + created + ", idDocker=" + idDocker + ", repoTag=" + repoTag
				+ ", size=" + size + ", virtualSize=" + virtualSize + "]";
	}

	public Image(long id, Date created, String idDocker, String repoTag, float size, float virtualSize) {
		super();
		this.id = id;
		this.created = created;
		this.idDocker = idDocker;
		this.repoTag = repoTag;
		this.size = size;
		this.virtualSize = virtualSize;
	}

	public Image() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getIdDocker() {
		return idDocker;
	}

	public void setIdDocker(String idDocker) {
		this.idDocker = idDocker;
	}

	public String getRepoTag() {
		return repoTag;
	}

	public void setRepoTag(String repoTag) {
		this.repoTag = repoTag;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getVirtualSize() {
		return virtualSize;
	}

	public void setVirtualSize(float virtualSize) {
		this.virtualSize = virtualSize;
	}

}
