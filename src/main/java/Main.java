import java.io.IOException;
import java.util.*;

/**
 * Classe principal para executar os experimentos de workload
 * Coordena a geração de workloads, execução e geração de relatórios
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== GERADOR DE WORKLOAD PARA TREEMAP ===");
        System.out.println("Iniciando experimentos...\n");
        
        try {
            ExperimentConfig config = parseArguments(args);
            
            runExperiments(config);
            
            System.out.println("=== EXPERIMENTOS CONCLUÍDOS COM SUCESSO ===");
            
        } catch (Exception e) {
            System.err.println("Erro durante a execução dos experimentos: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Configurações do experimento
     */
    static class ExperimentConfig {
        int[] workloadSizes = {100, 10_000, 100_000};
        WorkloadGenerator.WorkloadType[] workloadTypes = {
            WorkloadGenerator.WorkloadType.INSERT_DELETE_HEAVY,
            WorkloadGenerator.WorkloadType.QUERY_HEAVY
        };
        String outputDir = "workload_results";
        int numberOfRuns = 3; // Número de execuções para cada teste
        boolean verbose = true;
    }
    
    /**
     * Parse dos argumentos da linha de comando (opcional)
     */
    private static ExperimentConfig parseArguments(String[] args) {
        ExperimentConfig config = new ExperimentConfig();
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--output-dir":
                    if (i + 1 < args.length) {
                        config.outputDir = args[++i];
                    }
                    break;
                case "--runs":
                    if (i + 1 < args.length) {
                        try {
                            config.numberOfRuns = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("Número de execuções inválido: " + args[i]);
                        }
                    }
                    break;
                case "--quiet":
                    config.verbose = false;
                    break;
                case "--help":
                    printHelp();
                    System.exit(0);
                    break;
            }
        }
        
        return config;
    }
    
    /**
     * Imprime ajuda sobre o uso do programa
     */
    private static void printHelp() {
        System.out.println("Uso: gradle runWorkload [opções]");
        System.out.println("Opções:");
        System.out.println("  --output-dir <dir>  Diretório de saída (padrão: workload_results)");
        System.out.println("  --runs <n>          Número de execuções por teste (padrão: 3)");
        System.out.println("  --quiet             Modo silencioso");
        System.out.println("  --help              Mostra esta ajuda");
    }
    
    /**
     * Executa todos os experimentos configurados
     */
    private static void runExperiments(ExperimentConfig config) throws IOException {
        // Criar implementações disponíveis
        List<TreeMapImplementation<Integer, String>> implementations = createImplementations();
        
        if (implementations.isEmpty()) {
            System.err.println("ERRO: Nenhuma implementação disponível!");
            System.err.println("Por favor, implemente as classes PVTreeMapWrapper e/ou AVLTreeMapWrapper");
            return;
        }
        
        // Executar experimentos para cada combinação
        for (WorkloadGenerator.WorkloadType type : config.workloadTypes) {
            for (int size : config.workloadSizes) {
                if (config.verbose) {
                    System.out.printf("=== Executando workload: %s com %d operações ===%n", 
                        type, size);
                }
                
                // Gerar workload
                WorkloadGenerator.Workload workload = new WorkloadGenerator.Workload(type, size);
                
                // Executar para cada implementação
                List<WorkloadExecutor.ExecutionResult> results = new ArrayList<>();
                for (TreeMapImplementation<Integer, String> impl : implementations) {
                    if (config.verbose) {
                        System.out.println("Testando: " + impl.getName());
                    }
                    
                    try {
                        if (config.numberOfRuns > 1) {
                            // Múltiplas execuções para maior confiabilidade
                            List<WorkloadExecutor.ExecutionResult> multipleResults = 
                                WorkloadExecutor.executeMultipleRuns(workload, impl, config.numberOfRuns);
                            
                            // Usar resultado agregado
                            WorkloadExecutor.ExecutionResult aggregated = 
                                WorkloadExecutor.aggregateResults(multipleResults);
                            results.add(aggregated);
                        } else {
                            // Execução única
                            WorkloadExecutor.ExecutionResult result = 
                                WorkloadExecutor.executeWorkload(workload, impl);
                            results.add(result);
                        }
                        
                    } catch (UnsupportedOperationException e) {
                        System.err.printf("  AVISO: %s não implementado - %s%n", 
                            impl.getName(), e.getMessage());
                        continue;
                    } catch (Exception e) {
                        System.err.printf("  ERRO ao testar %s: %s%n", 
                            impl.getName(), e.getMessage());
                        continue;
                    }
                }
                
                if (!results.isEmpty()) {
                    // Gerar relatório
                    ReportGenerator.generateReport(results, workload, config.outputDir);
                    
                    // Imprimir resumo no console
                    if (config.verbose) {
                        ReportGenerator.printSummaryToConsole(results);
                    }
                } else {
                    System.err.println("  Nenhum resultado válido para este workload.");
                }
                
                System.out.println();
            }
        }
    }
    
    /**
     * Cria as implementações disponíveis para teste
     * NOTA: Adapte este método conforme suas implementações
     */
    private static List<TreeMapImplementation<Integer, String>> createImplementations() {
        List<TreeMapImplementation<Integer, String>> implementations = new ArrayList<>();
        
        // Sempre adicionar TreeMap padrão do Java
        implementations.add(new StandardTreeMap<>());
        
        // Tentar adicionar suas implementações personalizadas
        try {
            // Descomente e adapte quando suas classes estiverem prontas
            // implementations.add(new PVTreeMapWrapper<>());
            // implementations.add(new AVLTreeMapWrapper<>());
            
            System.out.println("NOTA: Para comparar com suas implementações, ");
            System.out.println("      descomente e adapte as linhas em createImplementations()");
            System.out.println("      no arquivo Main.java");
            
        } catch (Exception e) {
            System.err.println("Erro ao carregar implementações personalizadas: " + e.getMessage());
        }
        
        return implementations;
    }
    
    /**
     * Método de conveniência para execução rápida com configurações padrão
     */
    public static void runQuickTest() {
        System.out.println("=== TESTE RÁPIDO ===");
        
        try {
            // Configuração simplificada para teste rápido
            WorkloadGenerator.Workload smallWorkload = new WorkloadGenerator.Workload(
                WorkloadGenerator.WorkloadType.INSERT_DELETE_HEAVY, 1000);
            
            TreeMapImplementation<Integer, String> standardImpl = new StandardTreeMap<>();
            
            System.out.println("Executando teste rápido com TreeMap padrão...");
            WorkloadExecutor.ExecutionResult result = 
                WorkloadExecutor.executeWorkload(smallWorkload, standardImpl);
            
            System.out.printf("Resultado: %.2f ms total, %.2f ns/operação%n",
                result.getTotalTime() / 1_000_000.0, result.getAverageTime());
                
        } catch (Exception e) {
            System.err.println("Erro no teste rápido: " + e.getMessage());
        }
    }
}
