package sisrh.soap;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;

import sisrh.banco.Banco;
import sisrh.dto.Usuario;
import sisrh.dto.Usuarios;
import sisrh.seguranca.Autenticador;

@WebService
@SOAPBinding(style = Style.DOCUMENT)
public class ServicoUsuario {
	@Resource
	WebServiceContext context;

	@WebMethod(action = "listar")
	public Usuarios listar() throws Exception {
		Usuarios usuarios = new Usuarios();
		List<Usuario> lista = Banco.listarUsuarios();
		usuarios.getUsuarios().addAll(lista);
		return usuarios;
	}

	@WebMethod(action = "obter")
	public Usuario obterUsuario(String matricula) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		validarMatricula(matricula);
		Usuario usuario = Banco.buscarUsuarioPorMatricula(matricula);
		if (usuario == null) {
			throw new Exception("Usuário não encontrado!");
		}
		return usuario;
	}

	@WebMethod(action = "incluir")
	public Usuario incluirUsuario(Usuario usuario) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		if (usuario == null || usuario.getMatricula() == null) {
			throw new IllegalArgumentException("Usuário ou matrícula não podem ser nulos.");
		}
		return Banco.incluirUsuario(usuario);
	}

	@WebMethod(action = "alterar")
	public Usuario alterarUsuario(String matricula, Usuario usuarioAlterado) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		validarMatricula(matricula);
		Usuario usuarioExistente = Banco.buscarUsuarioPorMatricula(matricula);
		if (usuarioExistente == null) {
			throw new Exception("Usuário não encontrado!");
		}
		return Banco.alterarUsuario(matricula, usuarioAlterado);
	}

	@WebMethod(action = "excluir")
	public String excluirUsuario(String matricula) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		validarMatricula(matricula);
		Usuario usuario = Banco.buscarUsuarioPorMatricula(matricula);
		if (usuario == null) {
			throw new Exception("Usuário não encontrado!");
		}
		Banco.excluirUsuario(matricula);
		return "Usuário " + matricula + " excluído com sucesso!";
	}

	private void validarMatricula(String matricula) {
		if (matricula == null || matricula.trim().isEmpty()) {
			throw new IllegalArgumentException("A matrícula não pode ser nula ou vazia.");
		}
	}
}
