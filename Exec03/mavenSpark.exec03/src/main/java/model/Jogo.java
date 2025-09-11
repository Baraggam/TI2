package model;

import java.util.Date;

public class Jogo {
	private int id;
	private String nome;
	private Date lancamento;

	public Jogo() {
		this.id = -1;
		this.nome = "";
		this.lancamento = null;
	}

	public Jogo(int codigo, String nome, Date lancamento) {
		this.id = codigo;
		this.nome = nome;
		this.lancamento = lancamento;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getLancamento() {
		return lancamento;
	}

	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}

	@Override
	public String toString() {
		return "Jogo [id=" + id + ", nome=" + nome + ", lançamento=" + lancamento + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Jogo other = (Jogo) obj;
		return this.id == other.id;
	}
}
