package spring.crudJdbc.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipi_contatti")
public class TipoContatto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denominazione;

    public TipoContatto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDenominazione() { return denominazione; }
    public void setDenominazione(String denominazione) { this.denominazione = denominazione; }
}