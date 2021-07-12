package br.com.lasse.DockerMage.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.lasse.DockerMage.model.Container;

//https://stackoverflow.com/questions/20508788/do-i-need-content-type-application-octet-stream-for-file-download

@Controller
@RequestMapping("/containers")
public class ContainerController {

	public static ShellStarter shell = new ShellStarter();
	private String com;

//  Although returns an exceptions, it works at all
//	Os padrões de command são : "/bin/bash" e "sh"
//	Rodar container e atualizar container
//	https://docs.docker.com/engine/reference/commandline/update/#usage
	@PostMapping(value = "/")
	private ResponseEntity<?> create(@RequestBody Container container) {
		String memoryString = "";
		String swapString = "";
		String cpuString = "";
		String restartString = "";
		String nameString = "";
		String commandString = "";

		if (container.getImage().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome da imagem está vazio");
		}

		if (!container.getMemoryLimit().isEmpty()) {
			memoryString += " -m " + container.getMemoryLimit();
		}

		if (!container.getMemoryReservation().isEmpty()) {
			memoryString += " --memory-reservation " + container.getMemoryReservation();
		}

		if (!container.getSwapLimit().isEmpty()) {
			swapString += " --memory-swap " + container.getSwapLimit();
		}

		if (!container.getCpu().isEmpty()) {
			cpuString += " -c " + container.getCpu();
		}

		if (!container.getRestart().isEmpty()) {
			restartString += " --restart=" + container.getRestart(); // Usually == always
		}

		if (!container.getName().isEmpty()) {
			nameString += " --name " + container.getName();
		}

		if (container.getCommand().isEmpty()) {
			commandString += " sh ";
		} else {
			commandString += " " + container.getCommand() + " ";
		}

		if (!container.getIdDockerContainer().isEmpty())
			com = "docker update " + memoryString + swapString + cpuString + restartString + " "
					+ container.getIdDockerContainer();
		else
			com = "docker run -ti -d " + nameString + memoryString + swapString + cpuString + restartString + " "
					+ container.getImage() + " " + commandString;

		try {

			System.out.println("comando::: " + com);

			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);

		} catch (InternalError e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

//	Executar comando no container
	@PostMapping(value = "/com")
	private ResponseEntity<?> executeCommand(@RequestParam("com") String command,
			@RequestParam("container") String container) {
		try {
			if (command.isEmpty() || container.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetros faltando");

			com = "docker exec " + container + " " + command;

			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Copiar arquivos de um container p/ outro
	@PostMapping(value = "/cp")
	private ResponseEntity<?> copy(@RequestParam("containerHost") String containerHost,
			@RequestParam("containerClient") String containerClient, @RequestParam("hostPath") String filePath,
			@RequestParam("clientPath") String clientPath) {

		if (containerHost.isEmpty() || containerClient.isEmpty() || filePath.isEmpty() || clientPath.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("parâmetros incompletos");

		int randomNumber = (int) (Math.random() * 999999999 + 1);

		String localFile = "/tmp/tmp." + randomNumber;

		try {
			com = "docker cp " + containerHost + ":" + filePath + " " + localFile;
			System.out.println(com);
			shell.executeCommand(com);

			com = "docker cp " + localFile + " " + containerClient + ":" + clientPath;
			System.out.println(com);
			shell.executeCommand(com);

			com = "docker exec " + containerClient + " ls " + clientPath;
			System.out.println(com);
			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception | InternalError ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	copiar arquivo do container para host
//	@PostMapping(value = "/cp/h")
	@GetMapping(value = "/cp/h")
	private HttpEntity<?> copyHost(@RequestParam("containerHost") String containerHost,
			@RequestParam("hostPath") String filePath) {

		if (containerHost.isEmpty() || filePath.isEmpty()) {
			String msg = "parâmetro incompletos, o que foi enviado: containerHost:" + containerHost + " hostPath: "
					+ filePath;
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
		}

//		Random name
		String[] fileName = filePath.split("/");
		int randomNumber = (int) (Math.random() * 99999999 + 1);

//		Temporary Folder
		String folder = "/tmp/tmp-" + randomNumber;
		String name = fileName[fileName.length - 1];
		String localFilePath = folder + "/" + name;

		try {

			com = "mkdir " + folder;
			shell.executeCommand(com);

			com = "docker cp " + containerHost + ":" + filePath + " " + localFilePath;
			shell.executeCommand(com);

			File file = getFile(localFilePath);
			byte[] document = FileCopyUtils.copyToByteArray(file);

//			autoMIME
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String mime = mimeTypesMap.getContentType(file);
			System.out.println(mime);

			String[] split = mime.split("/");

//			Http preparation
			HttpHeaders header = new HttpHeaders();
//			header.setContentType(new MediaType("application", "png"));
			header.setContentType(new MediaType(split[0], split[1]));
			header.set("Content-Disposition", "inline; filename=" + file.getName());
			header.setContentLength(document.length);

//			Return file
			return new HttpEntity<byte[]>(document, header);
		} catch (Exception | InternalError ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	 Copiar arquivo do host p/ container
	@PostMapping(value = "/cp/c")
	private ResponseEntity<?> copyContainer(@RequestParam("containerClient") String containerClient,
			@RequestParam("pathContainerClient") String pathContainerClient, @RequestParam("file") MultipartFile file)
			throws IOException {

		if (containerClient.isEmpty() || file.isEmpty()) {
			String msg = "Parâmetros incompletos! O que foi recebido ::: containerClient=" + containerClient
					+ " || file=" + file.getName();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
		}
		
//		Random name
		int randomNumber = (int) (Math.random() * 99999999 + 1);

//		Temporary Folder
		String folder = "/tmp/tmp-" + randomNumber + "/";
		String localPath = folder + file.getOriginalFilename();

		try {
//			Create folder
			com = "mkdir " + folder;
			shell.executeCommand(com);

//			Receive and copy to server
			FileOutputStream ots = new FileOutputStream(localPath);
			ots.write(file.getBytes());
			ots.close();

//			copy from server to container
			com = "docker cp " + localPath + " " + containerClient + ":/" + pathContainerClient;
			shell.executeCommand(com);

			com = "docker exec " + containerClient + " ls " + pathContainerClient;
			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	https://tecadmin.net/export-and-import-docker-containers/
//	--change altera algumas configurações do dockerfile para o container que vamos salvar
//	CMD|ENTRYPOINT|ENV|EXPOSE|LABEL|ONBUILD|USER|VOLUME|WORKDIR
//	Realizar upload de um container rodando p/ registry
	@PostMapping(value = "/up")
	private ResponseEntity<?> containerUpdate(@RequestParam("container") String container,
			@RequestParam("user") String user, @RequestParam("passwd") String passwd,
			@RequestParam("repository") String repo, @RequestParam("version") String version,
			@RequestParam("serverRegistry") String server/* , @RequestParam("change") String change */) {

		if (container.isEmpty() || user.isEmpty() || passwd.isEmpty() || repo.isEmpty() || version.isEmpty()
				|| server.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetros inválidos e omitidos por segurança!");

		String comReturn = null;
		String com = null;
		ShellStarter shellStarter = new ShellStarter();

		try {

			String tag = user + "/" + repo;

			com = "docker login -u " + user + " -p" + passwd + " " + server;
			System.out.println(com);
			shellStarter.executeCommand(com);

//			com = "docker commit" + change + " " + container + " " + tag + ":" + version;
			com = "docker commit " + container + " " + tag + ":" + version;
			shellStarter.executeCommand(com);

			com = "docker push " + tag + ":" + version;
			System.out.println(com);
			comReturn = shellStarter.executeCommand(com);

			com = "docker logout";
			System.out.println(com);
			shellStarter.executeCommand(com);

			return ResponseEntity.ok(comReturn);

		} catch (RuntimeException | InternalError ex) {

			shellStarter.executeCommand("docker logout");

			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Parar container
	@PostMapping(value = "/stop/{idContainer}")
	private ResponseEntity<?> stop(@PathVariable("idContainer") String container) {
		try {
			com = "docker stop " + container;

			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Inicializar container
	@PostMapping(value = "/start/{idContainer}")
	private ResponseEntity<?> start(@PathVariable("idContainer") String container) {
		try {

			com = "docker start " + container;

			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Erro no segundo comando, executando os coms no terminal com resultado positivo! Problema deve estar nas threds  do ShellController
//	https://medium.com/@sh.tsang/docker-tutorial-4-exporting-container-and-saving-image-c3a7d792cfb6
//	https://tecadmin.net/export-and-import-docker-containers/
//	Exportar container para image
	@PostMapping(value = "/export")
	private ResponseEntity<?> exportToImage(@RequestParam("container") String container,
			@RequestParam("imageName") String image) {

		if (image.isEmpty() || container.isEmpty()) {
			String msg = "Pârametros inválidos! O que foi recebido ::: container=" + container + " || imageName="
					+ image;
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
		}

//		Random name
		int randomNumber = (int) (Math.random() * 99999999 + 1);

//		Temporary Folder
		String folder = "/tmp/tmp-" + randomNumber + "/";
		String filePath = folder + image + ".tar.gz";

		System.out.println("mkdir " + folder);
		System.out.println("docker export " + container + " | gzip > " + filePath);
		System.out.println("docker export " + container + " > " + filePath);
		System.out.println("gunzip -c " + filePath + " | docker import - " + image);
		System.out.println("docker inspect " + image);

		try {

			com = "mkdir " + folder;
			shell.executeCommand(com);
			System.out.println("1" + com);

//			com = "docker export " + container + " | gzip > " + filePath;
//			shell.executeCommand(com);
//			System.out.println("2" + com);

			com = "docker export " + container + " > " + filePath;
			shell.executeCommand(com);
			System.out.println("2" + com);

			com = "gunzip -c " + filePath + " | docker import - " + image;
			shell.executeCommand(com);
			System.out.println("3" + com);

			com = "docker inspect " + image;
			String comReturn = shell.executeCommand(com);
			System.out.println("4" + com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Excluir container
	@DeleteMapping(value = "/{idDockerContainer}")
	private ResponseEntity<?> delete(@PathVariable("idDockerContainer") String idDockerContainer) {
		try {
			if (idDockerContainer.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetro id vazio");

			com = "docker rm -f " + idDockerContainer;

			try {
				String comReturn = shell.executeCommand(com);

				return ResponseEntity.ok(comReturn);
			} catch (InternalError e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
			}
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Listar containers
	@GetMapping(value = "/{param}")
	private ResponseEntity<?> list(@PathVariable("param") String param) {
		try {

			this.com = "docker inspect " + param;

			return ResponseEntity.ok(shell.executeCommand(this.com));
		} catch (Exception | InternalError ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Listar containers
	@GetMapping(value = "/")
	private ResponseEntity<?> list() {
		try {

			this.com = "curl --unix-socket /var/run/docker.sock http:/v1.24/containers/json";

			return ResponseEntity.ok(shell.executeCommand(this.com));
		} catch (Exception | InternalError ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Baixar container .tar.gz
	@GetMapping(value = "/down/{idContainer}")
	private ResponseEntity<?> download() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

	private File getFile(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("File does not exist");
		}
		return file;
	}
}
