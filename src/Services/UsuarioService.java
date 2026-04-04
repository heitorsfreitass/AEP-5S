package Services;

import Models.Usuario;
import Repositories.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario criarIdentificado(String nome, String contato) {
        Usuario usuario = new Usuario(nome.trim(), contato == null ? "" : contato.trim());
        usuarioRepository.salvar(usuario);
        return usuario;
    }

    public Usuario criarAnonimo() {
        Usuario usuario = new Usuario();
        usuarioRepository.salvar(usuario);
        return usuario;
    }
}