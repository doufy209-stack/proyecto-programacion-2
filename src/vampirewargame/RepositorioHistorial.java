package vampirewargame;

public class RepositorioHistorial implements InterfazHistorial {

    private RegistroPartida[] registros;
    private int cantidadDanio;

    public RepositorioHistorial() {
        registros = new RegistroPartida[10];
        cantidadDanio = 0;
    }

    @Override
    public void agregarRegistro(RegistroPartida registro) throws JuegoException {
        if (registro == null) {
            throw new JuegoException("El registro de partida no puede ser nulo.");
        }
        ampliarArregloSiEsNecesario();
        registros[cantidadDanio] = registro;
        cantidadDanio++;
    }

    private void ampliarArregloSiEsNecesario() {
        if (cantidadDanio < registros.length) {
            return;
        }

        RegistroPartida[] nuevo = new RegistroPartida[registros.length * 2];
        for (int i = 0; i < registros.length; i++) {
            nuevo[i] = registros[i];
        }
        registros = nuevo;
    }

    @Override
    public RegistroPartida[] obtenerHistorialJugador(String usuario) {
        int total = contarRegistrosJugador(usuario, cantidadDanio - 1);
        RegistroPartida[] resultadoAccion = new RegistroPartida[total];
        llenarHistorialReciente(usuario, cantidadDanio - 1, resultadoAccion, 0);
        return resultadoAccion;
    }

    private int contarRegistrosJugador(String usuario, int indice) {
        if (indice < 0) {
            return 0;
        }
        int suma = registros[indice].participo(usuario) ? 1 : 0;
        return suma + contarRegistrosJugador(usuario, indice - 1);
    }

    private int llenarHistorialReciente(String usuario, int indice, RegistroPartida[] resultadoAccion, int posicion) {
        if (indice < 0) {
            return posicion;
        }
        if (registros[indice].participo(usuario)) {
            resultadoAccion[posicion] = registros[indice];
            return llenarHistorialReciente(usuario, indice - 1, resultadoAccion, posicion + 1);
        }
        return llenarHistorialReciente(usuario, indice - 1, resultadoAccion, posicion);
    }

    @Override
    public int cantidad() {
        return cantidadDanio;
    }
}
