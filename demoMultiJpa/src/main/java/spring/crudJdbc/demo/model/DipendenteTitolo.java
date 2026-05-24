package spring.crudJdbc.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dipendenti_titoli")
public class DipendenteTitolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "titolo_studio_id")
    private TitoloStudio titoloStudio;

    public DipendenteTitolo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TitoloStudio getTitoloStudio() { return titoloStudio; }
    public void setTitoloStudio(TitoloStudio titoloStudio) { this.titoloStudio = titoloStudio; }
}