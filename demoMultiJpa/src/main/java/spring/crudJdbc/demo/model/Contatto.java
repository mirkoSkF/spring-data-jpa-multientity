package spring.crudJdbc.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contatti")
public class Contatto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_contatto_id")
    private TipoContatto tipoContatto;

    private String valore;

    public Contatto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoContatto getTipoContatto() { return tipoContatto; }
    public void setTipoContatto(TipoContatto tipoContatto) { this.tipoContatto = tipoContatto; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }
}