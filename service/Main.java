import model.Box;
import model.Mecanico;
import model.OrdemServico;
import model.Servico;
import model.StatusOrdem;
import service.OficinaService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        OficinaService service = new OficinaService();

        inicializarMecanicosEBoxes(service);

        Scanner scanner = new Scanner(System.in);
        boolean executando = true;
        System.out.println(" Oficina do Glender");
    

        while (executando) {
            exibirMenu();
            System.out.print("Escolha uma opcao");
            String opcaoStr = scanner.nextLine().trim();

            switch (opcaoStr) {
                case "1":
                    cadastrarOrdemServico(service, scanner);
                    break;
                case "2":
                    associarMecanicoABox(service, scanner);
                    break;
                case "3":
                    atribuirOrdemABox(service, scanner);
                    break;
                case "4":
                    exibirOrdensBox(service, scanner);
                    break;
                case "5":
                    service.exibirTotalOrdensFinalizadasPorBox();
                    break;
                case "6":
                    buscarOrdensPorStatus(service, scanner);
                    break;
                case "7":
                    exibirDetalhesOrdem(service, scanner);
                    break;
                case "8":
                    finalizarOrdem(service, scanner);
                    break;
                case "9":
                    listarBoxesEMecanicos(service);
                    break;
                case "0":
                    System.out.println("\nEncerrando o sistema");
                    executando = false;
                    break;
                default:
                    System.out.println("\nOpção invlida\n");
                    break;
            }
        }

        scanner.close();
    }


    private static void exibirMenu() {
        System.out.println("1. Cadastrar ordem de serviço");
        System.out.println("2. Associar um mecânico a um box");
        System.out.println("3. Atribuir ordem de serviço a um box");
        System.out.println("4. Exibir ordens atribuídas a um box específico (com total)");
        System.out.println("5. Informar total de ordens finalizadas por cada box");
        System.out.println("6. Buscar ordens por status");
        System.out.println("7. Exibir detalhes completos de uma ordem específica");
        System.out.println("8. Finalizar ordem de serviço (libera box e conta finalizada)");
        System.out.println("9. Listar boxes e mecânicos cadastrados");
        System.out.println("0. Sair do sistema");

    }

    
    public static void inicializarMecanicosEBoxes(OficinaService service) {
        Mecanico m1 = new Mecanico("Carlos Silva", "111.222.333-44", "Mecânica Geral", "(11) 98888-1111");
        Mecanico m2 = new Mecanico("Marcos Souza", "222.333.444-55", "Elétrica", "(11) 97777-2222");
        Mecanico m3 = new Mecanico("Rafael Lima", "333.444.555-66", "Suspensão", "(11) 96666-3333");

        service.adicionarMecanico(m1);
        service.adicionarMecanico(m2);
        service.adicionarMecanico(m3);

        Box b1 = new Box(1, "Mecânica Geral", 2, "Galpão A - Entrada", m1);
        Box b2 = new Box(2, "Elétrica", 1, "Galpão B - Lateral", m2);
        Box b3 = new Box(3, "Suspensão", 3, "Galpão C - Fundos", m3);

        service.adicionarBox(b1);
        service.adicionarBox(b2);
        service.adicionarBox(b3);

        System.out.println("[Sistema] 3 Mecânicos e 3 Boxes ");
    }

    private static void cadastrarOrdemServico(OficinaService service, Scanner scanner) {
        System.out.println("\n--- Cadastrar Nova Ordem de Serviço ---");
        System.out.print("Nome do Cliente: ");
        String cliente = scanner.nextLine().trim();

        System.out.print("Modelo do Veículo: ");
        String modelo = scanner.nextLine().trim();

        System.out.print("Placa do Veiulo: ");
        String placa = scanner.nextLine().trim();

        System.out.print("Data da Ordem (ex: 01/10/2026): ");
        String data = scanner.nextLine().trim();

        System.out.print("Nome do Serviço: ");
        String nomeServico = scanner.nextLine().trim();

        System.out.println("Categoria do Serviço (deve coincidir com o tipo do box):");
        System.out.println("  Opções sugeridas: [Mecânica Geral], [Elétrica], [Suspensão]");
        System.out.print("Informe a categoria: ");
        String categoria = scanner.nextLine().trim();

        int tempo = lerInteiro(scanner, "Tempo estimado (em minutos): ");
        double valor = lerDouble(scanner, "Valor estimado do serviço (R$): ");

        Servico servico = new Servico(nomeServico, tempo, valor, categoria);
        OrdemServico ordem = service.cadastrarOrdemServico(cliente, modelo, placa, data, valor, servico);

        System.out.println("\nOrdem de serviço cadastrada com sucesso!");
        System.out.printf("Código gerado: %d | Status inicial: %s (Sem box atribuído)\n",
                ordem.getCodigo(), ordem.getStatus().getDescricao());
    }

    private static void associarMecanicoABox(OficinaService service, Scanner scanner) {
        System.out.println("\n--- Associar Mecânico a um Box ---");
        System.out.println("Mecânicos cadastrados:");
        for (Mecanico m : service.getMecanicos()) {
            System.out.printf(" - CPF: %s | Nome: %s | Especialidade: %s\n", m.getCpf(), m.getNome(), m.getEspecialidade());
        }

        System.out.print("Digite o CPF do mecânico: ");
        String cpf = scanner.nextLine().trim();

        System.out.println("Boxes existentes:");
        for (Box b : service.getBoxes()) {
            String mec = (b.getMecanicoResponsavel() != null) ? b.getMecanicoResponsavel().getNome() : "Nenhum";
            System.out.printf(" - Box nº %d (%s) | Mecânico atual: %s\n", b.getNumero(), b.getTipoServicoPermitido(), mec);
        }

        int numeroBox = lerInteiro(scanner, "Digite o número do Box: ");
        service.associarMecanicoABox(cpf, numeroBox);
    }

    private static void atribuirOrdemABox(OficinaService service, Scanner scanner) {
        System.out.println("\n--- Atribuir Ordem de Serviço a um Box ---");
        int codigoOrdem = lerInteiro(scanner, "Código da Ordem de Serviço: ");
        int numeroBox = lerInteiro(scanner, "Número do Box de destino: ");

        service.atribuirOrdemABox(codigoOrdem, numeroBox);
    }

    private static void exibirOrdensBox(OficinaService service, Scanner scanner) {
        System.out.println(" Exibir Ordens de um Box");
        int numeroBox = lerInteiro(scanner, "Informe o número do Box: ");
        service.exibirOrdensDoBox(numeroBox);
    }

    private static void buscarOrdensPorStatus(OficinaService service, Scanner scanner) {
        System.out.println(" Buscar Ordens por Status-");
        System.out.println("1. Aberta");
        System.out.println("2. Em Execução");
        System.out.println("3. Finalizada");
        int opcao = lerInteiro(scanner, "Escolha o status desejado (1-3): ");

        StatusOrdem status;
        switch (opcao) {
            case 1:
                status = StatusOrdem.ABERTA;
                break;
            case 2:
                status = StatusOrdem.EM_EXECUCAO;
                break;
            case 3:
                status = StatusOrdem.FINALIZADA;
                break;
            default:
                System.out.println("Opção de status inválida.");
                return;
        }

        service.buscarOrdensPorStatus(status);
    }

    private static void exibirDetalhesOrdem(OficinaService service, Scanner scanner) {
        System.out.println("\n--- Detalhes da Ordem de Serviço ---");
        int codigoOrdem = lerInteiro(scanner, "Informe o código da Ordem: ");
        service.exibirDetalhesOrdem(codigoOrdem);
    }

    private static void finalizarOrdem(OficinaService service, Scanner scanner) {
        System.out.println("\n--- Finalizar Ordem de Serviço ---");
        int codigoOrdem = lerInteiro(scanner, "Informe o código da Ordem a ser finalizada: ");
        service.finalizarOrdem(codigoOrdem);
    }

    private static void listarBoxesEMecanicos(OficinaService service) {
        System.out.println(" BOXES CADASTRADOS");
        for (Box b : service.getBoxes()) {
            System.out.println(b);
        }
        System.out.println("MECÂNICOS CADASTRADOS");
        for (Mecanico m : service.getMecanicos()) {
            System.out.println(m);
        }
    }

    private static int lerInteiro(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! ");
            }
        }
    }

    private static double lerDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido!");
            }
        }
    }
}