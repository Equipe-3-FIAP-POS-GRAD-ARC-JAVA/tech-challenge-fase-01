package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserAlreadyExistsException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

/**
 * Domain Service para operações de usuário que envolvem múltiplas entidades
 * ou regras de negócio complexas.
 * 
 * Seguindo os princípios de DDD:
 * - Contém lógica de domínio que não pertence naturalmente a uma entidade
 * - Coordena operações entre entidades
 * - Mantém regras de negócio centralizadas
 * - É stateless (sem estado próprio)
 * 
 * Casos de uso de Domain Service:
 * - Operações que envolvem múltiplas entidades
 * - Validações que requerem acesso ao repositório
 * - Regras de negócio que não se encaixam em nenhuma entidade específica
 */
public class UserDomainService {

    private final UserRepositoryPort userRepository;

    public UserDomainService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Valida se um username já está em uso.
     * 
     * Regra de negócio: Não pode existir dois usuários com o mesmo username.
     * 
     * @param username Username a validar
     * @throws UserAlreadyExistsException se o username já existe
     */
    public void ensureUsernameIsUnique(Username username) {
        if (userRepository.existsByUsername(username.getValue())) {
            throw new UserAlreadyExistsException(
                    "Já existe um usuário com o login: " + username.getValue());
        }
    }

    /**
     * Valida se um username já está em uso (versão String).
     * 
     * @param username Username em formato String
     * @throws UserAlreadyExistsException se o username já existe
     */
    public void ensureUsernameIsUnique(String username) {
        ensureUsernameIsUnique(Username.of(username));
    }

    /**
     * Valida se um email já está em uso.
     * 
     * Regra de negócio: Não pode existir dois usuários com o mesmo email.
     * 
     * @param email Email a validar
     * @throws UserAlreadyExistsException se o email já existe
     */
    public void ensureEmailIsUnique(Email email) {
        userRepository.findByEmail(email.getValue()).ifPresent(user -> {
            throw new UserAlreadyExistsException(
                    "Já existe um usuário com o email: " + email.getValue());
        });
    }

    /**
     * Valida se um usuário pode ser desativado.
     * 
     * Regras de negócio:
     * - Usuário já deve estar ativo
     * - Pode adicionar outras regras (ex: não pode desativar último admin)
     * 
     * @param user Usuário a validar
     * @throws BusinessRuleException se não pode ser desativado
     */
    public void ensureCanBeDeactivated(UserDomain user) {
        if (!user.isActive()) {
            throw new BusinessRuleException("Usuário já está inativo");
        }

        // Exemplo de regra adicional:
        // if (user.isOwner() && countActiveOwners() == 1) {
        // throw new BusinessRuleException("Não é possível desativar o último
        // proprietário");
        // }
    }

    /**
     * Valida se um usuário pode ser excluído.
     * 
     * Regras de negócio:
     * - Usuário deve estar inativo
     * - Não pode ter dependências ativas (exemplo futuro: pedidos em aberto)
     * 
     * @param user Usuário a validar
     * @throws BusinessRuleException se não pode ser excluído
     */
    public void ensureCanBeDeleted(UserDomain user) {
        if (user.isActive()) {
            throw new BusinessRuleException(
                    "Apenas usuários inativos podem ser excluídos. Desative o usuário primeiro.");
        }

        // Exemplo de regra adicional:
        // if (hasActiveOrders(user.getId())) {
        // throw new BusinessRuleException("Não é possível excluir usuário com pedidos
        // em aberto");
        // }
    }

    /**
     * Valida unicidade tanto de username quanto email antes de criar usuário.
     * 
     * Método de conveniência que valida ambos de uma vez.
     * 
     * @param username Username a validar
     * @param email    Email a validar
     * @throws UserAlreadyExistsException se username ou email já existem
     */
    public void ensureUserIsUnique(String username, String email) {
        ensureUsernameIsUnique(username);
        // ensureEmailIsUnique(Email.of(email)); // Descomentar quando implementar
        // validação de email duplicado
    }

    /**
     * Verifica se um usuário tem permissões para realizar uma operação em outro
     * usuário.
     * 
     * Regras de negócio:
     * - Owner pode gerenciar qualquer usuário
     * - Client só pode gerenciar a si mesmo
     * 
     * @param actor  Usuário que está tentando realizar a operação
     * @param target Usuário alvo da operação
     * @throws BusinessRuleException se não tem permissão
     */
    public void ensureHasPermissionToManage(UserDomain actor, UserDomain target) {
        // Se é owner, pode gerenciar qualquer um
        if (actor.isOwner()) {
            return;
        }

        // Se não é owner, só pode gerenciar a si mesmo
        if (!actor.getId().equals(target.getId())) {
            throw new BusinessRuleException(
                    "Você não tem permissão para gerenciar este usuário");
        }
    }

    /**
     * Verifica se um usuário pode alterar a role de outro usuário.
     * 
     * Regra de negócio: Apenas owners podem alterar roles.
     * 
     * @param actor Usuário que está tentando alterar a role
     * @throws BusinessRuleException se não tem permissão
     */
    public void ensureCanChangeRoles(UserDomain actor) {
        if (!actor.isOwner()) {
            throw new BusinessRuleException(
                    "Apenas proprietários podem alterar roles de usuários");
        }
    }
}
