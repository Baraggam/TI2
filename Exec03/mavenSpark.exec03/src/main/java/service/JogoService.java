package service;

import model.Jogo;
import dao.JogoDAO;
import java.io.File;
import spark.Request;
import java.util.Date;
import spark.Response;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class JogoService {

	private JogoDAO jogoDAO = new JogoDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;

	public JogoService() {
		makeForm();
	}

	public void makeForm() {
		makeForm(FORM_INSERT, new Jogo());
	}

	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Jogo());
	}

	public void makeForm(int tipo, Jogo jogo) {
		String nomeArquivo = "form.html";
		form = "";
		try {
			Scanner entrada = new Scanner(new File(nomeArquivo));
			while (entrada.hasNext()) {
				form += (entrada.nextLine() + "\n");
			}
			entrada.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		String umJogo = "";
		if (tipo != FORM_INSERT) {
			umJogo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/jogo/list/1\">Novo Jogo</a></b></font></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t</table>";
			umJogo += "\t<br>";
		}

		if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/jogo/";
			String name, nome, buttonLabel;
			if (tipo == FORM_INSERT) {
				action += "insert";
				name = "Inserir Jogo";
				nome = "";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + jogo.getId();
				name = "Atualizar Jogo (ID " + jogo.getId() + ")";
				nome = jogo.getNome();
				buttonLabel = "Atualizar";
			}
			umJogo += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umJogo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name
					+ "</b></font></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\""
					+ nome + "\"></td>";
			umJogo += "\t\t\t<td>Data de lançamento: <input class=\"input--register\" type=\"date\" name=\"lancamento\" value=\""
					+ (jogo.getLancamento() != null ? jogo.getLancamento().toString() : "") + "\"></td>";
			umJogo += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\"" + buttonLabel
					+ "\" class=\"input--main__style input--button\"></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t</table>";
			umJogo += "\t</form>";
		} else if (tipo == FORM_DETAIL) {
			umJogo += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Jogo (ID "
					+ jogo.getId() + ")</b></font></td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t\t<tr>";
			umJogo += "\t\t\t<td>&nbsp;Nome: " + jogo.getNome() + "</td>";
			umJogo += "\t\t\t<td>Data de lançamento: "
					+ (jogo.getLancamento() != null ? jogo.getLancamento().toString() : "") + "</td>";
			umJogo += "\t\t\t<td>&nbsp;</td>";
			umJogo += "\t\t</tr>";
			umJogo += "\t</table>";
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-JOGO>", umJogo);

		String list = "<table class=\"jogos-table\">";
		list += "<tr><th>ID</th><th>Nome</th><th>Data de Lançamento</th><th>Ações</th></tr>";

		Jogo[] jogos = jogoDAO.getJogos();
		for (Jogo j : jogos) {
			list += "<tr>";
			list += "<td>" + j.getId() + "</td>";
			list += "<td>" + j.getNome() + "</td>";
			list += "<td>"
					+ (j.getLancamento() != null ? new SimpleDateFormat("yyyy-MM-dd").format(j.getLancamento()) : "")
					+ "</td>";
			list += "<td class=\"action-btns\">"
					+ "<button type=\"button\" class=\"btn-atualizar\" onclick=\"atualizarJogo(" + j.getId()
					+ ")\">Atualizar Data</button> "
					+ "<button type=\"button\" class=\"btn-deletar\" onclick=\"deletarJogo(" + j.getId()
					+ ")\">Deletar</button>"
					+ "</td>";
			list += "</tr>";
		}
		list += "</table>";

		form = form.replace("<LISTAR-JOGO>", list);
	}

	public Object insert(Request request, Response response) {
		String nome = request.queryParams("nome");
		String dataStr = request.queryParams("dataLancamento");
		Date lancamento = null;
		try {
			lancamento = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
		} catch (Exception e) {
			lancamento = null;
		}

		String resp = "";

		// Gere um novo id automaticamente
		int novoId = jogoDAO.getMaxId() + 1;
		Jogo jogo = new Jogo(novoId, nome, lancamento);

		if (jogoDAO.inserirJogo(jogo)) {
			resp = "Jogo (" + nome + ") inserido!";
			response.status(201); // 201 Created
		} else {
			resp = "Jogo (" + nome + ") não inserido!";
			response.status(404); // 404 Not found
		}

		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
				"<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
	}

	public Object getAll(Request request, Response response) {
		makeForm();
		response.header("Content-Type", "text/html");
		response.header("Content-Encoding", "UTF-8");
		return form;
	}

	public Object update(Request request, Response response) {
		int id = Integer.parseInt(request.queryParams("id"));
		Jogo jogo = jogoDAO.getById(id);
		String resp = "";

		if (jogo != null) {
			String dataStr = request.queryParams("dataLancamento");
			Date lancamento = null;
			try {
				lancamento = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
			} catch (Exception e) {
				lancamento = null;
			}
			jogo.setLancamento(lancamento);
			jogoDAO.atualizarLancamentoJogo(jogo);
			response.status(200); // success
			resp = "Jogo (ID " + jogo.getId() + ") atualizado!";
		} else {
			response.status(404); // 404 Not found
			resp = "Jogo (ID " + id + ") não encontrado!";
		}
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
				"<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
	}

	public Object delete(Request request, Response response) {
		int id = Integer.parseInt(request.queryParams("id"));
		Jogo jogo = jogoDAO.getById(id);
		String resp = "";

		if (jogo != null) {
			jogoDAO.excluirJogo(id);
			response.status(200); // success
			resp = "Jogo (" + id + ") excluído!";
		} else {
			response.status(404); // 404 Not found
			resp = "Jogo (" + id + ") não encontrado!";
		}
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">",
				"<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
	}

	public String getForm() {
		return form;
	}
}