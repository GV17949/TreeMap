import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Gerador de Workloads para TreeMap
 * Cria sequências de operações para testar diferentes implementações
 */
public class WorkloadGenerator {
    
    // Enum para tipos de workload
    public enum WorkloadType {
        INSERT_DELETE_HEAVY(0.4, 0.4, 0.2), // 80% insert/delete, 20% consultas
        QUERY_HEAVY(0.1, 0.1, 0.8);         // 20% insert/delete, 80% consultas
        
        private final double insertRatio;
        private final double deleteRatio;
        private final double queryRatio;
        
        WorkloadType(double insertRatio, double deleteRatio, double queryRatio) {
            this.insertRatio = insertRatio;
            this.deleteRatio = deleteRatio;
            this.queryRatio = queryRatio;
        }
        
        public double getInsertRatio() { return insertRatio; }
        public double getDeleteRatio() { return deleteRatio; }
        public double getQueryRatio() { return queryRatio; }
    }
    
    public enum OperationType {
        INSERT, DELETE, QUERY
    }
    
    public static class Operation {
        private final OperationType type;
        private final Integer key;
        private final String value;
        
        public Operation(OperationType type, Integer key, String value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }
        
        public OperationType getType() { return type; }
        public Integer getKey() { return key; }
        public String getValue() { return value; }
        
        @Override
        public String toString() {
            return String.format("%s,%d,%s", type, key, value != null ? value : "");
        }
    }
    
    // Classe principal do workload
    public static class Workload {
        private final List<Operation> operations;
        private final WorkloadType type;
        private final int size;
        
        public Workload(WorkloadType type, int size) {
            this.type = type;
            this.size = size;
            this.operations = generateOperations();
        }
        
        private List<Operation> generateOperations() {
            List<Operation> ops = new ArrayList<>(size);
            Random random = ThreadLocalRandom.current();
            Set<Integer> existingKeys = new HashSet<>();
            
            for (int i = 0; i < size; i++) {
                double rand = random.nextDouble();
                OperationType opType;
                
                if (rand < type.getInsertRatio()) {
                    opType = OperationType.INSERT;
                } else if (rand < type.getInsertRatio() + type.getDeleteRatio()) {
                    opType = OperationType.DELETE;
                } else {
                    opType = OperationType.QUERY;
                }
                
                Integer key;
                String value = null;
                
                switch (opType) {
                    case INSERT:
                        key = random.nextInt(size * 2); // Range maior para evitar muitas colisões
                        value = "value_" + key;
                        existingKeys.add(key);
                        break;
                        
                    case DELETE:
                        if (!existingKeys.isEmpty() && random.nextDouble() < 0.7) {
                            // 70% chance de deletar chave existente
                            List<Integer> keyList = new ArrayList<>(existingKeys);
                            key = keyList.get(random.nextInt(keyList.size()));
                            existingKeys.remove(key);
                        } else {
                            key = random.nextInt(size * 2);
                        }
                        break;
                        
                    case QUERY:
                        if (!existingKeys.isEmpty() && random.nextDouble() < 0.6) {
                            // 60% chance de consultar chave existente
                            List<Integer> keyList = new ArrayList<>(existingKeys);
                            key = keyList.get(random.nextInt(keyList.size()));
                        } else {
                            key = random.nextInt(size * 2);
                        }
                        break;
                        
                    default:
                        throw new IllegalStateException("Tipo de operação desconhecido: " + opType);
                }
                
                ops.add(new Operation(opType, key, value));
            }
            
            return ops;
        }
        
        public List<Operation> getOperations() { return operations; }
        public WorkloadType getType() { return type; }
        public int getSize() { return size; }
    }
}
