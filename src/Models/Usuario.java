package Models;

public class Usuario {
    private String id;
    private String nome;
    private String contato;
    private boolean anonimo;

    //--- 2 Construtores pq o anonimo eh setado na criação e não pode mudar depois
    //-- 1 pra anonimo = true e um pra anonimo = false

    // Usuário nao anonimo
    public Usuario(String nome, String contato) {
        this.id = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nome = nome;
        this.contato = contato;
        this.anonimo = false;
    }

    // Usuário anônimo
    public Usuario() {
        this.id = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nome = "Anônimo";
        this.contato = null;
        this.anonimo = true;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}
