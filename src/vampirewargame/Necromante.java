package vampirewargame;

public class Necromante extends Pieza {

    public Necromante(ColorBando ladoEquipo) {
        super(TipoPieza.NECROMANTE, ladoEquipo, 4, 3, 1);
    }

    @Override
    public boolean movimientoValido(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) {
        return estaAlLado(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino);
    }

    public ResultadoDanio lanzarLanza(Pieza piezaObjetivo) throws JuegoException {
        if (piezaObjetivo == null) {
            throw new JuegoException("No hay una pieza enemiga en el destino.");
        }
        if (piezaObjetivo.getColor() == ladoEquipo) {
            throw new JuegoException("No puedes lanzar la lanza contra una pieza de tu mismo bando.");
        }
        return piezaObjetivo.recibirDanioDirectoVida(2);
    }

    public Zombie invocarZombie() {
        return new Zombie(ladoEquipo);
    }

    public ResultadoDanio ordenarAtaqueZombie(Zombie invocacionZombie, Pieza piezaObjetivo) throws JuegoException {
        if (invocacionZombie == null || invocacionZombie.getColor() != ladoEquipo) {
            throw new JuegoException("Debes seleccionar un Zombie propio.");
        }
        return invocacionZombie.atacarNormal(piezaObjetivo);
    }

    @Override
    public String getHabilidadEspecial() {
        return "Lanza a distancia, invocación de Zombie y ataque a través de un Zombie propio.";
    }
}
