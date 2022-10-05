package br.ce.wcaquino.br.ce.wcaquino.exception;

public class FilmeSemEstoqueException extends Exception{

    private static final long serialVersionUID= -4970527916966267734L;

    public FilmeSemEstoqueException(String filme_sem_estoque) {
        super( filme_sem_estoque);
    }

}
