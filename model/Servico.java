package model;

public class Servico {
    private String nome;
    private int tempoEstimadoMinutos;
    private double valor;
    private String categoria;

    public Servico(String nome, int tempoEstimadoMinutos, double valor, String categoria) {
        this.nome = nome;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.valor = valor;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTempoEstimadoMinutos() {
        return tempoEstimadoMinutos;
    }

    public void setTempoEstimadoMinutos(int tempoEstimadoMinutos) {
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return String.format("Servico [Nome: %s | Categoria: %s | Tempo Estimado: %d min | Valor: R$ %f]",
                nome, categoria, tempoEstimadoMinutos, valor);
    }
}