package service;

import model.Box;
import model.Mecanico;
import model.OrdemServico;
import model.Servico;
import model.StatusOrdem;

import java.util.ArrayList;
import java.util.List;

public class OficinaService {
    private List<Mecanico> mecanicos;
    private List<Box> boxes;
    private List<OrdemServico> ordens;
    private int proximoCodigoOrdem;

    public OficinaService() {
        this.mecanicos = new ArrayList<>();
        this.boxes = new ArrayList<>();
        this.ordens = new ArrayList<>();
        this.proximoCodigoOrdem = 1;
    }

    public List<Mecanico> getMecanicos() {
        return mecanicos;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public List<OrdemServico> getOrdens() {
        return ordens;
    }

    public void adicionarMecanico(Mecanico mecanico) {
        this.mecanicos.add(mecanico);
    }

    public void adicionarBox(Box box) {
        this.boxes.add(box);
    }

    public Mecanico buscarMecanicoPorCpf(String cpf) {
        for (Mecanico m : mecanicos) {
            if (m.getCpf().equalsIgnoreCase(cpf.trim())) {
                return m;
            }
        }
        return null;
    }

    public Box buscarBoxPorNumero(int numero) {
        for (Box b : boxes) {
            if (b.getNumero() == numero) {
                return b;
            }
        }
        return null;
    }

    public OrdemServico buscarOrdemPorCodigo(int codigo) {
        for (OrdemServico os : ordens) {
            if (os.getCodigo() == codigo) {
                return os;
            }
        }
        return null;
    }

    
    public OrdemServico cadastrarOrdemServico(String nomeCliente, String modeloVeiculo, String placaVeiculo,
                                             String data, double valorEstimado, Servico servico) {
        OrdemServico novaOrdem = new OrdemServico(
                proximoCodigoOrdem++,
                nomeCliente,
                modeloVeiculo,
                placaVeiculo,
                data,
                valorEstimado,
                servico
        );
        ordens.add(novaOrdem);
        return novaOrdem;
    }

    public boolean associarMecanicoABox(String cpfMecanico, int numeroBox) {
        Mecanico mecanico = buscarMecanicoPorCpf(cpfMecanico);
        if (mecanico == null) {
            System.out.println("Erro: Mecânico não encontrado com o CPF informado.");
            return false;
        }

        Box boxDestino = buscarBoxPorNumero(numeroBox);
        if (boxDestino == null) {
            System.out.println("Erro: Box nº " + numeroBox + " não encontrado.");
            return false;
        }

        for (Box b : boxes) {
            if (b.getMecanicoResponsavel() != null && b.getMecanicoResponsavel().getCpf().equalsIgnoreCase(cpfMecanico)) {
                if (b.getNumero() == numeroBox) {
                    System.out.println("Aviso: O mecânico já é o responsável por este mesmo Box.");
                    return true;
                } else {
                    System.out.println("Erro: Pela Regra 1, um mecânico só pode ser responsável por um box.");
                    System.out.printf("O mecânico %s já é responsável pelo Box nº %d.\n", mecanico.getNome(), b.getNumero());
                    return false;
                }
            }
        }

        boxDestino.setMecanicoResponsavel(mecanico);
        System.out.printf("Sucesso: Mecânico %s associado ao Box nº %d!\n", mecanico.getNome(), boxDestino.getNumero());
        return true;
    }

    public boolean atribuirOrdemABox(int codigoOrdem, int numeroBox) {
        OrdemServico ordem = buscarOrdemPorCodigo(codigoOrdem);
        if (ordem == null) {
            System.out.println("Erro: Ordem de serviço nº " + codigoOrdem + " não encontrada.");
            return false;
        }

        if (ordem.getStatus() == StatusOrdem.FINALIZADA) {
            System.out.println("Erro: Não é possível atribuir uma ordem já FINALIZADA.");
            return false;
        }

        if (ordem.getStatus() == StatusOrdem.EM_EXECUCAO) {
            System.out.println("Erro: Esta ordem já está em execução no Box nº " +
                    (ordem.getBox() != null ? ordem.getBox().getNumero() : "?") + ".");
            return false;
        }

        Box box = buscarBoxPorNumero(numeroBox);
        if (box == null) {
            System.out.println("Erro: Box nº " + numeroBox + " não encontrado.");
            return false;
        }

        if (ordem.getServico() != null && box.getTipoServicoPermitido() != null) {
            if (!ordem.getServico().getCategoria().equalsIgnoreCase(box.getTipoServicoPermitido().trim())) {
                System.out.printf("Erro de Incompatibilidade: O Box nº %d só aceita serviços do tipo '%s'.\n",
                        box.getNumero(), box.getTipoServicoPermitido());
                System.out.printf("O serviço da ordem requer: '%s'.\n", ordem.getServico().getCategoria());
                return false;
            }
        }
        if (box.isCheio()) {
            System.out.printf("Erro: O Box nº %d atingiu a capacidade máxima de %d veículo(s).\n",
                    box.getNumero(), box.getCapacidadeMaxima());
            return false;
        }

        box.adicionarOrdem(ordem);
        ordem.setBox(box);
        ordem.setStatus(StatusOrdem.EM_EXECUCAO);

        System.out.printf("Sucesso: Ordem nº %d atribuída ao Box nº %d com status 'Em Execução'!\n",
                ordem.getCodigo(), box.getNumero());
        return true;
    }
    public void exibirOrdensDoBox(int numeroBox) {
        Box box = buscarBoxPorNumero(numeroBox);
        if (box == null) {
            System.out.println("Erro: Box nº " + numeroBox + " não encontrado.");
            return;
        }

        List<OrdemServico> ordensBox = box.getOrdensAtribuidas();
        System.out.printf(" Ordens Atribuídas ao Box nº %d (%s) - Mecânico: %s\n",
                box.getNumero(), box.getTipoServicoPermitido(),
                box.getMecanicoResponsavel() != null ? box.getMecanicoResponsavel().getNome() : "Sem mecânico");


        if (ordensBox.isEmpty()) {
            System.out.println("Nenhuma ordem atribuída a este box no momento.");
        } else {
            for (OrdemServico os : ordensBox) {
                System.out.printf(" - OS #%d | Cliente: %s | Veículo: %s (Placa: %s) | Status: %s | Valor: R$ %.2f\n",
                        os.getCodigo(), os.getNomeCliente(), os.getModeloVeiculo(),
                        os.getPlacaVeiculo(), os.getStatus().getDescricao(), os.getValorEstimado());
            }
        }
        System.out.printf(" TOTAL DE ORDENS ATRIBUÍDAS AO BOX %d: %d\n", box.getNumero(), ordensBox.size());
    }


    public void exibirTotalOrdensFinalizadasPorBox() {
        System.out.println(" RELATÓRIO DE ORDENS FINALIZADAS POR CADA BOX");
        if (boxes.isEmpty()) {
            System.out.println("Nenhum box cadastrado no sistema.");
        } else {
            for (Box box : boxes) {
                String mecNome = (box.getMecanicoResponsavel() != null) ? box.getMecanicoResponsavel().getNome() : "Nenhum";
                System.out.printf("Box nº %d | Serviço: %-15s | Mecânico: %-15s | Total Finalizadas: %d\n",
                        box.getNumero(), box.getTipoServicoPermitido(), mecNome, box.getTotalOrdensFinalizadas());
            }
        }
    }

    public void buscarOrdensPorStatus(StatusOrdem status) {
        System.out.printf(" BUSCA DE ORDENS DE SERVIÇO POR STATUS: %s\n", status.getDescricao().toUpperCase());
        int encontradas = 0;
        for (OrdemServico os : ordens) {
            if (os.getStatus() == status) {
                encontradas++;
                System.out.println(os.getDetalhesFormatados());
                System.out.println();
            }
        }

        if (encontradas == 0) {
            System.out.printf("Nenhuma ordem encontrada com status '%s'.\n", status.getDescricao());
        } else {
            System.out.printf("Total de ordens encontradas com status '%s': %d\n", status.getDescricao(), encontradas);
        }
    }

    public void exibirDetalhesOrdem(int codigoOrdem) {
        OrdemServico os = buscarOrdemPorCodigo(codigoOrdem);
        if (os == null) {
            System.out.println("Erro: Ordem de serviço nº " + codigoOrdem + " não encontrada.");
            return;
        }
        System.out.println();
        System.out.println(os.getDetalhesFormatados());
        System.out.println();
    }

    
    public boolean finalizarOrdem(int codigoOrdem) {
        OrdemServico os = buscarOrdemPorCodigo(codigoOrdem);
        if (os == null) {
            System.out.println("Erro: Ordem de serviço nº " + codigoOrdem + " não encontrada.");
            return false;
        }

        if (os.getStatus() == StatusOrdem.FINALIZADA) {
            System.out.println("Aviso: Esta ordem de serviço já se encontra finalizada.");
            return false;
        }

        Box boxAtual = os.getBox();
        if (boxAtual != null) {
            boxAtual.removerOrdem(os);
            boxAtual.incrementarTotalFinalizadas();
            
        }

        os.setStatus(StatusOrdem.FINALIZADA);
        System.out.printf("Sucesso: Ordem nº %d finalizada com sucesso", os.getCodigo());
        return true;
    }
}