package spring.crudJdbc.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dipendenti")
public class Dipendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;

    @Column(name = "codice_fiscale", unique = true, nullable = false, length = 16)
    private String codiceFiscale;

    private String genere;

    @Column(name = "data_di_nascita")
    private LocalDate dataDiNascita;

    @Column(name = "luogo_nascita")
    private String luogoNascita;

    // Relazione N-to-1 con Ruolo
    @ManyToOne
    @JoinColumn(name = "ruolo_id")
    private RuoloAziendale ruolo;

    // Relazione 1-to-1 con Account (con cascade per salvare/aggiornare insieme al dipendente)
    @OneToOne(mappedBy = "dipendente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    // Relazione 1-to-N con l'entità di giunzione Contatti
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dipendente_id")
    private List<Contatto> contatti = new ArrayList<>();

    // Relazione 1-to-N verso l'entità di giunzione dei Titoli di Studio
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dipendente_id")
    private List<DipendenteTitolo> titoliStudio = new ArrayList<>();

    public Dipendente() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }

    public LocalDate getDataDiNascita() { return dataDiNascita; }
    public void setDataDiNascita(LocalDate dataDiNascita) { this.dataDiNascita = dataDiNascita; }

    public String getLuogoNascita() { return luogoNascita; }
    public void setLuogoNascita(String luogoNascita) { this.luogoNascita = luogoNascita; }

    public RuoloAziendale getRuolo() { return ruolo; }
    public void setRuolo(RuoloAziendale ruolo) { this.ruolo = ruolo; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) {
        this.account = account;
        if (account != null) {
            account.setDipendente(this);
        }
    }

    public List<Contatto> getContatti() { return contatti; }
    public void setContatti(List<Contatto> contatti) { this.contatti = contatti; }

    public List<DipendenteTitolo> getTitoliStudio() { return titoliStudio; }
    public void setTitoliStudio(List<DipendenteTitolo> titoliStudio) { this.titoliStudio = titoliStudio; }
}