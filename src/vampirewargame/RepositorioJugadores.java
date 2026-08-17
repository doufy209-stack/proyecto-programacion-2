package vampirewargame;

public class RepositorioJugadores implements InterfazJugadores {

    private Jugador[] jugadores;
    private int cantidadDanio;

    public RepositorioJugadores() {
        jugadores = new Jugador[10];
        cantidadDanio = 0;
    }

    @Override
    public void agregarJugador(Jugador participante) throws JuegoException {
        if (participante == null) {
            throw new JuegoException("El jugador no puede ser nulo.");
        }
        if (buscarJugador(participante.getUsuario()) != null) {
            throw new JuegoException("Ese nombre de usuario ya existe.");
        }

        ampliarArregloSiEsNecesario();
        jugadores[cantidadDanio] = participante;
        cantidadDanio++;
    }

    private void ampliarArregloSiEsNecesario() {
        if (cantidadDanio < jugadores.length) {
            return;
        }

        Jugador[] nuevo = new Jugador[jugadores.length * 2];
        for (int i = 0; i < jugadores.length; i++) {
            nuevo[i] = jugadores[i];
        }
        jugadores = nuevo;
    }

    @Override
    public Jugador buscarJugador(String usuario) {
        if (usuario == null) {
            return null;
        }
        return buscarJugadorRecursivo(usuario.trim(), 0);
    }

    private Jugador buscarJugadorRecursivo(String usuario, int indice) {
        if (indice >= cantidadDanio) {
            return null;
        }
        if (jugadores[indice].getUsuario().equalsIgnoreCase(usuario)) {
            return jugadores[indice];
        }
        return buscarJugadorRecursivo(usuario, indice + 1);
    }

    @Override
    public Jugador[] obtenerJugadoresActivos() {
        int activos = 0;
        for (int i = 0; i < cantidadDanio; i++) {
            if (jugadores[i].isActivo()) {
                activos++;
            }
        }

        Jugador[] resultadoAccion = new Jugador[activos];
        int posicion = 0;
        for (int i = 0; i < cantidadDanio; i++) {
            if (jugadores[i].isActivo()) {
                resultadoAccion[posicion] = jugadores[i];
                posicion++;
            }
        }
        return resultadoAccion;
    }

    @Override
    public Jugador[] obtenerRanking() {
        Jugador[] ranking = obtenerJugadoresActivos();
        ordenarRankingRecursivo(ranking, 0);
        return ranking;
    }

    private void ordenarRankingRecursivo(Jugador[] ranking, int inicio) {
        if (inicio >= ranking.length - 1) {
            return;
        }

        int posicionMayor = inicio;
        for (int i = inicio + 1; i < ranking.length; i++) {
            if (ranking[i].getPuntos() > ranking[posicionMayor].getPuntos()) {
                posicionMayor = i;
            }
        }

        Jugador temporal = ranking[inicio];
        ranking[inicio] = ranking[posicionMayor];
        ranking[posicionMayor] = temporal;

        ordenarRankingRecursivo(ranking, inicio + 1);
    }

    @Override
    public int cantidad() {
        return cantidadDanio;
    }
}
