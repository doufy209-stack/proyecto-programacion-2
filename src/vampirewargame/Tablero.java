package vampirewargame;

public class Tablero {

    public static final int TAMANO = 6;
    private final Pieza[][] celdasTablero;

    public Tablero() {
        celdasTablero = new Pieza[TAMANO][TAMANO];
        inicializarPiezas();
    }

    private void inicializarPiezas() {
        colocarFilaInicial(0, ColorBando.NEGRO);
        colocarFilaInicial(TAMANO - 1, ColorBando.BLANCO);
    }

    private void colocarFilaInicial(int renglon, ColorBando ladoEquipo) {
        celdasTablero[renglon][0] = new HombreLobo(ladoEquipo);
        celdasTablero[renglon][1] = new Vampiro(ladoEquipo);
        celdasTablero[renglon][2] = new Necromante(ladoEquipo);
        celdasTablero[renglon][3] = new Necromante(ladoEquipo);
        celdasTablero[renglon][4] = new Vampiro(ladoEquipo);
        celdasTablero[renglon][5] = new HombreLobo(ladoEquipo);
    }

    public Pieza getPieza(int renglon, int indiceColumna) throws JuegoException {
        validarPosicion(renglon, indiceColumna);
        return celdasTablero[renglon][indiceColumna];
    }

    public boolean estaVacia(int renglon, int indiceColumna) throws JuegoException {
        validarPosicion(renglon, indiceColumna);
        return celdasTablero[renglon][indiceColumna] == null;
    }

    public void moverPieza(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPosicion(renglonOrigen, indiceColumnaOrigen);
        validarPosicion(renglonDestino, indiceColumnaDestino);

        if (celdasTablero[renglonOrigen][indiceColumnaOrigen] == null) {
            throw new JuegoException("La casilla de origen está vacía.");
        }
        if (celdasTablero[renglonDestino][indiceColumnaDestino] != null) {
            throw new JuegoException("La casilla de destino debe estar vacía para mover una pieza.");
        }

        celdasTablero[renglonDestino][indiceColumnaDestino] = celdasTablero[renglonOrigen][indiceColumnaOrigen];
        celdasTablero[renglonOrigen][indiceColumnaOrigen] = null;
    }

    public void colocarPieza(Pieza elementoPieza, int renglon, int indiceColumna) throws JuegoException {
        validarPosicion(renglon, indiceColumna);
        if (elementoPieza == null) {
            throw new JuegoException("La pieza no puede ser nula.");
        }
        if (celdasTablero[renglon][indiceColumna] != null) {
            throw new JuegoException("La casilla seleccionada ya está ocupada.");
        }
        celdasTablero[renglon][indiceColumna] = elementoPieza;
    }

    public void eliminarPieza(int renglon, int indiceColumna) throws JuegoException {
        validarPosicion(renglon, indiceColumna);
        celdasTablero[renglon][indiceColumna] = null;
    }

