package maven.exec02;

import java.util.Date;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Principal {

	public static void main(String[] args) {

		DAO dao = new DAO();

		dao.conectar();

		Scanner scanner = new Scanner(System.in);
		int opcao = 0;

		do {
			System.out.println("Menu de opções:");
			System.out.println("1)  Listar");
			System.out.println("2)  Inserir");
			System.out.println("3)  Excluir");
			System.out.println("4)  Atualizar");
			System.out.println("0)  Sair");
			System.out.print("Escolha uma opção: ");
			opcao = scanner.nextInt();
			scanner.nextLine(); // Limpa o buffer

			int id = -1;
			String nome = "";
			Date lancamento = null;

			switch (opcao) {
				case 1:
					Jogo[] jogos = dao.getJogos();
					if (jogos != null) {

						System.out.println("==== Mostrar jogos === ");
						for (int i = 0; i < jogos.length; i++) {
							System.out.println(jogos[i].toString());
						}
					}
					break;
				case 2:
					System.out.println("Digite o nome do jogo: ");
					nome = scanner.nextLine().strip();

					System.out.println("Digite a data de lançamento do jogo: (dd/mm/aaaa) ");
					lancamento = getDateFromString(scanner.nextLine());

					Jogo jogo = new Jogo(dao.getMaxId(), nome, lancamento);
					if (dao.inserirJogo(jogo) == true) {
						System.out.println("Inserção com sucesso -> " + jogo.toString());
					}

					break;
				case 3:
					System.out.println("Digite o id do jogo para exclusão: ");
					id = scanner.nextInt();
					dao.excluirJogo(id);
					break;
				case 4:
					System.out.println("Digite o id do jogo para atualização: ");
					id = scanner.nextInt();

					scanner.nextLine(); // Limpa o buffer

					System.out.println("Digite a nova data de lançamento do jogo: ");
					lancamento = getDateFromString(scanner.nextLine());

					dao.atualizarLancamentoJogo(id, lancamento);
					break;
				case 0:
					System.out.println("Saindo...");
					break;
				default:
					System.out.println("Opção inválida!");
			}
		} while (opcao != 0);

		scanner.close();
		dao.close();
	}

	private static Date getDateFromString(String dataStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = formatter.parse(dataStr.strip());
		} catch (ParseException e) {
			System.out.println("Data inválida. Formato esperado: dd/mm/aaaa");
		}
		return date;
	}
}
