package eci.arsw.model;

import javax.persistence.*;


@Entity
public class Apuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private double cantidad;
    private String evento;

    // getters y setters


    public Apuesta(Long id, String usuario, double cantidad, String evento) {
        this.id = id;
        this.usuario = usuario;
        this.cantidad = cantidad;
        this.evento = evento;
    }

    public Apuesta() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }
}
