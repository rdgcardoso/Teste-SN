package br.com.teste.testerecyclerview.domain.model;

import android.util.Log;

public enum Genero {

    VAZIO("Selecione um Gênero", -1),
    MASCULINO("Masculino", 0),
    FEMININO("Feminino", 1);

    private String descricao;
    private int id;

    Genero(String descricao, int id) {
        this.descricao = descricao;
        this.id = id;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public int getIdSelecionado() {
        return id;
    }
}
