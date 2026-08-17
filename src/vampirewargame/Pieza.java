package vampirewargame;

public abstract class Pieza {

    protected TipoPieza categoriaPieza;
    protected ColorBando ladoEquipo;
    protected int valorAtaque;
    protected int puntosVida;
    protected int vidaMax;
    protected int puntosEscudo;

    public Pieza(TipoPieza categoriaPieza, ColorBando ladoEquipo, int valorAtaque, int puntosVida, int puntosEscudo) {
        this.categoriaPieza = categoriaPieza;
        this.ladoEquipo = ladoEquipo;
        this.valorAtaque = valorAtaque;
        this.puntosVida = puntosVida;
        this.vidaMax = puntosVida;
        this.puntosEscudo = puntosEscudo;
    }

    public abstract boolean movimientoValido(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino);

    public abstract String getHabilidadEspecial();

    public final ResultadoDanio atacarNormal(Pieza piezaObjetivo) throws JuegoException {
        if (piezaObjetivo == null) {
            throw new JuegoException("No hay una pieza para atacar.");
        }
        if (piezaObjetivo.getColor() == ladoEquipo) {
            throw new JuegoException("No puedes atacar una pieza de tu mismo bando.");
        }
        return piezaObjetivo.recibirDanio(valorAtaque);
    }

    public final ResultadoDanio recibirDanio(int danioEntrante) throws JuegoException {
        if (danioEntrante <= 0) {
            throw new JuegoException("El daño debe ser mayor que cero.");
        }

        int escudoAntes = puntosEscudo;
        int vidaAntes = puntosVida;

        int danioAlEscudo = danioEntrante;
        if (danioAlEscudo > puntosEscudo) {
            danioAlEscudo = puntosEscudo;
        }

        puntosEscudo = puntosEscudo - danioAlEscudo;
        int danioALaVida = danioEntrante - danioAlEscudo;

        if (danioALaVida > puntosVida) {
            danioALaVida = puntosVida;
        }
        puntosVida = puntosVida - danioALaVida;

        return new ResultadoDanio(
                danioAlEscudo,
                danioALaVida,
                puntosEscudo,
                puntosVida,
                estaDestruida()
        );
    }

    public final ResultadoDanio recibirDanioDirectoVida(int danioDirecto) throws JuegoException {
        if (danioDirecto <= 0) {
            throw new JuegoException("El daño debe ser mayor que cero.");
        }

        int vidaAntes = puntosVida;
        int danoAplicado = danioDirecto;
        if (danoAplicado > puntosVida) {
            danoAplicado = puntosVida;
        }
        puntosVida = puntosVida - danoAplicado;

        return new ResultadoDanio(
                0,
                vidaAntes - puntosVida,
                puntosEscudo,
                puntosVida,
                estaDestruida()
        );
    }

    public final boolean estaDestruida() {
        return puntosVida <= 0;
    }

    public final boolean estaAlLado(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) {
        int diferenciaFila = Math.abs(renglonDestino - renglonOrigen);
        int diferenciaColumna = Math.abs(indiceColumnaDestino - indiceColumnaOrigen);
        return diferenciaFila <= 1 && diferenciaColumna <= 1 && (diferenciaFila + diferenciaColumna > 0);
    }

    public TipoPieza getTipo() { return categoriaPieza; }
    public ColorBando getColor() { return ladoEquipo; }
    public int getAtaque() { return valorAtaque; }
    public int getVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMax; }
    public int getEscudo() { return puntosEscudo; }

    @Override
    public String toString() {
        return categoriaPieza.getNombre() + " " + ladoEquipo + " | ataque=" + valorAtaque + ", vida=" + puntosVida + ", escudo=" + puntosEscudo;
    }
}
