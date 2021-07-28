# VISÃO GERAL

## O que é?`

Spring Batch é uma estrutura de lote leve e abrangente projetada para permitir o desenvolvimento de aplicativos 
de lote robustos, vitais para as operações diárias de sistemas corporativos.

Spring Batch fornece funções reutilizáveis que são essenciais no processamento de grandes volumes de registros, 
incluindo logging/tracing, gerenciamento de transações, estatísticas de processamento de trabalho, 
reinício de trabalho, skip, e gerenciamento de recursos.

Em especial nessa POC **sbatch-arqt-refe-atom-pubsub** foram criados componentes customizados tanto para leitura como
para a escrita, o qual sera abordado em sequência.

## Quando utilizar?

Muitos aplicativos no domínio corporativo requerem processamento em massa para realizar operações de negócios em
ambientes de missão crítica. Essas operações comerciais incluem:

- Processamento automatizado e complexo de grandes volumes de informações que são processadas de forma mais eficiente sem
  interação do usuário. Essas operações geralmente incluem eventos baseados em tempo (como cálculos de fim de mês, avisos ou correspondência).

- Aplicação periódica de regras de negócios complexas processadas repetidamente em conjuntos de dados muito grandes 
(por exemplo, determinação de benefícios de seguro ou ajustes de taxas).

- Integração de informações que são recebidas de sistemas internos e externos que normalmente requerem formatação, 
  validação e processamento de maneira transacional no sistema de registro. O processamento em lote é usado para processar 
  bilhões de transações todos os dias para empresas.

- Recomenda-se a utilização baseado dessa POC **sbatch-arqt-refe-atom-pubsub** quanto ouver a necessidade de integração
com o serviço de mensagens assíncronas Google Cloud Pub/Sub.

# REFERÊNCIA TÉCNICA


## Componentes utilizados
  
- Para o nosso estudo de caso, foram criados componentes customizados tanto para leitura como a escrita de mensagens.
  

## Particularidades ou Restrinções para uso.


- Criar Topico no GCP
- Criar um subscription
- Enviar direto pelo console do GCP para possiveis testes

```yaml
job:
  topic: ${TOPIC}
  subscription: ${SUBSCRIPTION}
```

Essa solução foi testada com um arquivo XML contento 1 milhão de registro, tendo um tempo de processamento médio de 1 minuto.


# APLICAÇÃO DE REFERÊNCIA

O projeto utiliza as seguintes tecnologias:

1) Java 11
2) Maven 3.5.3
3) SprintBoot 2+
4) JUnit Jupiter 5+
5) Lombok 1.18.20

Segue a descrição dos principais componentes utilizados para Escrita e para a Leitura.

### StaxEventItemReader

O componente StaxEventItemReader fornece uma configuração típica para o processamento de registros de um fluxo de entrada XML.

O exemplo a seguir mostra a leitura de um elemento raiz, chamado customer.

Segue o modelo em XML:

```xml
<customers>
    <customer>
        <name>Tesla</name>
        <email>elon@tesla.com</email>
        <itens>
            <item>Item 1</item>
            <item>Item 2</item>
            <item>Item 3</item>
        </itens>
    </customer>
    <customer>
        <name>Microsoft</name>
        <email>bill@microsoft.com</email>
        <itens>
            <item>Item 5</item>
            <item>Item 6</item>
            <item>Item 7</item>
        </itens>
    </customer>
    <customer>
        <name>Google</name>
        <email>larry@google.com</email>
        <itens>
            <item>Item 8</item>
            <item>Item 9</item>
            <item>Item 10</item>
        </itens>
    </customer>
</customers>
```

Segue o modelo de leitura em codigo Java:

```java
@Bean
public ItemReader<Customer> xmlReader() {
    var customerMarshaller = new Jaxb2Marshaller();
    customerMarshaller.setClassesToBeBound(Customer.class);
    return new StaxEventItemReaderBuilder<Customer>()
            .name("customerReader")
            .resource(new FileSystemResource(this.resource))
            .addFragmentRootElements("customer")
            .unmarshaller(customerMarshaller)
            .build();
}
```

### StaxEventItemWriter

O componente StaxEventItemWriter fornece uma configuração típica para o processamento de registros de um fluxo de
excrita dos dados em XML.

O exemplo a seguir mostra a escrita de um elemento raiz, chamado customers.

Segue o modelo de codigo escrita em codigo Java:

```java
@Bean
    public ItemWriter<Customer> printWriter() {
        var customerMarshaller = new Jaxb2Marshaller();
        customerMarshaller.setClassesToBeBound(Customer.class);
        return new StaxEventItemWriterBuilder<Customer>()
                .name("customerWriter")
                .marshaller(customerMarshaller)
                .resource(new FileSystemResource(this.resource))
                .rootTagName("customers")
                .overwriteOutput(true)
                .build();
    }
```

### Instalando dependências e compilando

Para instalar as dependências e compilar o seu projeto, execute o seguinte comando no bash.

```
$ mvn clean install
``` 

Certifique-se de o repositório do Maven está corretamente configurado.

### Executando o projeto

O projeto pode ser executado de duas formas após sua compilação:
1) via IntelliJ, via classe `SpringBatchApplication.java`
2) via java, utilizando o comando:

```
// na pasta `target`
$ java -jar sbatch-arqt-refe-atom-modelo-1.0.0.jar
```

### Etapas da Execução

O projeto executará as seguintes etapas:

- Reader: Leitura do arquivo [customer_1.xml] o qual se encontra na pasta raíz do projeto [xmlsExamples].
- Processor: Recebe os dados parseados e transformados em objeto, convertendo alguns campos em maiúsculo (UpperCase).
- Writer: Após processar os dados cria outro arquivo com o nome output_customer_1.xml na pasta raíz do projeto [xmlsExamples].

### Parâmetro de Entrada

Não é necessário informar um parâmetro de entrada para execução do projeto.

Caso seja utilizado um banco de dados com persistência será necessário informar um parâmetro para execução e reexecução.

### Banco de Dados em memória

Utilização de banco de dados em memória com nome da base de dados `batch`.

As configurações do banco de dados são definidas através da configuração abaixo:

```yaml
spring:
    database:
      url: jdbc:h2:mem:batch
      driver-class-name: org.h2.Driver
      username: sa
      password: sa
```

As tabelas de controle do SpringBatch são criadas automaticamente através do parâmetro definido abaixo:

```yaml
spring:
  application:
    batch:
      initialize-schema: always
```

### Banco de Dados local 

Utilização de banco de dados localhost postgresql com nome da base de dados `batch`.

#### STEP 01

Ativer o profile postgres, na execução da sua aplicação.

```
-Dspring.profiles.active=postgres
```

#### STEP 02

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

# LIGAÇÕES EXTERNAS

- Bitbucket do projeto [clique aqui](https://bitbucket.bvnet.bv/scm/arqt-refe/sbatch-arqt-refe-atom-large-xml-file.git)
- Spring batch Reference doc [clique aqui](https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/)
- XML Item Readers and Writers [clique aqui](https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/readersAndWriters.html#xmlReadingWriting)




