package maven.exec02;

import java.sql.*;

public class DAO {
	private Connection conexao;

	public DAO() {
		conexao = null;
	}

	public boolean conectar() {
		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String mydatabase = "ti2";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
		String username = "ti2cc";
		String password = "ti@cc";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}

	public boolean close() {
		boolean status = false;

		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	public boolean inserirJogo(Jogo jogo) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			String sql = "INSERT INTO jogos (id, nome, lancamento) VALUES ("
					+ jogo.getId() + ", '"
					+ jogo.getNome() + "', '"
					+ new java.sql.Date(jogo.getLancamento().getTime()) + "')";
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public boolean atualizarLancamentoJogo(int id, java.util.Date novaData) {
		boolean status = false;
		try {
			java.sql.Date sqlDate = new java.sql.Date(novaData.getTime());
			Statement st = conexao.createStatement();
			String sql = "UPDATE jogos SET lancamento = '" + sqlDate + "' WHERE id = " + id;
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	public boolean excluirJogo(int id) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM jogos WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public Jogo[] getJogos() {
		Jogo[] jogos = null;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM jogos");
			if (rs.next()) {
				rs.last();
				jogos = new Jogo[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					jogos[i] = new Jogo(rs.getInt("id"), rs.getString("nome"), rs.getDate("lancamento"));
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return jogos;
	}

	public int getMaxId() {
		int maxId = 0;
		try {
			Statement st = conexao.createStatement();
			ResultSet rs = st.executeQuery("SELECT MAX(id) AS max_id FROM jogos");
			if (rs.next()) {
				maxId = rs.getInt("max_id");
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return maxId;
	}

}