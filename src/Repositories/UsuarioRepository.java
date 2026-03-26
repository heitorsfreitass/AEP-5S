package Repositories;

import Models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private List<Usuario> usuarios = new ArrayList<>();

    public void salvar(Usuario usuario)
    {
        usuarios.add(usuario);
    }

    public Usuario buscarPorId(String id)
    {
        Usuario resultado = null;

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equalsIgnoreCase(id)) {
                resultado = usuarios.get(i);
            }
        }
        return resultado;
    }

    public List<Usuario> listarTodos()
    {
        return usuarios;
    }
}
