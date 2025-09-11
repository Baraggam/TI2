package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import model.Jogo;

public class JogoDAO extends DAO {
	public JogoDAO() {
		super();
		conectar();
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

	public boolean atualizarLancamentoJogo(Jogo jogo) {
		boolean status = false;
		try {
			java.sql.Date sqlDate = new java.sql.Date(jogo.getLancamento().getTime());
			Statement st = conexao.createStatement();
			String sql = "UPDATE jogos SET lancamento = '" + sqlDate + "' WHERE id = " + jogo.getId();
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

	public Jogo getById(int id) {
		Jogo jogo = null;
		try {
			Statement st = conexao.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM jogos WHERE id = " + id);
			if (rs.next()) {
				jogo = new Jogo(rs.getInt("id"), rs.getString("nome"), rs.getDate("lancamento"));
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return jogo;
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