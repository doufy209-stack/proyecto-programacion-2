package vampirewargame;

public class Partida {

    private static final int PUNTOS_POR_VICTORIA = 3;

    private final Jugador usuarioBlanco;
    private final Jugador usuarioNegro;
    private final Tablero tableroActual;
    private final Ruleta ruleta;
    private final InterfazHistorial historial;
    private ColorBando ladoEnTurno;
    private TipoPieza tipoHabilitado;
    private int girosUsados;
    private boolean activa;
    private String mensajeFinal;

    public Partida(Jugador usuarioBlanco, Jugador usuarioNegro, InterfazHistorial historial) throws JuegoException {
        if (usuarioBlanco == null || usuarioNegro == null) {
            throw new JuegoException("La partida necesita dos jugadores.");
        }
        if (usuarioBlanco == usuarioNegro || usuarioBlanco.getUsuario().equalsIgnoreCase(usuarioNegro.getUsuario())) {
            throw new JuegoException("El oponente debe ser un jugador diferente.");
        }
        if (!usuarioBlanco.isActivo() || !usuarioNegro.isActivo()) {
            throw new JuegoException("Ambos jugadores deben tener sus cuentas activas.");
        }
        if (historial == null) {
            throw new JuegoException("No se encontró el almacenamiento del historial.");
        }

        this.usuarioBlanco = usuarioBlanco;
        this.usuarioNegro = usuarioNegro;
        this.historial = historial;
        tableroActual = new Tablero();
        ruleta = new Ruleta();
        ladoEnTurno = ColorBando.BLANCO;
        tipoHabilitado = null;
        girosUsados = 0;
        activa = true;
        mensajeFinal = "";
    }

    public ResultadoRuleta girarRuleta() throws JuegoException {
        validarPartidaActiva();
        if (tipoHabilitado != null) {
            throw new JuegoException("Ya tienes una pieza habilitada para este turno.");
        }

        int maximoGiros = getGirosPermitidosTurnoActual();
        if (girosUsados >= maximoGiros) {
            throw new JuegoException("Ya utilizaste todos los giros permitidos en este turno.");
        }

        girosUsados++;
        TipoPieza resultadoAccion = ruleta.girar();
        boolean disponible = tableroActual.hayPiezaDelTipo(ladoEnTurno, resultadoAccion);

        if (disponible) {
            tipoHabilitado = resultadoAccion;
            return new ResultadoRuleta(resultadoAccion, true, false, "La ruleta seleccionó: " + resultadoAccion.getNombre() + ".");
        }

        boolean puedeVolver = girosUsados < maximoGiros;
        if (puedeVolver) {
            return new ResultadoRuleta(resultadoAccion, false, true, "La ruleta seleccionó " + resultadoAccion.getNombre() + ", pero ya no tienes piezas de ese tipo. Puedes volver a girar.");
        }

        String mensaje = "La ruleta seleccionó " + resultadoAccion.getNombre() + " y no tienes piezas disponibles de ese tipo. Pierdes el turno.";
        cambiarTurno();
        return new ResultadoRuleta(resultadoAccion, false, false, mensaje);
    }

