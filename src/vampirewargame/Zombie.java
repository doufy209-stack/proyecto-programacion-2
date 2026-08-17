package vampirewargame;

public final class Zombie extends Pieza {

    public Zombie(ColorBando ladoEquipo) {
        super(TipoPieza.ZOMBIE, ladoEquipo, 1, 1, 0);
    }

    @Override
    public boolean movimientoValido(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) {
        return false;
    }

    @Override
    public String getHabilidadEspecial() {
        return "No se mueve por sí mismo. Solo ataca cuando el Necrómante lo ordena.";
    }
}
