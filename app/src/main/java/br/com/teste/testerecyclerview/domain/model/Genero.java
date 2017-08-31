package br.com.teste.testerecyclerview.domain.model;

import android.util.Log;

public enum Genero {

    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private String descricao;
    private int id;

    Genero(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
