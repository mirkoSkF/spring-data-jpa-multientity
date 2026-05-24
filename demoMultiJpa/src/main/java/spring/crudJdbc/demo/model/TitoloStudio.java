package spring.crudJdbc.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "titoli_studio")
public class TitoloStudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descrizione;

    public TitoloStudio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
}