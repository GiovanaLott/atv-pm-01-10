package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Box {
    private int numero;
    private String tipoServicoPermitido;
    private int capacidadeMaxima;
    private String localizacao;
    private Mecanico mecanicoResponsavel;
    private List<OrdemServico> ordensAtribuidas;
    private int totalOrdensFinalizadas;

    public Box(int numero, String tipoServicoPermitido, int capacidadeMaxima, String localizacao, Mecanico mecanicoResponsavel) {
        this.numero = numero;
        this.tipoServicoPermitido = tipoServicoPermitido;
        this.capacidadeMaxima = capacidadeMaxima;
        this.localizacao = localizacao;
        this.mecanicoResponsavel = mecanicoResponsavel;
        this.ordensAtribuidas = new ArrayList<>();
        this.totalOrdensFinalizadas = 0;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipoServicoPermitido() {
        return tipoServicoPermitido;
    }

    public void setTipoServicoPermitido(String tipoServicoPermitido) {
        this.tipoServicoPermitido = tipoServicoPermitido;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Mecanico getMecanicoResponsavel() {
        return mecanicoResponsavel;
    }

    public void setMecanicoResponsavel(Mecanico mecanicoResponsavel) {
        this.mecanicoResponsavel = mecanicoResponsavel;
    }

    public List<OrdemServico> getOrdensAtribuidas() {
        return Collections.unmodifiableList(ordensAtribuidas);
    }

    public int getTotalOrdensFinalizadas() {
        return totalOrdensFinalizadas;
    }

    public boolean isCheio() {
        return ordensAtribuidas.size() >= capacidadeMaxima;
    }

    public boolean adicionarOrdem(OrdemServico ordem) {
        if (isCheio()) {
            return false;
        }
        if (!ordensAtribuidas.contains(ordem)) {
            ordensAtribuidas.add(ordem);
            return true;
        }
        return false;
    }

    public boolean removerOrdem(OrdemServico ordem) {
        return ordensAtribuidas.remove(ordem);
    }

    public void incrementarTotalFinalizadas() {
        this.totalOrdensFinalizadas++;
    }

    @Override
    public String toString() {
        String mecNome = (mecanicoResponsavel != null) ? mecanicoResponsavel.getNome() : "Nenhum";
        return String.format("Box nº %d [Serviço: %s | Capacidade: %d (Ocupadas: %d) | Local: %s | Mecanico: %s | Finalizado: %d]",
                numero, tipoServicoPermitido, capacidadeMaxima, ordensAtribuidas.size(), localizacao, mecNome, totalOrdensFinalizadas);
    }
}