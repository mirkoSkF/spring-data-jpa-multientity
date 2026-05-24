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

    // Relazione N-to-1 con Ruolo (Tabella dizionario: nessuna cascata per evitare sovrascritture dei ruoli esistenti)
    @ManyToOne
    @JoinColumn(name = "ruolo_id")
    private RuoloAziendale ruolo;

    // Relazione 1-to-1 con Account (con cascade totale e orphanRemoval per sincronizzare la cancellazione dell'account)
    @OneToOne(mappedBy = "dipendente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;

    // Relazione 1-to-N con i Contatti (le righe di contatto sono dipendenti logici del singolo dipendente)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dipendente_id")
    private List<Contatto> contatti = new ArrayList<>();

    // CORRETTO: Rimosso CascadeType.PERSIST per evitare l'errore "Detached entity passed to persist" 
    // quando si associano titoli di studio preesistenti (tabelle dizionario/anagrafica)
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "dipendenti_titoli",
        joinColumns = @JoinColumn(name = "dipendente_id"),
        inverseJoinColumns = @JoinColumn(name = "titolo_studio_id")
    )
    private List<TitoloStudio> titoliStudio = new ArrayList<>();

    // Costruttore vuoto (Obbligatorio per le specifiche JPA)
    public Dipendente() {}

    // Costruttore pieno completo
    public Dipendente(Long id, String nome, String cognome, String codiceFiscale, String genere, 
                      LocalDate dataDiNascita, String luogoNascita, RuoloAziendale ruolo, 
                      Account account, List<Contatto> contatti, List<TitoloStudio> titoliStudio) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.genere = genere;
        this.dataDiNascita = dataDiNascita;
        this.luogoNascita = luogoNascita;
        this.ruolo = ruolo;
        this.setAccount(account); // Sfrutta il setter per mantenere la coerenza bidirezionale
        this.contatti = contatti;
        this.titoliStudio = titoliStudio;
    }

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
            account.setDipendente(this); // Garantisce la consistenza della relazione bidirezionale 1:1
        }
    }

    public List<Contatto> getContatti() { return contatti; }
    public void setContatti(List<Contatto> contatti) { this.contatti = contatti; }

    public List<TitoloStudio> getTitoliStudio() { return titoliStudio; }
    public void setTitoliStudio(List<TitoloStudio> titoliStudio) { this.titoliStudio = titoliStudio; }
}