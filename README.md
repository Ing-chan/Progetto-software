# PROVA FINALE DI INGEGNERIA DEL SOFTWARE – A. A. 2023 – 2024

L’obiettivo principale del progetto è quello di implementare il gioco da tavola Codex Naturalis utilizzando l’architettura MVC (Model View e Controller).  
Il risultato finale rispetta integralmente le regole del gioco e offre la possibilità di interagire con esso attraverso un'interfaccia a riga di comando (CLI) o tramite grafica (GUI). Per quanto riguarda la comunicazione via rete, sono state utilizzate sia le socket che RMI.

## FUNZIONALITÀ SVILUPPATE

- Regole complete
- CLI
- GUI
- Socket
- RMI

## FUNZIONALITÀ AGGIUNTIVE SVILUPPATE

- Partite multiple
- Chat

## ESECUZIONE DEI JAR

### CLIENT

Il client può essere eseguito in due modalità: CLI e GUI; il giocatore può scegliere la modalità di gioco tramite un comando apposito all'inizio dell'esecuzione del Jar.

#### CLI

Per eseguire il client in modalità testuale, è necessario eseguire il seguente comando da terminale, specificando che si vuole eseguire l'interfaccia a riga di comando:
```bash
java -jar client.jar -cli
```

#### GUI

Per poter eseguire MyShelfie con interfaccia grafica è sufficiente digitare il seguente comando:
```bash
java -jar client.jar -gui
```

### SERVER

L'esecuzione del server avviene attraverso il seguente comando, in cui si specifica il percorso del file di configurazione:
```bash
java -jar server.jar [server IP]
```

## COMPONENTI DEL GRUPPO

- Isabella Argentiero
- Lorenzo Castellana
- Samuele Celedi
- Matteo Montanelli
```
