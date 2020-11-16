## Desafio Sicredi

#### Executar o projeto:

    git clone https://github.com/ricardorv/desafio-sicredi.git
    cd desafio-sicredi
    ./mvnw spring-boot:run
       
#### Executar o activemq localmente

    docker run -p 61616:61616 -p 8161:8161 rmohr/activemq
       
#### Documentação da API:

    http://localhost:8080/swagger-ui.html
    
#### Acesso banco de dados em memória

    http://localhost:8080/h2
    JDBC URL: jdbc:h2:mem:testdb
    USERNAME: sa
    PASSWORD: 
    
    * Caso esteja configurado para salvar em um arquivo, substitua JDBC URL por: jdbc:h2:file:./testdb
    
#### Observações
 - É possível escolher entre persistir ou não os dados após o termino da aplicação, para isso, altere o 
 arquivo application.properties.
 - Para votar é necessário informar o usuário. Os usuários válidos são: usuario1, usuario2, usuario3 e usuario4.
