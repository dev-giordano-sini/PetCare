# PetCare

PetCare è una semplice applicazione da console per gestire le attività di un centro dedicato alla cura degli animali.

## Funzionalità

L'applicazione permette di:

- registrare un animale e i dati del suo proprietario;
- programmare visite veterinarie, vaccinazioni e appuntamenti di toelettatura;
- consultare animali registrati, appuntamenti futuri e storico degli appuntamenti;
- generare report sugli appuntamenti della settimana successiva e sulle visite scadute;
- salvare e ricaricare automaticamente i dati in formato JSON.

I dati sono conservati nei file `pets.json` e `appointments.json`. L'estensione è rimasta `.txt` per compatibilità con il progetto originale, ma il contenuto dei file è JSON.

## Tecnologie utilizzate

- **Java 21 LTS** per la logica dell'applicazione;
- **Maven** per la gestione del progetto e delle dipendenze;
- **Jackson** per leggere e scrivere i dati JSON, incluse date e orari Java;
- **JUnit 5** per i test automatici;
- API Java NIO per la scrittura tramite file temporanei e la sostituzione sicura dei file di dati.

## Compilazione e test

È necessario avere installati JDK 21 o successivo e Maven.

```bash
mvn test
```

L'applicazione può essere avviata eseguendo la classe `com.example.Main` dal proprio IDE.
