package vampirewargame;

public class Vampiro extends Pieza {

    public Vampiro(ColorBando ladoEquipo) {
        super(TipoPieza.VAMPIRO, ladoEquipo, 3, 4, 5);
    }

    @Override
    public boolean movimientoValido(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) {
        return estaAlLado(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino);
    }

    public ResultadoDanio absorberSangre(Pieza piezaObjetivo) throws JuegoException {
        if (piezaObjetivo == null) {
            throw new JuegoException("No hay una pieza enemiga para absorber sangre.");
        }
        if (piezaObjetivo.getColor() == ladoEquipo) {
            throw new JuegoException("No puedes absorber sangre de una pieza de tu mismo bando.");
        }

        ResultadoDanio resultadoAccion = piezaObjetivo.recibirDanio(1);
        if (resultadoAccion.getDanoTotal() > 0) {
            puntosVida = Math.min(vidaMax, puntosVida + 1);
        }
        return resultadoAccion;
    }

    @Override
    public String getHabilidadEspecial() {
        return "Absorción de sangre: resta 1 punto al enemigo y recupera 1 punto de vida.";
    }
}
