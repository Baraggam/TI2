package app;

import service.JogoService;
import static spark.Spark.*;

public class Aplicacao {

	private static JogoService JogoService = new JogoService();

	public static void main(String[] args) {
		port(6789);

		// Página inicial
		get("/", (request, response) -> JogoService.getForm());

		// Rota para inserir jogo
		post("/inserir", (request, response) -> JogoService.insert(request, response));

		// Rota para deletar jogo
		post("/deletar", (request, response) -> JogoService.delete(request, response));

		// Rota para atualizar jogo
		post("/atualizar", (request, response) -> JogoService.update(request, response));

		// Rota para ler todos os jogos
		get("/ler", (request, response) -> JogoService.getAll(request, response));
	}
}