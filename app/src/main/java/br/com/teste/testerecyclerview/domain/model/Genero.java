package br.com.teste.testerecyclerview.domain.model;


import java.util.ArrayList;
import java.util.List;

public class Genero {
    private static final String[][] generos = {
            {"1", "Masculino"},
            {"2", "Feminino"}
    };

    private int id;
    private String descricao;

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setId(int id) {
        this.id = id;

        for (int i = 0; i < generos.length; i++) {
            if (generos[i][0].equals(String.valueOf(id))) {
                descricao = generos[i][1];
            }
        }
    }

    public void setDescricao(String descricao) {

        this.descricao = descricao;

        for (int i = 0; i < generos.length; i++) {
            if (generos[i][1].equals(descricao)) {
                id = Integer.parseInt(generos[i][0]);
            }
        }
    }

    public List<String> getGenerosDescricaoList() {

       ArrayList<String> generosDescricao = new ArrayList<>();

        for (int i = 0; i < generos.length; i++) {
            generosDescricao.add(generos[i][1]);
        }
        return generosDescricao;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "id='" + id + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
