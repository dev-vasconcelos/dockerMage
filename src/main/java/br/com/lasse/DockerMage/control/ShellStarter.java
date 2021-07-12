package br.com.lasse.DockerMage.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShellStarter {

	String executeCommand(String command) {
		StringBuffer output = new StringBuffer();

		Process p;

		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			if (p.exitValue() == 0) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

				String line = "";

				while ((line = reader.readLine()) != null)
					output.append(line + "\n");
			} else {
				throw new RuntimeException("Ocorreu um erro por parte do cleinte"); // 400
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InternalError("Ocorreu um erro interno");
		}

		return output.toString();
	}

}
