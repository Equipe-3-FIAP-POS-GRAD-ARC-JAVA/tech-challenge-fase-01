# Projeto: Nome do projeto

## Equipe: Lista dos nomes e RMs dos alunos

| Nome | RM |
| --- | --- |
| Emerson Pereira da Silva | RM367268 |
| Levi Aparecido do Santos | RM369031 |
| Luiz Octavio Tassinari Saraiva | RM367408|
| Rhayana Lacerda Gomes | | 
| Vinicius Padovam Valentim | RM367199|

 
# 1. Introdução
   
## 1.1. Descrição do problema

Um grupo de restaurantes busca desenvolver um sistema de gestão unificado e compartilhado para reduzir os altos custos de soluções individuais. O objetivo é criar uma plataforma robusta que permita aos restaurantes gerenciar suas operações de forma eficiente e, ao mesmo tempo, ofereça aos clientes a possibilidade de consultar informações, fazer pedidos online e deixar avaliações.
Devido a limitações orçamentárias, o projeto será entregue em fases, permitindo uma implementação gradual, com melhorias contínuas baseadas no feedback dos restaurantes e clientes.


## 1.2. Objetivo do projeto

Desenvolver um backend completo e robusto utilizando Spring Boot e MySQL.
O sistema deve permitir:
- Cadastro, atualização e exclusão de usuários;
- Troca de senha do usuário em endpoint separado;
- Atualização das  demais informações do  usuário  em  endpoint  distinto do endpoint de senha;
- Registro da data da última alteração;
- Busca de usuários pelo nome;
- Garantia de que o e-mail cadastrado seja único;
- Validação  de  login  obrigatória,  por  meio  de  um serviço que  verifique se login e senha são válidos:

- Implementações extras:
<<<<<<< Updated upstream
    - Conter três niveis de acesso: Cliente, Restaurante e Administrador;
=======
    - Conter três niveis de acesso: Cliente, Restaurante (Dono) e Administrador;
>>>>>>> Stashed changes
    - Testes

A  aplicação  deverá  ser  dockerizada,  utilizando  Docker  Compose  para orquestração junto com um banco de dados relacional (MySQL)

# 2. Arquitetura do Sistema

Descrição da Arquitetura

// TODO
Hexagonal

[Explique brevemente a estrutura do sistema, como a separação de camadas 
(Controller, Service, Repository), uso do Docker, banco de dados escolhido, etc.]
Diagrama da Arquitetura

[Insira um diagrama explicando os componentes principais.]

# 3. Descrição dos Endpoints da API
Tabela de Endpoints 
Endpoint    Método    Descrição
     
Exemplos de requisição e resposta

Descreva aqui exemplos de requisições e possíveis respostas.

# 4. Configuração do Projeto
 
Configuração do Docker Compose

Descreva o arquivo docker-compose.yml e explique como ele orquestra a aplicação e o banco de dados.
Instruções para execução local
[Documente as URLs e portas configuradas.]

# 5. Qualidade do Código
 
Boas Práticas Utilizadas

[Detalhe práticas de organização e padrões seguidos, como DRY, SOLID, e convenções do Spring Boot.]

# 6. Collections para Teste
 
Link para a Collection do Postman
[Inclua um link ou descreva como importar as collections.]
Descrição dos Testes Manuais
[Descreva como validar os endpoints usando as collections.]

# 7. Repositório do Código
 
URL do Repositório no Github: https://github.com/Equipe-3-FIAP-POS-GRAD-ARC-JAVA/tech-challenge-fase-01
