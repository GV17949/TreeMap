import java.util.*;

/**
 * Executor de Workloads e armazenamento de resultados
 * Executa as operações e coleta métricas de performance
 */
public class WorkloadExecutor {
    
    // Classe para armazenar resultados de execução
    public static class ExecutionResult {
        private final String implementation;
        private final List<Long> operationTimes; // nanosegundos
        private final long totalTime;
        
        public ExecutionResult(String implementation, List<Long> operationTimes, long totalTime) {
            this.implementation = implementation;
            this.operationTimes = new ArrayList<>(operationTimes);
            this.totalTime = totalTime;
        }
        
        public String getImplementation() { return implementation; }
        public List<Long> getOperationTimes() { return operationTimes; }
        public long getTotalTime() { return totalTime; }
        
        public double getAverageTime() {
            return operationTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        }
        
        public double getMedian() {
            List<Long> sorted = new ArrayList<>(operationTimes);
            Collections.sort(sorted);
            int size = sorted.size();
            if (size % 2 == 0) {
                return (sorted.get(size/2 - 1) + sorted.get(size/2)) / 2.0;
            } else {
                return sorted.get(size/2);
            }
        }
        
        public double getStandardDeviation() {
            double mean = getAverageTime();
            double sumSquaredDiffs = operationTimes.stream()
                .mapToDouble(time -> Math.pow(time - mean, 2))
                .sum();
            return Math.sqrt(sumSquaredDiffs / operationTimes.size());
        }
    }
    
    /**
     * Executa um workload em uma implementação específica de TreeMap
     */
    public static ExecutionResult executeWorkload(WorkloadGenerator.Workload workload, 
                                                TreeMapImplementation<Integer, String> implementation) {
        List<Long> operationTimes = new ArrayList<>();
        implementation.clear();
        
        // Aquecimento da JVM
        warmupJVM(implementation);
        
        long startTime = System.nanoTime();
        
        for (WorkloadGenerator.Operation op : workload.getOperations()) {
            long opStart = System.nanoTime();
            
            switch (op.getType()) {
                case INSERT:
                    implementation.put(op.getKey(), op.getValue());
                    break;
                case DELETE:
                    implementation.remove(op.getKey());
                    break;
                case QUERY:
                    implementation.get(op.getKey());
                    break;
            }
            
            long opEnd = System.nanoTime();
            operationTimes.add(opEnd - opStart);
        }
        
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        
        return new ExecutionResult(implementation.getName(), operationTimes, totalTime);
    }
    
    /**
     * Aquecimento da JVM para medições mais precisas
     */
    private static void warmupJVM(TreeMapImplementation<Integer, String> implementation) {
        for (int i = 0; i < 1000; i++) {
            implementation.put(i, "warmup_" + i);
            implementation.get(i);
            implementation.remove(i);
        }
        implementation.clear();
        
        // Força garbage collection
        System.gc();
        
        // Pequena pausa para estabilizar
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Executa múltiplas rodadas do mesmo workload para obter médias mais confiáveis
     */
    public static List<ExecutionResult> executeMultipleRuns(WorkloadGenerator.Workload workload,
                                                           TreeMapImplementation<Integer, String> implementation,
                                                           int numberOfRuns) {
        List<ExecutionResult> results = new ArrayList<>();
        
        for (int run = 0; run < numberOfRuns; run++) {
            System.out.printf("    Executando rodada %d/%d para %s%n", 
                run + 1, numberOfRuns, implementation.getName());
            
            ExecutionResult result = executeWorkload(workload, implementation);
            results.add(result);
            
            // Pequena pausa entre rodadas
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return results;
    }
    
    /**
     * Calcula estatísticas agregadas de múltiplas execuções
     */
    public static ExecutionResult aggregateResults(List<ExecutionResult> results) {
        if (results.isEmpty()) {
            throw new IllegalArgumentException("Lista de resultados não pode estar vazia");
        }
        
        String implementation = results.get(0).getImplementation();
        List<Long> allOperationTimes = new ArrayList<>();
        long totalTime = 0;
        
        for (ExecutionResult result : results) {
            allOperationTimes.addAll(result.getOperationTimes());
            totalTime += result.getTotalTime();
        }
        
        return new ExecutionResult(implementation + " (agregado)", allOperationTimes, totalTime / results.size());
    }
}
