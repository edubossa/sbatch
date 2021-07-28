# Batch para integração com o Pub/Sub

Esse projeto foi gerado utilizado o modelo de Batch Atômico.

`Batch para processamento de uma base de dados e exibição das informações.`

# Desenvolvimento

O projeto Batch segue alguns padrões bem definidos:

- Nomenclatura de classes e pacotes;
- Estrutura de pastas;
- Divisão de capacidades entre módulos;
- Documentação de serviços via Swagger;
- Logging;
- Monitoramento;
- Entre outros.
  
Para maiores detalhes da arquitetura atômica utilizada, assim como outros facilitadores disponíveis, [clique aqui](#documentação).

# Execução

Abaixo estão algumas seções que descrevem as tecnologias utilizadas (assim como suas versões), o processo de instalação as dependências e o método de execução do projeto.

## Tecnologias utilizadas

O projeto utiliza as seguintes tecnologias:

1) Java 11
2) Maven 3.5.3
3) SprintBoot 2+
4) JUnit Jupiter 5+
5) Lombok 1.18.20
6) Spring Cloud GCP

## Instalando dependências e compilando

Para instalar as dependências e compilar o seu projeto, execute o seguinte comando no bash.

```
$ mvn clean install
``` 

Certifique-se de o repositório do Maven está corretamente configurado.

## Executando o projeto

### STEP 01

Antes de executar o projeto, será necessario exportar a variavel de ambiente com a service account, a qual contem as 
permições necessárias para acesso ao serviço do Google Cloud Pub/Sub. 
A mesma pode ser referenciada tanto na sua IDE de preferência ou no terminal como segue abaixo.

```
export GOOGLE_APPLICATION_CREDENTIALS=/myfolder/app/service-account.json
```

### STEP 02
Criar um Tópico e um Subscription ou se certificar que ja estejam criados no Google Cloud Pub/Sub.
Feito isso deve preencher as variaveis abaixo no application.yml.

```yaml
job:
  topic: ${TOPIC:batch-xml-process}
  subscription: ${SUBSCRIPTION:bv-batch-subscription}
```

### STEP 03

O projeto pode ser executado de duas formas após a sua compilação:
1) via IntelliJ, via classe `SpringBatchApplication.java`
2) via java, utilizando o comando:

```
// na pasta `target`
$ java -jar sbatch-arqt-refe-atom-pubsub-1.0.0.jar
```

### Etapas da Execução

O projeto executará as seguintes etapas:

- Reader: Leitura das mensagens contidas no serviço do Google Pub/Sub
- Processor: Recebe os dados lidos do Pub/Sub convertendo-os em maiúsculo (UpperCase).
- Writer: Após o processamento, enviará os dados para outro Topico. 

### Parâmetro de Entrada

Não é necessário informar um parâmetro de entrada para execução do projeto.

Caso seja utilizado um banco de dados com persistência será necessário informar um parâmetro para execução e reexecução. 

### Banco de Dados em local

Utilização de banco de dados postgree com nome da base de dados `batch`.

As configurações do banco de dados são definidas através da configuração abaixo:

```yaml
spring:
  datasource:
      initialization-mode: always
      url: ${DB_URL:jdbc:postgresql://localhost:5432/batch}
      username: ${DB_USERNAME:postgres}
      password: ${DB_PASSWORD:postgres}
      hikari:
        connection-timeout: ${CONNECTION_TIMEOUT:90000}
        leak-detection-threshold: 90000
        max-lifetime: ${MAX_LIFETIME:1800000}
        minimum-idle: ${MINIMUM_IDLE:20}
        maximum-pool-size: ${MAXIMUM_POOL_SIZE:100}
        idle-timeout: ${IDLE_TIMEOUT:600000}
```

As tabelas de controle do SpringBatch são criadas automaticamente através do parâmetro definido abaixo:

```yaml
spring:
  application:
    batch:
      initialize-schema: always
```

### Banco de dados MySQL na Nuven

Caso venha a utilizar o banco de Dados na Nuven seguir os seguintes passos: 

#### STEP 01

Ativer o profile gcp, na execução da sua aplicação

```
-Dspring.profiles.active=gcp
```

#### STEP 02

As configurações do banco de dados são definidas através da configuração abaixo:

```yaml
spring:
  cloud:
    gcp:
      sql:
        database-name: ${DATABASE_NAME}
        instance-connection-name: ${INSTANCE_NAME}
  datasource:
    initialization-mode: always
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      connection-timeout: ${CONNECTION_TIMEOUT:90000}
      leak-detection-threshold: 90000
      max-lifetime: ${MAX_LIFETIME:1800000}
      minimum-idle: ${MINIMUM_IDLE:20}
      maximum-pool-size: ${MAXIMUM_POOL_SIZE:100}
      idle-timeout: ${IDLE_TIMEOUT:600000}  
```

## Configurações adicionais:

É possivel customizar o pull de threads e outras configurações adicionais, tanto no envio quanto no consumo do Pub/Sub.

As configurações do Pub/Sub são definidas através da configuração abaixo:

```yaml
spring:  
  cloud:
    gcp:
      pubsub:
        publisher:
          executor-threads: ${PUBLISHER_THREADS:20}
          retry:
            max-attempts: ${MAX_ATTEMPTS:5}
            initial-retry-delay-seconds: ${INITIAL_RETRY_DELAY_SECONDS:10}
            max-retry-delay-seconds: ${MAX_RETRY_DELAY_SECONDS:30}
            initial-rpc-timeout-seconds: ${INITIAL_RPC_TIMEOUT_SECONDS:10}
            max-rpc-timeout-seconds: ${MAX_RPC_TIMEOUT_SECONDS:30}
            total-timeout-seconds: ${TOTAL_TIMEOUT_SECONDS:60}
        subscriber:
          executor-threads: ${SUBSCRIBER_THREADS:20}
          max-acknowledgement-threads: ${MAX_ACKNOWLEDGEMENT_THREADS:20}
          retry:
            max-attempts: ${MAX_ATTEMPTS:5}
            initial-retry-delay-seconds: ${INITIAL_RPC_TIMEOUT_SECONDS:10}
            max-retry-delay-seconds: ${MAX_RETRY_DELAY_SECONDS:30}
            initial-rpc-timeout-seconds: ${INITIAL_RPC_TIMEOUT_SECONDS:10}
            max-rpc-timeout-seconds: ${MAX_RPC_TIMEOUT_SECONDS:30}
            total-timeout-seconds: ${TOTAL_TIMEOUT_SECONDS:60}
```