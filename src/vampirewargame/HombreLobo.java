package vampirewargame;

public class HombreLobo extends Pieza {

    public HombreLobo(ColorBando ladoEquipo) {
        super(TipoPieza.HOMBRE_LOBO, ladoEquipo, 5, 5, 2);
    }

    @Override
    public boolean movimientoValido(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) {
        if (renglonOrigen == renglonDestino && indiceColumnaOrigen == indiceColumnaDestino) {
            return false;
        }

        if (renglonOrigen == renglonDestino) {
            return indiceColumnaDestino == indiceColumnaOrigen - 1 || indiceColumnaDestino == indiceColumnaOrigen + 1
                    || indiceColumnaDestino == indiceColumnaOrigen - 2 || indiceColumnaDestino == indiceColumnaOrigen + 2;
        }

        if (indiceColumnaOrigen == indiceColumnaDestino) {
            return renglonDestino == renglonOrigen - 1 || renglonDestino == renglonOrigen + 1
                    || renglonDestino == renglonOrigen - 2 || renglonDestino == renglonOrigen + 2;
        }

        if (renglonDestino == renglonOrigen - 1 && indiceColumnaDestino == indiceColumnaOrigen - 1) return true;
        if (renglonDestino == renglonOrigen - 1 && indiceColumnaDestino == indiceColumnaOrigen + 1) return true;
        if (renglonDestino == renglonOrigen + 1 && indiceColumnaDestino == indiceColumnaOrigen - 1) return true;
        if (renglonDestino == renglonOrigen + 1 && indiceColumnaDestino == indiceColumnaOrigen + 1) return true;
        if (renglonDestino == renglonOrigen - 2 && indiceColumnaDestino == indiceColumnaOrigen - 2) return true;
        if (renglonDestino == renglonOrigen - 2 && indiceColumnaDestino == indiceColumnaOrigen + 2) return true;
        if (renglonDestino == renglonOrigen + 2 && indiceColumnaDestino == indiceColumnaOrigen - 2) return true;
        if (renglonDestino == renglonOrigen + 2 && indiceColumnaDestino == indiceColumnaOrigen + 2) return true;

        return false;
    }

    @Override
    public String getHabilidadEspecial() {
        return "Desplazamiento extendido: puede moverse hasta 2 casillas vacías en cualquier dirección.";
    }
}
