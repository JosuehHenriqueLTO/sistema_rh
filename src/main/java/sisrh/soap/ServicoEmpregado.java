package sisrh.soap;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.WebServiceContext;

import sisrh.banco.Banco;
import sisrh.dto.Empregado;
import sisrh.dto.Empregados;
import sisrh.seguranca.Autenticador;

@WebService
@SOAPBinding(style = Style.RPC)
public class ServicoEmpregado {
	@Resource
	WebServiceContext context;

	@WebMethod(action = "listar")
	public Empregados listar() throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		Empregados empregados = new Empregados();
		List<Empregado> lista = Banco.listarEmpregados();
		empregados.getEmpregados().addAll(lista);
		return empregados;
	}

	@WebMethod(action = "listarAtivos")
	public Empregados listarAtivos() throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		Empregados empregados = new Empregados();
		List<Empregado> lista = Banco.listarAtivos();
		empregados.getEmpregados().addAll(lista);
		return empregados;
	}

	@WebMethod(action = "listarInativos")
	public Empregados listarInativos() throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		Empregados empregados = new Empregados();
		List<Empregado> lista = Banco.listarInativos();
		empregados.getEmpregados().addAll(lista);
		return empregados;
	}

	@WebMethod(action = "obter")
	public Empregado obterEmpregado(String matricula) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		validarMatricula(matricula);
		Empregado empregado = Banco.buscarEmpregadoPorMatricula(matricula);
		if (empregado == null) {
			throw new Exception("Empregado não encontrado!");
		}
		return empregado;
	}

	@WebMethod(action = "incluir")
	public Empregado incluirEmpregado(Empregado empregado) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		if (empregado == null || empregado.getMatricula() == null) {
			throw new IllegalArgumentException("Empregado ou matrícula não podem ser nulos.");
		}
		return Banco.incluirEmpregado(empregado);
	}

	@WebMethod(action = "alterar")
	public Empregado alterarEmpregado(String matricula, Empregado empregadoAlterado) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		validarMatricula(matricula);
		Empregado empregadoExistente = Banco.buscarEmpregadoPorMatricula(matricula);
		if (empregadoExistente == null) {
			throw new Exception("Empregado não encontrado!");
		}
		return Banco.alterarEmpregado(matricula, empregadoAlterado);
	}

	@WebMethod(action = "excluir")
	public String excluirEmpregado(String matricula) throws Exception {
		Autenticador.autenticarUsuarioSenha(context);
		validarMatricula(matricula);
		Empregado empregado = Banco.buscarEmpregadoPorMatricula(matricula);
		if (empregado == null) {
			throw new Exception("Empregado não encontrado!");
		}
		Banco.excluirEmpregado(matricula);
		return "Empregado " + matricula + " excluído com sucesso!";
	}

	private void validarMatricula(String matricula) {
		if (matricula == null || matricula.trim().isEmpty()) {
			throw new IllegalArgumentException("A matrícula não pode ser nula ou vazia.");
		}
	}
}
