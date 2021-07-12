package br.com.lasse.DockerMage.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/composes")
public class ComposeController {

	private static ShellStarter shell = new ShellStarter();

//	criar docker-compose
	@PostMapping(value = "/")
	private ResponseEntity<?> create() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Rodar compose
	@PostMapping(value = "/run")
	private ResponseEntity<?> run() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Upload de arquivo compose
	@PostMapping(value = "/up")
	private ResponseEntity<?> upload() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Listar compose
	@DeleteMapping(value = "/{param}")
	private ResponseEntity<?> list() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

//	Deletar compose
	@GetMapping(value = "/{idCompose}")
	private ResponseEntity<?> delete() {
		try {
			return null;
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
		}
	}

}
