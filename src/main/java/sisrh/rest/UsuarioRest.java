package sisrh.rest;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import sisrh.banco.Banco;
import sisrh.dto.Usuario;

@Api
@Path("/usuario")
public class UsuarioRest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Listar todos os usuários")
	public Response listarUsuarios() throws Exception {
		List<Usuario> lista = Banco.listarUsuarios();
		return Response.ok(new GenericEntity<List<Usuario>>(lista) {
		}).build();
	}

	@GET
	@Path("{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Obter um usuário pelo número de matrícula")
	public Response obterUsuario(@PathParam("matricula") String matricula) {
		try {
			Usuario usuario = Banco.buscarUsuarioPorMatricula(matricula);
			if (usuario != null) {
				return Response.ok(usuario).build();
			} else {
				return Response.status(Status.NOT_FOUND).entity(criarMensagemErro("Usuário não encontrado!")).build();
			}
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(criarMensagemErro("Falha para obter usuário!", e)).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Incluir um novo usuário")
	public Response incluirUsuario(Usuario usuario) {
		try {
			// Validação de entrada pode ser feita aqui
			Usuario novoUsuario = Banco.incluirUsuario(usuario);
			return Response.status(Status.CREATED).entity(novoUsuario).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(criarMensagemErro("Falha na inclusão do usuário!", e)).build();
		}
	}

	@PUT
	@Path("{matricula}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Alterar um usuário existente")
	public Response alterarUsuario(@PathParam("matricula") String matricula, Usuario usuario) {
		try {
			if (Banco.buscarUsuarioPorMatricula(matricula) == null) {
				return Response.status(Status.NOT_FOUND).entity(criarMensagemErro("Usuário não encontrado!")).build();
			}
			Usuario usuarioAlterado = Banco.alterarUsuario(matricula, usuario);
			return Response.ok(usuarioAlterado).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(criarMensagemErro("Falha na alteração do usuário!", e)).build();
		}
	}

	@DELETE
	@Path("{matricula}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Excluir um usuário pelo número de matrícula")
	public Response excluirUsuario(@PathParam("matricula") String matricula) {
		try {
			if (Banco.buscarUsuarioPorMatricula(matricula) == null) {
				return Response.status(Status.NOT_FOUND).entity(criarMensagemErro("Usuário não encontrado!")).build();
			}
			Banco.excluirUsuario(matricula);
			return Response.ok().entity("{ \"mensagem\" : \"Usuário " + matricula + " excluído!\" }").build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(criarMensagemErro("Falha na exclusão do usuário!", e)).build();
		}
	}

	private String criarMensagemErro(String mensagem) {
		return "{ \"mensagem\" : \"" + mensagem + "\" }";
	}

	private String criarMensagemErro(String mensagem, Exception e) {
		return "{ \"mensagem\" : \"" + mensagem + "\", \"detalhe\" : \"" + e.getMessage() + "\" }";
	}
}
