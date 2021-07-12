package br.com.lasse.DockerMage.control;

import java.io.File;
import java.io.FileNotFoundException;
//https://medium.com/the-code-review/top-10-docker-commands-you-cant-live-without-54fb6377f481
import java.io.FileOutputStream;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/images")
public class ImageController {

	private static ShellStarter shell = new ShellStarter();
	private String com;

//	Realizar upload de .tar.gz
	@PostMapping(value = "/")
	private ResponseEntity<?> images(@RequestParam("file") MultipartFile file) {

		System.out.println(file.getContentType());
		if (file.getContentType().equals("application/gzip"))
			System.out.println("caraio");

		if (!file.getContentType().equals("application/gzip"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Upload errado! Somente arquivos .tar.gz são aceitos!");

		String[] imageName = file.getOriginalFilename().split("\\.");

		if (file.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo vazio");

		int randomNumber = (int) (Math.random() * 99999999 + 1);

		String folder = "/tmp/tmp-" + randomNumber + "/";

		String localPath = folder + imageName[0];
		try {
			com = "mkdir " + folder;
			shell.executeCommand(com);

			FileOutputStream ots = new FileOutputStream(localPath);
			ots.write(file.getBytes());
			ots.close();

			com = "docker load --input " + localPath;
			shell.executeCommand(com);

			return ResponseEntity.ok(
					"Upload realizado com sucesso! Se ao salvar você não deu a tag, listar e alterar a imagem sem nome");
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Realizar upload para registry
	@PostMapping(value = "/up")
	private ResponseEntity<?> registryUpload() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Deletar Imagem
	@DeleteMapping(value = "/{idImage}")
	private ResponseEntity<?> delete(@PathVariable("idImage") String idImage) {

		if (idImage.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválido!");

		try {
			com = "docker rmi " + idImage;
			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	 Listar Images do registry
	@GetMapping(value = "/registry/{param}")
	private ResponseEntity<?> listRegistry() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Listar imagens servidor local
	@GetMapping(value = "/local")
	private ResponseEntity<?> listLocal(@RequestParam(value = "param", required = false) String param) {

		if (param == null)
			com = "curl --unix-socket /var/run/docker.sock http:/v1.24/images/json";
		else
			com = "docker images | grep " + param; //com = "curl --unix-socket /var/run/docker.sock http:/v1.24/images/json | grep " + param;
		
		System.out.println(com);
		
		try {
			String comReturn = shell.executeCommand(com);

			return ResponseEntity.ok(comReturn);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	docker save ubuntu:latest -o ubuntu-redirect.tar.gz
//	docker save $IMAGEMCOMTAG -o $NOMEDOARQUIVO.tar.gz
//	Baixar imagem .tar.gz
	@GetMapping(value = "/registry/down")
	private HttpEntity<?> downloadRegistry() {

		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Baixar imagem .tar.gz 
	@GetMapping(value = "/local/down/{image}")
	private HttpEntity<?> downloadLocal(@PathVariable("image") String imageTag) {
		if (imageTag.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wront parameters");

		if (!imageTag.contains(":"))
			imageTag += ":latest";

		String[] imageTagSplit = imageTag.split(":");

		int randomNumber = (int) (Math.random() * 99999999 + 1);

		String folder = "/tmp/tmp-" + randomNumber + "/";

		String localPath = folder + imageTagSplit[0];

		try {

			com = "mkdir " + folder;
			shell.executeCommand(com);

			com = "docker save " + imageTag + " -o " + localPath;
			shell.executeCommand(com);

//			arquivo
			File file = getFile(localPath);
			byte[] document = FileCopyUtils.copyToByteArray(file);

//			request
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "gzip"));
			header.set("Content-Disposition", "inline; filename=" + file.getName() + ".tar.gz");
			header.setContentLength(document.length);

			return new HttpEntity<byte[]>(document, header);
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
