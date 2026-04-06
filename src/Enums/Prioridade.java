package Enums;

public enum Prioridade {
    BAIXA(72),
    MEDIA(48),
    ALTA(24),
    URGENTE(4);

    private final int slaHoras;

    Prioridade(int slaHoras) {
        this.slaHoras = slaHoras;
    }

    public int getSlaHoras() {
        return slaHoras;
    }
}
