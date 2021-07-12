package br.com.lasse.DockerMage.model;

public class Dockerfile {

	private long id;

	private String path;

	private String name;

	private boolean builded;

	@Override
	public String toString() {
		return "Dockerfile [id=" + id + ", path=" + path + ", name=" + name + ", builded=" + builded + "]";
	}

	public Dockerfile(long id, String path, String name, boolean builded) {
		super();
		this.id = id;
		this.path = path;
		this.name = name;
		this.builded = builded;
	}

	public Dockerfile() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBuilded() {
		return builded;
	}

	public void setBuilded(boolean builded) {
		this.builded = builded;
	}

}
