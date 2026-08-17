package vampirewargame;

import java.util.Calendar;

public class RegistroPartida {

    private final String usuarioBlanco;
    private final String usuarioNegro;
    private final String jugadorGanador;
    private final String jugadorPerdedor;
    private final String mensaje;
    private final Calendar fecha;

    public RegistroPartida(String usuarioBlanco, String usuarioNegro, String jugadorGanador, String jugadorPerdedor, String mensaje) {
        this.usuarioBlanco = usuarioBlanco;
        this.usuarioNegro = usuarioNegro;
        this.jugadorGanador = jugadorGanador;
        this.jugadorPerdedor = jugadorPerdedor;
        this.mensaje = mensaje;
        fecha = Calendar.getInstance();
    }

    public boolean participo(String usuario) {
        return usuarioBlanco.equalsIgnoreCase(usuario) || usuarioNegro.equalsIgnoreCase(usuario);
    }

    public String getFechaTexto() {
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int anio = fecha.get(Calendar.YEAR);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        return String.format("%02d/%02d/%04d %02d:%02d", dia, mes, anio, hora, minuto);
    }

    public String getJugador1() {
        return usuarioBlanco;
    }

    public String getJugador2() {
        return usuarioNegro;
    }

    public String getGanador() {
        return jugadorGanador;
    }

    public String getPerdedor() {
        return jugadorPerdedor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Calendar getFecha() {
        return (Calendar) fecha.clone();
    }

    @Override
    public String toString() {
        return getFechaTexto() + " - " + mensaje;
    }
}
