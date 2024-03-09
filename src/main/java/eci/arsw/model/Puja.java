package eci.arsw.model;

import javax.persistence.*;

@Entity
public class Puja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double monto;
    private String usuario;
    private Long subastaId;

    // Getters y setters

    public Puja(Long id, double monto, String usuario, Long subastaId) {
        this.id = id;
        this.monto = monto;
        this.usuario = usuario;
        this.subastaId = subastaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getSubastaId() {
        return subastaId;
    }

    public void setSubastaId(Long subastaId) {
        this.subastaId = subastaId;
    }
}
