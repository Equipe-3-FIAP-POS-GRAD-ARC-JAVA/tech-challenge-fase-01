# Índice dos Diagramas - Tech Challenge

Este documento apresenta todos os diagramas PlantUML do projeto, organizados por categoria e funcionalidade.

## 📊 Visão Geral dos Diagramas

### 🏗️ **Arquitetura e Estrutura**

#### [`diag01.puml`](diag01.puml) - Arquitetura Hexagonal Completa
![Arquitetura Hexagonal](Hexagonal_Architecture.png)

- **Descrição**: Visão completa da arquitetura hexagonal com todas as camadas
- **Conteúdo**: 
  - Driving Adapters (Controllers, Security)
  - Application Core (Use Cases, Domain, Ports)
  - Driven Adapters (Repositories, Security implementations)
  - Relacionamentos entre componentes
- **Foco**: User e Address management
- **Atualizado**: ✅ Incluindo fluxos completos de Address

#### [`diag05.puml`](diag05.puml) - Visão Geral Simplificada
![Arquitetura Completa](Complete_Architecture_Overview.png)

- **Descrição**: Overview simplificado dos componentes principais
- **Conteúdo**: 
  - Camadas principais (Web, Application, Infrastructure)
  - Use Cases principais
  - Componentes de domínio
  - Database
- **Foco**: Visão macro da arquitetura
- **Atualizado**: ✅ Incluindo Use Cases de Address

#### [`diag07.puml`](diag07.puml) - Estrutura de Packages
![Estrutura de Pacotes](Package_Structure_Complete.png)

- **Descrição**: Organização completa de packages e classes do projeto
- **Conteúdo**: 
  - Estrutura hierárquica de packages
  - Classes principais por package
  - Interfaces e implementations
  - Relacionamentos entre packages
- **Foco**: Organização código e separação de responsabilidades
- **Novo**: ✅ Criado para documentar estrutura atual

---

### 🏢 **Domínio e Modelo de Classes**

#### [`diag02.puml`](diag02.puml) - Modelo de Domínio Completo
![Modelo de Domínio](Domain_Model_Classes.png)

- **Descrição**: Classes de domínio com detalhes completos
- **Conteúdo**: 
  - **Domain Entities**: UserDomain, AddressDomain
  - **Value Objects**: Email, Username, PersonName
  - **Domain Services**: UserDomainService, AddressDomainService  
  - **Enums**: RolesEnum
  - **Ports**: Todas as interfaces de entrada e saída
  - **Exceptions**: BusinessRuleException, InvalidFieldException
- **Foco**: Design rico do domínio
- **Atualizado**: ✅ AddressDomain com constantes de validação e métodos detalhados

---

### 🔐 **Fluxos de Sequência**

#### [`diag03.puml`](diag03.puml) - Fluxo de Autenticação JWT
![Sequência de Autenticação](Authentication_Sequence.png)

- **Descrição**: Sequence diagram detalhado do processo de autenticação
- **Conteúdo**: 
  - Login bem-sucedido completo
  - Validação de token em requisições subsequentes
  - Cenários de erro (usuário não encontrado, token inválido)
  - Interação entre todas as camadas
- **Foco**: Segurança e autenticação
- **Atualizado**: ✅ Corrigido nome de classes (BCryptPasswordEncoder)

#### [`diag06.puml`](diag06.puml) - Fluxos Completos de Address
![Fluxo de Endereços](Address_Complete_Flow_Sequence.png)

- **Descrição**: Sequence diagrams para todos os CRUDs de Address
- **Conteúdo**: 
  - **Criar Endereço**: POST /api/v1/address
  - **Listar Endereços**: GET /api/v1/address  
  - **Atualizar Endereço**: PUT /api/v1/address/{id}
  - **Excluir Endereço**: DELETE /api/v1/address/{id}
  - Validações de ownership em todos os fluxos
  - Cenários de erro (403 Forbidden, 404 Not Found)
- **Foco**: CRUD completo de Address com segurança
- **Novo**: ✅ Criado especificamente para documentar fluxos de Address

---

