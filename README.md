# TreeMap
Estudo prático sobre a implementação e desempenho de um TreeMap

## Link para nossa documentação
### [Projeto de Estrutura de Dados](https://docs.google.com/document/d/1unQ7hZ-Pi80GWnIRjvouqGUBOaaXNFiQ5919jvKOx0o/edit?usp=sharing)

## Como rodar o projeto:

### Execução default:
```
gradle runWorkload
```

### Execução com nível de rodadas específico (default são 3 rodadas):
```
gradle runWorkload --args='--runs=3'
```

### Plotagem do gráfico dos reports gerados pelo runWorkload:
```
cd workload_results
R -f plota_workload.r
```
