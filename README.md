## Desafio Sicredi

#### Executar o projeto:

    git clone https://github.com/ricardorv/desafio-sicredi.git
    cd desafio-sicredi
    ./mvnw spring-boot:run
       
#### Documentação da API:

    http://localhost:8080/swagger-ui.html
    
#### Acesso banco de dados em memória

    http://localhost:8080/h2
    JDBC URL: jdbc:h2:mem:testdb
    USERNAME: sa
    PASSWORD: 
    
    * Caso esteja configurado para salvar em um arquivo, substitua JDBC URL por: jdbc:h2:file:./testdb
    
#### Observações
 - É possivel escolher entre persistir ou não os dados após o termino da aplicação, para isso, altere o 
 arquivo application.properties.
 - O projeto insere dados na tabela ASSOCIADOS. Caso esteja configurado para persistir em arquivo, alguns erros 
 serão exibidos ao executar pela segunda vez (irei ajustar isso no futuro).
