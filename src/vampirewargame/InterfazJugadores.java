package vampirewargame;

public interface InterfazJugadores {

    void agregarJugador(Jugador participante) throws JuegoException;

    Jugador buscarJugador(String usuario);

    Jugador[] obtenerJugadoresActivos();

    Jugador[] obtenerRanking();

    int cantidad();
}
