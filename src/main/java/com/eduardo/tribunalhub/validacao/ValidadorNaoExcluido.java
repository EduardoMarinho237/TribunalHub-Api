package com.eduardo.tribunalhub.validacao;

import java.util.Objects;

public class ValidadorNaoExcluido {

    /**
     * Valida se o objeto não está marcado como excluído.
     * @param visivel campo que indica se o objeto está ativo
     * @param nomeEntidade nome da entidade para mensagem de exceção
     */
    public static void validar(boolean visivel, String nomeEntidade) {
        if (!visivel) {
            throw new IllegalStateException(nomeEntidade + " já foi excluído");
        }
    }

    /**
     * Valida usando um objeto e um getter de visibilidade.
     * @param entidade objeto a ser validado
     * @param getterVisivel função que retorna o campo visivel
     * @param nomeEntidade nome da entidade para mensagem de exceção
     * @param <T> tipo da entidade
     */
    public static <T> void validar(T entidade, java.util.function.Function<T, Boolean> getterVisivel, String nomeEntidade) {
        Objects.requireNonNull(entidade, nomeEntidade + " não encontrado");
        Boolean visivel = getterVisivel.apply(entidade);
        if (visivel == null || !visivel) {
            throw new IllegalStateException(nomeEntidade + " já foi excluído");
        }
    }
}