    public String moverPieza(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza elementoPieza = validarPiezaOrigen(renglonOrigen, indiceColumnaOrigen);

        if (!tableroActual.estaVacia(renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("La casilla destino está ocupada. Debes usar una acción de ataque.");
        }
        if (!elementoPieza.movimientoValido(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("Ese movimiento no es válido para " + elementoPieza.getTipo().getNombre() + ".");
        }
        if (elementoPieza.getTipo() == TipoPieza.HOMBRE_LOBO && !tableroActual.caminoLibreParaDosCasillas(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("El Hombre Lobo no puede atravesar una pieza para moverse dos casillas.");
        }

        tableroActual.moverPieza(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino);
        String mensaje = "Se movió " + elementoPieza.getTipo().getNombre() + " a la casilla [" + renglonDestino + ", " + indiceColumnaDestino + "].";
        terminarAccion();
        return mensaje;
    }

    public String atacarNormal(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza atacante = validarPiezaOrigen(renglonOrigen, indiceColumnaOrigen);
        Pieza piezaObjetivo = validarObjetivoEnemigo(renglonDestino, indiceColumnaDestino);

        if (!atacante.estaAlLado(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("El ataque normal solo puede dirigirse a una pieza enemiga al lado.");
        }

        ResultadoDanio resultadoAccion = atacante.atacarNormal(piezaObjetivo);
        String mensaje = construirMensajeAtaque(piezaObjetivo, resultadoAccion, renglonDestino, indiceColumnaDestino);
        resolverDespuesDelAtaque(renglonDestino, indiceColumnaDestino, piezaObjetivo, resultadoAccion);
        return mensaje;
    }

    public String absorberSangre(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza elementoPieza = validarPiezaOrigen(renglonOrigen, indiceColumnaOrigen);
        if (elementoPieza.getTipo() != TipoPieza.VAMPIRO) {
            throw new JuegoException("La absorción de sangre solo pertenece al Vampiro.");
        }
        if (!elementoPieza.estaAlLado(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("El Vampiro solo puede absorber sangre de una pieza enemiga al lado.");
        }

        Pieza piezaObjetivo = validarObjetivoEnemigo(renglonDestino, indiceColumnaDestino);
        Vampiro vampiro = (Vampiro) elementoPieza;
        ResultadoDanio resultadoAccion = vampiro.absorberSangre(piezaObjetivo);
        String mensaje = "El Vampiro absorbió sangre. " + construirMensajeAtaque(piezaObjetivo, resultadoAccion, renglonDestino, indiceColumnaDestino);
        resolverDespuesDelAtaque(renglonDestino, indiceColumnaDestino, piezaObjetivo, resultadoAccion);
        return mensaje;
    }

    public String lanzarLanza(int renglonOrigen, int indiceColumnaOrigen, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza elementoPieza = validarPiezaOrigen(renglonOrigen, indiceColumnaOrigen);
        if (elementoPieza.getTipo() != TipoPieza.NECROMANTE) {
            throw new JuegoException("El ataque de lanza solo pertenece al Necrómante.");
        }
        if (!tableroActual.sinObstruccionLanza(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("La lanza requiere un enemigo a 2 casillas en línea horizontal o vertical y sin obstrucciones.");
        }

        Pieza piezaObjetivo = validarObjetivoEnemigo(renglonDestino, indiceColumnaDestino);
        Necromante necromante = (Necromante) elementoPieza;
        ResultadoDanio resultadoAccion = necromante.lanzarLanza(piezaObjetivo);
        String mensaje = "El Necrómante lanzó su lanza ignorando el escudo. " + construirMensajeAtaque(piezaObjetivo, resultadoAccion, renglonDestino, indiceColumnaDestino);
        resolverDespuesDelAtaque(renglonDestino, indiceColumnaDestino, piezaObjetivo, resultadoAccion);
        return mensaje;
    }

    public String invocarZombie(int renglonNecromante, int indiceColumnaNecromante, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        validarPartidaActiva();
        Pieza elementoPieza = validarPiezaOrigen(renglonNecromante, indiceColumnaNecromante);
        if (elementoPieza.getTipo() != TipoPieza.NECROMANTE) {
            throw new JuegoException("Solo el Necrómante puede invocar Zombies.");
        }
        if (!tableroActual.estaVacia(renglonDestino, indiceColumnaDestino)) {
            throw new JuegoException("El Zombie solo puede invocarse en una casilla vacía.");
        }

        Necromante necromante = (Necromante) elementoPieza;
        tableroActual.colocarPieza(necromante.invocarZombie(), renglonDestino, indiceColumnaDestino);
        String mensaje = "El Necrómante invocó un Zombie en la casilla [" + renglonDestino + ", " + indiceColumnaDestino + "].";
        terminarAccion();
        return mensaje;
    }

    public String atacarPorZombie(int renglonNecromante, int indiceColumnaNecromante, int filaZombie, int columnaZombie, int renglonObjetivo, int indiceColumnaObjetivo) throws JuegoException {
        validarPartidaActiva();
        Pieza piezaNecromante = validarPiezaOrigen(renglonNecromante, indiceColumnaNecromante);
        if (piezaNecromante.getTipo() != TipoPieza.NECROMANTE) {
            throw new JuegoException("Debes seleccionar un Necrómante habilitado por la ruleta.");
        }

        Pieza piezaZombie = tableroActual.getPieza(filaZombie, columnaZombie);
        if (piezaZombie == null || piezaZombie.getTipo() != TipoPieza.ZOMBIE || piezaZombie.getColor() != ladoEnTurno) {
            throw new JuegoException("Debes seleccionar un Zombie propio para ejecutar este ataque.");
        }

        Pieza piezaObjetivo = validarObjetivoEnemigo(renglonObjetivo, indiceColumnaObjetivo);
        if (!piezaZombie.estaAlLado(filaZombie, columnaZombie, renglonObjetivo, indiceColumnaObjetivo)) {
            throw new JuegoException("El enemigo debe estar al lado al Zombie propio.");
        }

        int distanciaFila = Math.abs(renglonObjetivo - renglonNecromante);
        int distanciaColumna = Math.abs(indiceColumnaObjetivo - indiceColumnaNecromante);
        int distanciaMaxima = Math.max(distanciaFila, distanciaColumna);
        boolean cerca = distanciaMaxima > 0 && distanciaMaxima <= 2;

        if (cerca) {
            throw new JuegoException("El ataque a través de Zombie se usa contra enemigos que no estén a 1 ni a 2 casillas del Necrómante.");
        }

        Necromante necromante = (Necromante) piezaNecromante;
        Zombie invocacionZombie = (Zombie) piezaZombie;
        ResultadoDanio resultadoAccion = necromante.ordenarAtaqueZombie(invocacionZombie, piezaObjetivo);
        String mensaje = "El Necrómante ordenó un ataque a través de su Zombie. " + construirMensajeAtaque(piezaObjetivo, resultadoAccion, renglonObjetivo, indiceColumnaObjetivo);
        resolverDespuesDelAtaque(renglonObjetivo, indiceColumnaObjetivo, piezaObjetivo, resultadoAccion);
        return mensaje;
    }

    public String retirarse(Jugador jugadorRetirado) throws JuegoException {
        validarPartidaActiva();
        if (jugadorRetirado == null || (jugadorRetirado != usuarioBlanco && jugadorRetirado != usuarioNegro)) {
            throw new JuegoException("El jugador indicado no pertenece a esta partida.");
        }

        Jugador jugadorGanador = jugadorRetirado == usuarioBlanco ? usuarioNegro : usuarioBlanco;
        String mensaje = jugadorRetirado.getUsuario() + " se ha retirado. ¡Felicidades, " + jugadorGanador.getUsuario() + ", has ganado " + PUNTOS_POR_VICTORIA + " puntos!";
        finalizarPartida(jugadorGanador, jugadorRetirado, mensaje);
        return mensaje;
    }

    private Pieza validarPiezaOrigen(int renglon, int indiceColumna) throws JuegoException {
        if (tipoHabilitado == null) {
            throw new JuegoException("Primero debes girar la ruleta y obtener un tipo de pieza válido.");
        }

        Pieza elementoPieza = tableroActual.getPieza(renglon, indiceColumna);
        if (elementoPieza == null) {
            throw new JuegoException("La casilla de origen está vacía.");
        }
        if (elementoPieza.getColor() != ladoEnTurno) {
            throw new JuegoException("La pieza seleccionada no pertenece al jugador del turno actual.");
        }
        if (elementoPieza.getTipo() != tipoHabilitado) {
            throw new JuegoException("La ruleta solo permite usar una pieza de tipo " + tipoHabilitado.getNombre() + ".");
        }
        return elementoPieza;
    }

    private Pieza validarObjetivoEnemigo(int renglon, int indiceColumna) throws JuegoException {
        Pieza piezaObjetivo = tableroActual.getPieza(renglon, indiceColumna);
        if (piezaObjetivo == null) {
            throw new JuegoException("La casilla destino está vacía.");
        }
        if (piezaObjetivo.getColor() == ladoEnTurno) {
            throw new JuegoException("No puedes atacar una pieza propia.");
        }
        return piezaObjetivo;
    }

    private String construirMensajeAtaque(Pieza piezaObjetivo, ResultadoDanio resultadoAccion, int renglonDestino, int indiceColumnaDestino) {
        if (resultadoAccion.isDestruida()) {
            if (piezaObjetivo.getTipo() == TipoPieza.NECROMANTE) {
                return "Se destruyó la pieza Necrómante del jugador "
                        + getJugadorPorColor(piezaObjetivo.getColor()).getUsuario()
                        + ". Todos sus Zombies fueron destruidos automáticamente.";
            }
            return "Se destruyó la pieza " + piezaObjetivo.getTipo().getNombre()
                    + " del jugador " + getJugadorPorColor(piezaObjetivo.getColor()).getUsuario() + ".";
        }
        return "Se atacó la pieza " + piezaObjetivo.getTipo().getNombre() + " y se le quitaron " + resultadoAccion.getDanoTotal() + " puntos; le quedan " + resultadoAccion.getEscudoRestante() + " puntos de escudo y " + resultadoAccion.getVidaRestante() + " de vida.";
    }

    private void resolverDespuesDelAtaque(int renglonDestino, int indiceColumnaDestino, Pieza piezaObjetivo, ResultadoDanio resultadoAccion) throws JuegoException {
        if (resultadoAccion.isDestruida()) {
            tableroActual.eliminarPieza(renglonDestino, indiceColumnaDestino);

            
            if (piezaObjetivo.getTipo() == TipoPieza.NECROMANTE) {
                int zombiesEliminados = tableroActual.eliminarZombiesDeColor(piezaObjetivo.getColor());
                if (zombiesEliminados > 0) {
                }
            }
        }

        ColorBando ladoRival = ladoEnTurno == ColorBando.BLANCO ? ColorBando.NEGRO : ColorBando.BLANCO;
        if (tableroActual.contarPiezas(ladoRival) == 0) {
            Jugador jugadorGanador = getJugadorPorColor(ladoEnTurno);
            Jugador jugadorPerdedor = getJugadorPorColor(ladoRival);
            String mensaje = jugadorGanador.getUsuario() + " venció a " + jugadorPerdedor.getUsuario()
                    + ". ¡Felicidades, has ganado " + PUNTOS_POR_VICTORIA + " puntos!";
            finalizarPartida(jugadorGanador, jugadorPerdedor, mensaje);
        } else {
            terminarAccion();
        }
    }

    private void terminarAccion() {
        cambiarTurno();
    }

    private void cambiarTurno() {
        ladoEnTurno = ladoEnTurno == ColorBando.BLANCO ? ColorBando.NEGRO : ColorBando.BLANCO;
        tipoHabilitado = null;
        girosUsados = 0;
    }

    private int getGirosPermitidosTurnoActual() {
        int piezasRestantes = tableroActual.contarPiezasPrincipales(ladoEnTurno);
        int perdidas = 6 - piezasRestantes;
        return ruleta.calcularGirosPermitidos(perdidas);
    }

    private void finalizarPartida(Jugador jugadorGanador, Jugador jugadorPerdedor, String mensaje) throws JuegoException {
        activa = false;
        mensajeFinal = mensaje;
        jugadorGanador.sumarPuntos(PUNTOS_POR_VICTORIA);
        historial.agregarRegistro(new RegistroPartida(usuarioBlanco.getUsuario(), usuarioNegro.getUsuario(), jugadorGanador.getUsuario(), jugadorPerdedor.getUsuario(), mensaje));
    }

    private void validarPartidaActiva() throws JuegoException {
        if (!activa) {
            throw new JuegoException("La partida ya finalizó.");
        }
    }

    public Jugador getJugadorPorColor(ColorBando ladoEquipo) {
        return ladoEquipo == ColorBando.BLANCO ? usuarioBlanco : usuarioNegro;
    }

    public Jugador getJugadorTurnoActual() {
        return getJugadorPorColor(ladoEnTurno);
    }

    public Tablero getTablero() {
        return tableroActual;
    }

    public ColorBando getTurnoActual() {
        return ladoEnTurno;
    }

    public TipoPieza getTipoHabilitado() {
        return tipoHabilitado;
    }

    public int getGirosUsados() {
        return girosUsados;
    }

    public int getGirosPermitidos() {
        return getGirosPermitidosTurnoActual();
    }

    public boolean isActiva() {
        return activa;
    }

    public String getMensajeFinal() {
        return mensajeFinal;
    }

    public Jugador getJugador1() {
        return usuarioBlanco;
    }

    public Jugador getJugador2() {
        return usuarioNegro;
    }
}
