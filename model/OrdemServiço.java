package model;


public class OrdemServico {
    private int codigo;
    private String nomeCliente;
    private String modeloVeiculo;
    private String placaVeiculo;
    private String data;
    private StatusOrdem status;
    private double valorEstimado;
    private Servico servico;
    private Box box;

    public OrdemServico(int codigo, String nomeCliente, String modeloVeiculo, String placaVeiculo,
                        String data, double valorEstimado, Servico servico) {
        this.codigo = codigo;
        this.nomeCliente = nomeCliente;
        this.modeloVeiculo = modeloVeiculo;
        this.placaVeiculo = placaVeiculo;
        this.data = data;
        this.valorEstimado = valorEstimado;
        this.servico = servico;
        this.status = StatusOrdem.ABERTA;
        this.box = null;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(String placaVeiculo) {
        this.placaVeiculo = placaVeiculo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public StatusOrdem getStatus() {
        return status;
    }

    public void setStatus(StatusOrdem status) {
        this.status = status;
    }

    public double getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(double valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public String getDetalhesFormatados() {
        StringBuilder sb = new StringBuilder();
        sb.append("-\n");
        sb.append(String.format(" ORDEM DE SERVIÇO Nº %d - Status: %s\n", codigo, status.getDescricao()));
        sb.append("-\n");
        sb.append(String.format(" Data de Abertura: %s", data));
        sb.append(String.format(" Cliente: %s\n", nomeCliente));
        sb.append(String.format(" Veiculo: %s (Placa: %s)", modeloVeiculo, placaVeiculo));
        sb.append(String.format(" Valor Estimado: R$ %f", valorEstimado));
        
        if (servico != null) {
            sb.append(" -\n");
            sb.append(String.format(" Servico: %s", servico.getNome()));
            sb.append(String.format(" Categoria: %s | Tempo Est.: %d min | Valor: R$ %f\n",
                    servico.getCategoria(), servico.getTempoEstimadoMinutos(), servico.getValor()));
        } else {
            sb.append(" Serviço: Nao foi informado\n");
        }

        sb.append(" -\n");
        if (box != null) {
            sb.append(String.format(" Box Atribuido: Nº %d - Localizacaolo: %s\n", box.getNumero(), box.getLocalizacao()));
            if (box.getMecanicoResponsavel() != null) {
                sb.append(String.format(" Mecanico Responssvel: %s (Esp.: %s, Tel: %s)\n",
                        box.getMecanicoResponsavel().getNome(),
                        box.getMecanicoResponsavel().getEspecialidade(),
                        box.getMecanicoResponsavel().getTelefone()));
            } else {
                sb.append(" Mecanico Responsavel: Nenhum mecanico associado a ese box\n");
            }
        } else {
            sb.append(" Box Atribuido: Nenhum (Ordem sem box vinculado)\n");
        }
        sb.append("-");
        return sb.toString();
    }

    @Override
    public String toString() {
        String boxInfo = (box != null) ? ("Box " + box.getNumero()) : "Sem box";
        return String.format("OS #%d | %s | %s (%s) | Status: %s | %s",
                codigo, nomeCliente, modeloVeiculo, placaVeiculo, status.getDescricao(), boxInfo);
    }
}