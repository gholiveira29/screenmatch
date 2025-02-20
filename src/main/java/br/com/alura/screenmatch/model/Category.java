package br.com.alura.screenmatch.model;

public enum Category {
    ACAO("Action"),
    DRAMA("Drama"),
    COMEDIA("Comedy"),
    ROMANCE("Romance"),
    CRIME("Crime"),;

    private String CategoryOmdb;

    Category(String categoryOmdb) {
        this.CategoryOmdb = categoryOmdb;
    }

    public static Category getCategory(String categoryOmdb) {
        for (Category category : Category.values()) {
            if (category.CategoryOmdb.equals(categoryOmdb)) {
                return category;
            }
        }
        throw  new IllegalArgumentException("Categoria não encontrada");
    }
}
