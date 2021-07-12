package br.com.lasse.DockerMage.model;

import java.util.Arrays;
import java.util.Date;

public class Container {

	private long id;

	private String image;

	private String idDockerContainer;
	private String command;
	private String status;
	private String port;
	private String name;
	private String created;
	private String ports;
	private String label;
	private String state;
	private String memoryLimit;
	private String memoryReservation;
	private String swapLimit;
	private String cpu;
	private String restart;
	private Date data;

	@Override
	public String toString() {
		return "Container [id=" + id + ", image=" + image + ", idDockerContainer=" + idDockerContainer + ", command="
				+ command + ", status=" + status + ", port=" + port + ", name=" + name + ", created=" + created
				+ ", ports=" + ports + ", label=" + label + ", state=" + state + ", memoryLimit=" + memoryLimit
				+ ", memoryReservation=" + memoryReservation + ", swapLimit=" + swapLimit + ", cpu=" + cpu
				+ ", restart=" + restart + ", data=" + data + "]";
	}

	public Container() {
		super();
	}

	public Container(long id, String image, String idDockerContainer, String command, String status, String port,
			String name, String created, String ports, String label, String state, String memoryLimit,
			String memoryReservation, String swapLimit, String cpu, String restart, Date data) {
		super();
		this.id = id;
		this.image = image;
		this.idDockerContainer = idDockerContainer;
		this.command = command;
		this.status = status;
		this.port = port;
		this.name = name;
		this.created = created;
		this.ports = ports;
		this.label = label;
		this.state = state;
		this.memoryLimit = memoryLimit;
		this.memoryReservation = memoryReservation;
		this.swapLimit = swapLimit;
		this.cpu = cpu;
		this.restart = restart;
		this.data = data;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIdDockerContainer() {
		return idDockerContainer;
	}

	public void setIdDockerContainer(String idDockerContainer) {
		this.idDockerContainer = idDockerContainer;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMemoryLimit() {
		return memoryLimit;
	}

	public void setMemoryLimit(String memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	public String getMemoryReservation() {
		return memoryReservation;
	}

	public void setMemoryReservation(String memoryReservation) {
		this.memoryReservation = memoryReservation;
	}

	public String getSwapLimit() {
		return swapLimit;
	}

	public void setSwapLimit(String swapLimit) {
		this.swapLimit = swapLimit;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRestart() {
		return restart;
	}

	public void setRestart(String restart) {
		this.restart = restart;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

//	private long id;
//
//	private String idDocker;
//
//	private String names[];
//
//	private String image;
//
//	private String command;
//
//	private Date created;
//
//	private String ports[];
//
//	private String state;
//
//	private String status;
//
//	private String ip;
//
//	private String mac;
//
//	@Override
//	public String toString() {
//		return "Container [id=" + id + ", idDocker=" + idDocker + ", names=" + Arrays.toString(names) + ", image="
//				+ image + ", command=" + command + ", created=" + created + ", ports=" + Arrays.toString(ports)
//				+ ", state=" + state + ", status=" + status + ", ip=" + ip + ", mac=" + mac + "]";
//	}
//
//	public Container(long id, String idDocker, String[] names, String image, String command, Date created,
//			String[] ports, String state, String status, String ip, String mac) {
//		super();
//		this.id = id;
//		this.idDocker = idDocker;
//		this.names = names;
//		this.image = image;
//		this.command = command;
//		this.created = created;
//		this.ports = ports;
//		this.state = state;
//		this.status = status;
//		this.ip = ip;
//		this.mac = mac;
//	}
//
//	public Container() {
//		super();
//	}
//
//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}
//
//	public String getIdDocker() {
//		return idDocker;
//	}
//
//	public void setIdDocker(String idDocker) {
//		this.idDocker = idDocker;
//	}
//
//	public String[] getNames() {
//		return names;
//	}
//
//	public void setNames(String[] names) {
//		this.names = names;
//	}
//
//	public String getImage() {
//		return image;
//	}
//
//	public void setImage(String image) {
//		this.image = image;
//	}
//
//	public String getCommand() {
//		return command;
//	}
//
//	public void setCommand(String command) {
//		this.command = command;
//	}
//
//	public Date getCreated() {
//		return created;
//	}
//
//	public void setCreated(Date created) {
//		this.created = created;
//	}
//
//	public String[] getPorts() {
//		return ports;
//	}
//
//	public void setPorts(String[] ports) {
//		this.ports = ports;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public String getIp() {
//		return ip;
//	}
//
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//
//	public String getMac() {
//		return mac;
//	}
//
//	public void setMac(String mac) {
//		this.mac = mac;
//	}

}