    public boolean hayPiezaDelTipo(ColorBando ladoEquipo, TipoPieza categoriaPieza) {
        for (int renglon = 0; renglon < TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < TAMANO; indiceColumna++) {
                Pieza elementoPieza = celdasTablero[renglon][indiceColumna];
                if (elementoPieza != null && elementoPieza.getColor() == ladoEquipo && elementoPieza.getTipo() == categoriaPieza) {
                    return true;
                }
            }
        }
        return false;
    }

    public int eliminarZombiesDeColor(ColorBando ladoEquipo) {
        int totalEliminados = 0;
        for (int renglon = 0; renglon < TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < TAMANO; indiceColumna++) {
                Pieza elementoPieza = celdasTablero[renglon][indiceColumna];
                if (elementoPieza != null && elementoPieza.getColor() == ladoEquipo && elementoPieza.getTipo() == TipoPieza.ZOMBIE) {
                    celdasTablero[renglon][indiceColumna] = null;
                    totalEliminados++;
                }
            }
        }
        return totalEliminados;
    }

    public int contarPiezas(ColorBando ladoEquipo) {
        int cantidadDanio = 0;
        for (int renglon = 0; renglon < TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < TAMANO; indiceColumna++) {
                Pieza elementoPieza = celdasTablero[renglon][indiceColumna];
                if (elementoPieza != null && elementoPieza.getColor() == ladoEquipo) {
                    cantidadDanio++;
                }
            }
        }
        return cantidadDanio;
    }

    public int contarPiezasPrincipales(ColorBando ladoEquipo) {
        int cantidadDanio = 0;
        for (int renglon = 0; renglon < TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < TAMANO; indiceColumna++) {
                Pieza elementoPieza = celdasTablero[renglon][indiceColumna];
                if (elementoPieza != null && elementoPieza.getColor() == ladoEquipo && elementoPieza.getTipo() != TipoPieza.ZOMBIE) {
                    cantidadDanio++;
                }
            }
        }
        return cantidadDanio;
    }

    public boolean caminoLibreParaDosCasillas(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPosicion(renglonOrigen, indiceColumnaOrigen);
        validarPosicion(renglonDestino, indiceColumnaDestino);

        if (renglonDestino == renglonOrigen) {
            if (indiceColumnaDestino == indiceColumnaOrigen - 2) {
                return celdasTablero[renglonOrigen][indiceColumnaOrigen - 1] == null;
            }
            if (indiceColumnaDestino == indiceColumnaOrigen + 2) {
                return celdasTablero[renglonOrigen][indiceColumnaOrigen + 1] == null;
            }
        }

        if (indiceColumnaDestino == indiceColumnaOrigen) {
            if (renglonDestino == renglonOrigen - 2) {
                return celdasTablero[renglonOrigen - 1][indiceColumnaOrigen] == null;
            }
            if (renglonDestino == renglonOrigen + 2) {
                return celdasTablero[renglonOrigen + 1][indiceColumnaOrigen] == null;
            }
        }

        if (renglonDestino == renglonOrigen - 2 && indiceColumnaDestino == indiceColumnaOrigen - 2) {
            return celdasTablero[renglonOrigen - 1][indiceColumnaOrigen - 1] == null;
        }
        if (renglonDestino == renglonOrigen - 2 && indiceColumnaDestino == indiceColumnaOrigen + 2) {
            return celdasTablero[renglonOrigen - 1][indiceColumnaOrigen + 1] == null;
        }
        if (renglonDestino == renglonOrigen + 2 && indiceColumnaDestino == indiceColumnaOrigen - 2) {
            return celdasTablero[renglonOrigen + 1][indiceColumnaOrigen - 1] == null;
        }
        if (renglonDestino == renglonOrigen + 2 && indiceColumnaDestino == indiceColumnaOrigen + 2) {
            return celdasTablero[renglonOrigen + 1][indiceColumnaOrigen + 1] == null;
        }

        return true;
    }

    public boolean sinObstruccionLanza(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPosicion(renglonOrigen, indiceColumnaOrigen);
        validarPosicion(renglonDestino, indiceColumnaDestino);

        if (renglonDestino == renglonOrigen && indiceColumnaDestino == indiceColumnaOrigen - 2) {
            return celdasTablero[renglonOrigen][indiceColumnaOrigen - 1] == null;
        }
        if (renglonDestino == renglonOrigen && indiceColumnaDestino == indiceColumnaOrigen + 2) {
            return celdasTablero[renglonOrigen][indiceColumnaOrigen + 1] == null;
        }
        if (indiceColumnaDestino == indiceColumnaOrigen && renglonDestino == renglonOrigen - 2) {
            return celdasTablero[renglonOrigen - 1][indiceColumnaOrigen] == null;
        }
        if (indiceColumnaDestino == indiceColumnaOrigen && renglonDestino == renglonOrigen + 2) {
            return celdasTablero[renglonOrigen + 1][indiceColumnaOrigen] == null;
        }

        return false;
    }

    public void validarPosicion(int renglon, int indiceColumna) throws JuegoException {
        if (renglon < 0 || renglon >= TAMANO || indiceColumna < 0 || indiceColumna >= TAMANO) {
            throw new JuegoException("La posición debe estar dentro del tablero 6x6.");
        }
    }
}
