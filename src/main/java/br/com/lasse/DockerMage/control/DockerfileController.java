package br.com.lasse.DockerMage.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dockerfiles")
public class DockerfileController {

	private static ShellStarter shell = new ShellStarter();

//	Criar dockerfile
	@PostMapping(value = "/")
	private ResponseEntity<?> create() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Buildar dockerfile
	@PostMapping(value = "/build")
	private ResponseEntity<?> build() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	upload de dockerfile
	@PostMapping(value = "/up")
	private ResponseEntity<?> upload() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Deletar dockerfile
	@DeleteMapping(value = "/{idDockerfile}")
	private ResponseEntity<?> delete() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Listar dockerfile
	@GetMapping(value = "/{param}")
	private ResponseEntity<?> list() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

}
