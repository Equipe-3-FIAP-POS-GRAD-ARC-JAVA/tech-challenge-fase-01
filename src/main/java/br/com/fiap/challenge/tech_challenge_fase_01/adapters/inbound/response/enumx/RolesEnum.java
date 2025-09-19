package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.enumx;

public enum RolesEnum {

    OWNER(1, "Owner"),
    CLIENT(2, "Client"),
    ADMIN(3, "Admin");

    private final int id;
    private final String description;

    RolesEnum(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