### 🗄️ **Banco de Dados**

#### [`diag04.puml`](diag04.puml) - Schema PostgreSQL
![Schema do Banco](Database_Schema.png)

- **Descrição**: Estrutura completa das tabelas do banco
- **Conteúdo**: 
  - Tabela `users` com campos e constraints
  - Tabela `address` com relacionamento 1:N
  - Campos, tipos, constraints e índices
  - Relacionamentos FK
  - Notas sobre validações de domínio
- **Foco**: Persistência e modelo relacional
- **Atualizado**: ✅ Constraints de validação do domínio documentadas

---

## 🎯 **Como Usar os Diagramas**

### **Para Desenvolvedores Novos no Projeto**
1. Começar com [`diag01.puml`](diag01.puml) - Entender a arquitetura geral
2. Estudar [`diag07.puml`](diag07.puml) - Ver organização de packages
3. Analisar [`diag02.puml`](diag02.puml) - Compreender o modelo de domínio

### **Para Implementar Novos Features**
1. Consultar [`diag02.puml`](diag02.puml) - Ver domain services e ports existentes
2. Verificar [`diag06.puml`](diag06.puml) - Seguir padrões de sequence flows
3. Atualizar [`diag01.puml`](diag01.puml) - Incluir novos componentes na arquitetura

### **Para Auditoria de Segurança**
1. Analisar [`diag03.puml`](diag03.puml) - Fluxo de autenticação JWT
2. Verificar [`diag06.puml`](diag06.puml) - Validações de ownership
3. Consultar [`diag01.puml`](diag01.puml) - Security filters e exception handling

### **Para Análise de Performance**
1. Estudar [`diag04.puml`](diag04.puml) - Índices e relacionamentos de BD
2. Analisar [`diag06.puml`](diag06.puml) - Queries executadas nos fluxos
3. Verificar [`diag01.puml`](diag01.puml) - Caching e repository patterns

---

## 📁 **Arquivos de Diagrama**

```
docs/
├── diag01.puml  # Arquitetura Hexagonal Completa
├── diag02.puml  # Modelo de Domínio
├── diag03.puml  # Fluxo de Autenticação JWT
├── diag04.puml  # Schema PostgreSQL
├── diag05.puml  # Visão Geral Simplificada
├── diag06.puml  # Fluxos de Address (NOVO)
├── diag07.puml  # Estrutura de Packages (NOVO)
└── DIAGRAMS-INDEX.md  # Este arquivo
```

---

## ✅ **Status das Atualizações**

| Diagrama | Status | Última Atualização | Inclui Address |
|----------|--------|-------------------|----------------|
| diag01.puml | ✅ Atualizado | Nov 2025 | ✅ Sim |
| diag02.puml | ✅ Atualizado | Nov 2025 | ✅ Sim |
| diag03.puml | ✅ Atualizado | Nov 2025 | ❌ Não (foco em Auth) |
| diag04.puml | ✅ Atualizado | Nov 2025 | ✅ Sim |
| diag05.puml | ✅ Atualizado | Nov 2025 | ✅ Sim |
| diag06.puml | ✅ Novo | Nov 2025 | ✅ Sim (foco principal) |
| diag07.puml | ✅ Novo | Nov 2025 | ✅ Sim |

---

## 🔄 **Convenções de Atualização**

Para manter os diagramas sempre atualizados:

1. **Novos Use Cases**: Atualizar diag01.puml e diag02.puml
2. **Novos Endpoints**: Atualizar diag01.puml e criar sequence em diag06.puml  
3. **Mudanças de DB**: Atualizar diag04.puml
4. **Novos Packages**: Atualizar diag07.puml
5. **Mudanças de Security**: Atualizar diag03.puml

### **Validação de Diagramas**
- Todos os diagramas devem ser validados com PlantUML antes do commit
- Manter consistência de nomes entre código e diagramas
- Documentar mudanças significativas neste índice

---

**Última atualização**: Novembro 2025  
**Versão do projeto**: Tech Challenge Fase 01  
**Arquitetura**: Hexagonal + Clean Architecture + SOLID  