package sisrh.dto;

import javax.xml.bind.annotation.XmlElement;

public class Usuario {

	@XmlElement(name = "nome")
	private String nome;

	@XmlElement(name = "perfil")
	private Integer perfil;

	@XmlElement(name = "matricula")
	private String matricula;

	@XmlElement(name = "senha")
	private String senha;

	public Usuario() {

	}

	public Usuario(String nome, Integer perfil, String matricula, String senha) {
		super();
		this.nome = nome;
		this.perfil = perfil;
		this.matricula = matricula;
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public Integer getPerfil() {
		return perfil;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getSenha() {
		return senha;
	}
}
