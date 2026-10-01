package model;

public enum StatusOrdem {
    ABERTA("Aberta"),
    EM_EXECUCAO("Em Execucaoo"),
    FINALIZADA("Finalizada");

    private final String descricao;

    StatusOrdem(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}