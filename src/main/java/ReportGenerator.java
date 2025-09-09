import java.io.*;
import java.util.*;

/**
 * Gerador de Relatórios para os resultados dos workloads
 * Cria relatórios detalhados e logs de replicação
 */
public class ReportGenerator {
    
    /**
     * Gera relatório completo com estatísticas e logs
     */
    public static void generateReport(List<WorkloadExecutor.ExecutionResult> results, 
                                    WorkloadGenerator.Workload workload, 
                                    String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        System.out.println("Gerando relatórios em: " + new File(outputDir).getAbsolutePath());
        
        generateMainReport(results, workload, outputDir);
        
        generateOperationLog(workload, outputDir);
        
        // Gerar logs detalhados dos tempos
        generateDetailedTimeLogs(results, workload, outputDir);
        
        // Gerar relatório comparativo
        generateComparativeReport(results, workload, outputDir);
        
        System.out.println("Todos os relatórios foram gerados com sucesso!");
    }
    
    /**
     * Gera o relatório principal com estatísticas
     */
    private static void generateMainReport(List<WorkloadExecutor.ExecutionResult> results, 
                                         WorkloadGenerator.Workload workload, 
                                         String outputDir) throws IOException {
        String reportFileName = String.format("%s/workload_report_%s_%d_ops.txt", 
            outputDir, workload.getType().name().toLowerCase(), workload.getSize());
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFileName))) {
            writer.println("=== RELATÓRIO DE WORKLOAD ===");
            writer.println("Tipo: " + workload.getType());
            writer.println("Número de operações: " + workload.getSize());
            writer.println("Data/Hora: " + new Date());
            writer.println("Distribuição das operações:");
            writer.printf("  - INSERT: %.1f%%%n", workload.getType().getInsertRatio() * 100);
            writer.printf("  - DELETE: %.1f%%%n", workload.getType().getDeleteRatio() * 100);
            writer.printf("  - QUERY:  %.1f%%%n", workload.getType().getQueryRatio() * 100);
            writer.println();
            writer.println("=".repeat(60));
            writer.println();
            
            for (WorkloadExecutor.ExecutionResult result : results) {
                writer.println("--- " + result.getImplementation() + " ---");
                writer.printf("Tempo total do workload: %.2f ms%n", result.getTotalTime() / 1_000_000.0);
                writer.printf("Tempo médio por operação: %.2f ns%n", result.getAverageTime());
                writer.printf("Mediana das operações: %.2f ns%n", result.getMedian());
                writer.printf("Desvio padrão: %.2f ns%n", result.getStandardDeviation());
                writer.printf("Throughput: %.2f ops/ms%n", 
                    (double) workload.getSize() / (result.getTotalTime() / 1_000_000.0));
                writer.println();
            }
        }
    }
    
    /**
     * Gera log detalhado das operações para replicação
     */
    private static void generateOperationLog(WorkloadGenerator.Workload workload, 
                                           String outputDir) throws IOException {
        String logFileName = String.format("%s/workload_log_%s_%d_ops.csv", 
            outputDir, workload.getType().name().toLowerCase(), workload.getSize());
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName))) {
            writer.println("operation_index,operation_type,key,value");
            List<WorkloadGenerator.Operation> operations = workload.getOperations();
            for (int i = 0; i < operations.size(); i++) {
                WorkloadGenerator.Operation op = operations.get(i);
                writer.printf("%d,%s,%d,%s%n", 
                    i, op.getType(), op.getKey(), 
                    op.getValue() != null ? op.getValue() : "");
            }
        }
    }
    
    /**
     * Gera logs detalhados dos tempos por implementação
     */
    private static void generateDetailedTimeLogs(List<WorkloadExecutor.ExecutionResult> results,
                                               WorkloadGenerator.Workload workload,
                                               String outputDir) throws IOException {
        for (WorkloadExecutor.ExecutionResult result : results) {
            String timesFileName = String.format("%s/times_%s_%s_%d_ops.csv", 
                outputDir, 
                sanitizeFileName(result.getImplementation()), 
                workload.getType().name().toLowerCase(), 
                workload.getSize());
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(timesFileName))) {
                writer.println("operation_index,time_ns,time_ms");
                List<Long> times = result.getOperationTimes();
                for (int i = 0; i < times.size(); i++) {
                    long timeNs = times.get(i);
                    writer.printf("%d,%d,%.6f%n", i, timeNs, timeNs / 1_000_000.0);
                }
            }
        }
    }
    
    /**
     * Gera relatório comparativo entre implementações
     */
    private static void generateComparativeReport(List<WorkloadExecutor.ExecutionResult> results,
                                                WorkloadGenerator.Workload workload,
                                                String outputDir) throws IOException {
        if (results.size() < 2) return;
        
        String compareFileName = String.format("%s/comparative_report_%s_%d_ops.txt", 
            outputDir, workload.getType().name().toLowerCase(), workload.getSize());
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(compareFileName))) {
            writer.println("=== RELATÓRIO COMPARATIVO ===");
            writer.println("Workload: " + workload.getType() + " (" + workload.getSize() + " ops)");
            writer.println("Data/Hora: " + new Date());
            writer.println();
            
            // Encontrar a melhor implementação para cada métrica
            WorkloadExecutor.ExecutionResult fastestTotal = results.get(0);
            WorkloadExecutor.ExecutionResult fastestAverage = results.get(0);
            WorkloadExecutor.ExecutionResult mostConsistent = results.get(0);
            
            for (WorkloadExecutor.ExecutionResult result : results) {
                if (result.getTotalTime() < fastestTotal.getTotalTime()) {
                    fastestTotal = result;
                }
                if (result.getAverageTime() < fastestAverage.getAverageTime()) {
                    fastestAverage = result;
                }
                if (result.getStandardDeviation() < mostConsistent.getStandardDeviation()) {
                    mostConsistent = result;
                }
            }
            
            writer.println("MELHORES RESULTADOS:");
            writer.println("Menor tempo total: " + fastestTotal.getImplementation() + 
                " (" + String.format("%.2f ms", fastestTotal.getTotalTime() / 1_000_000.0) + ")");
            writer.println("Menor tempo médio: " + fastestAverage.getImplementation() + 
                " (" + String.format("%.2f ns", fastestAverage.getAverageTime()) + ")");
            writer.println("Mais consistente: " + mostConsistent.getImplementation() + 
                " (σ = " + String.format("%.2f ns", mostConsistent.getStandardDeviation()) + ")");
            writer.println();
            
            // Tabela comparativa
            writer.println("TABELA COMPARATIVA:");
            writer.printf("%-30s %15s %15s %15s %15s%n", 
                "Implementação", "Tempo Total", "Tempo Médio", "Mediana", "Desvio Padrão");
            writer.println("-".repeat(95));
            
            for (WorkloadExecutor.ExecutionResult result : results) {
                writer.printf("%-30s %12.2f ms %12.2f ns %12.2f ns %12.2f ns%n",
                    result.getImplementation(),
                    result.getTotalTime() / 1_000_000.0,
                    result.getAverageTime(),
                    result.getMedian(),
                    result.getStandardDeviation());
            }
            
            writer.println();
            
            // Speedup analysis
            if (results.size() >= 2) {
                writer.println("ANÁLISE DE SPEEDUP (relativo à implementação mais lenta):");
                WorkloadExecutor.ExecutionResult slowest = results.stream()
                    .max(Comparator.comparing(WorkloadExecutor.ExecutionResult::getTotalTime))
                    .orElse(results.get(0));
                
                for (WorkloadExecutor.ExecutionResult result : results) {
                    double speedup = (double) slowest.getTotalTime() / result.getTotalTime();
                    writer.printf("%-30s: %.2fx mais rápido%n", result.getImplementation(), speedup);
                }
            }
        }
    }
    
    /**
     * Sanitiza nome de arquivo removendo caracteres especiais
     */
    private static String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9]", "_");
    }
    
    /**
     * Gera um resumo rápido no console
     */
    public static void printSummaryToConsole(List<WorkloadExecutor.ExecutionResult> results) {
        System.out.println("\n=== RESUMO DOS RESULTADOS ===");
        for (WorkloadExecutor.ExecutionResult result : results) {
            System.out.printf("%-30s: %8.2f ms total, %8.2f ns/op médio%n",
                result.getImplementation(),
                result.getTotalTime() / 1_000_000.0,
                result.getAverageTime());
        }
        System.out.println();
    }
}
