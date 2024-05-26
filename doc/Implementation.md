# Implementation

## Beschreibung des Frontends mit Screenshots der fertigen Applikation

Das Frontend unserer Anwendung wurde mit Svelte entwickelt und bietet eine intuitive Benutzeroberfläche, die den Benutzern ermöglicht, Dokumente hochzuladen, zu signieren, Templates zu erstellen und den Status ihrer Dokumente zu überwachen. Die Anwendung ist responsiv und für verschiedene Bildschirmgrößen optimiert. 

### Hauptfunktionen:
1. **Dokumentenübersicht**: Benutzer können eine Liste ihrer hochgeladenen Dokumente einsehen.
2. **Dokument hochladen**: Ermöglicht das Hochladen neuer Dokumente zur Signatur.
3. **Dokument signieren**: Benutzer können Dokumente elektronisch unterschreiben.
4. **Templates erstellen**: Benutzer können Templates für wiederkehrende Dokumente erstellen.
5. **API-Zugang (Pro-Version)**: Erlaubt den Zugriff auf API-Endpunkte für erweiterte Integrationen.
5. **Adming zugang**: Erlaubt den Zugriff die Adminpage, wo Dokumente heruntergeladen, gelöscht und Metriken über die Webseite erfasst sind.

### Screenshots der Applikation:

#### 1. Dokumentenübersicht
![Dokumentenübersicht](screenshots/dokumentenübersicht.PNG)

#### 2. Dokument hochladen
![Dokument hochladen 1](screenshots/dokument-hochladen1.PNG)
![Dokument hochladen 2](screenshots/dokument-hochladen2.PNG)


#### 3. Dokument signieren
![Dokument signieren](screenshots/dokument-signieren.PNG)

#### 4. Templates erstellen
![Templates erstellen 1](screenshots/templates-erstellen1.PNG)
![Templates erstellen 2](screenshots/templates-erstellen2.PNG)
#### 5. API-Zugang (Pro-Version)
![API-Zugang](screenshots/api-zugang.PNG)

#### 5. API-Zugang (Pro-Version)
![Adminpage](screenshots/admin.PNG)

## Klassendiagramm mit dem technischen Datenmodell (DTOs, DAOs) und deren Verwendungszweck

Das Klassendiagramm stellt die Struktur der Datenobjekte (DTOs) und Datenzugriffsobjekte (DAOs) dar, die in der Anwendung verwendet werden.

### Klassendiagramm
![Klassendiagramm](klassendiagramm.png)

### Beschreibung der Klassen

1. **User**:
   - **Beschreibung**: Repräsentiert einen Benutzer der Anwendung.
   - **Attribute**: `id`, `username`, `email`, `roles`
   - **Verwendungszweck**: Verwaltung der Benutzerinformationen und deren Rollen.

2. **PolicyDocument**:
   - **Beschreibung**: Repräsentiert ein zu signierendes Dokument.
   - **Attribute**: `id`, `title`, `description`, `url`, `status`, `creationDate`, `updateDate`, `xSignature`, `ySignature`, `signatureWidth`, `signaturePage`, `userId`, `signedByEmail`
   - **Verwendungszweck**: Speicherung und Verwaltung der Dokumentinformationen.

3. **Template**:
   - **Beschreibung**: Repräsentiert ein Dokumenttemplate.
   - **Attribute**: `id`, `url`, `xSignature`, `ySignature`, `signatureWidth`, `userId`, `title`, `description`
   - **Verwendungszweck**: Verwaltung der Templates für wiederkehrende Dokumente.

4. **Signatory**:
   - **Beschreibung**: Repräsentiert einen Unterzeichner eines Dokuments.
   - **Attribute**: `id`, `signed`, `signDate`, `documentId`, `signatureDataUrl`
   - **Verwendungszweck**: Speicherung der Unterschrifteninformationen.

## Aufgaben und Funktionen eingebundener Drittsysteme

### Eingebundene Drittsysteme:

1. **Auth0**:
   - **Beschreibung**: Authentifizierungs- und Autorisierungsdienst.
   - **Funktion**: Verwaltung der Benutzeranmeldung und -authentifizierung sowie der Zugriffskontrollen.

2. **AWS S3**:
   - **Beschreibung**: Cloud-Speicherdienst von Amazon Web Services.
   - **Funktion**: Speicherung und Abruf der hochgeladenen Dokumente und Templates.

3. **Spring Boot**:
   - **Beschreibung**: Framework für die Entwicklung von Java-basierten Webanwendungen.
   - **Funktion**: Bereitstellung der Backend-Logik, API-Endpunkte und Datenbankintegration.

4. **JavaMailSender**:
   - **Beschreibung**: Bibliothek zum Senden von E-Mails in Java-Anwendungen.
   - **Funktion**: Versand von Benachrichtigungen und Dokumenten an die Benutzer per E-Mail.

5. **PDFBox**:
   - **Beschreibung**: Open-Source-Bibliothek zum Erstellen und Bearbeiten von PDF-Dokumenten.
   - **Funktion**: Hinzufügen von Signaturen zu PDF-Dokumenten.

### Funktionen der Drittsysteme:

- **Auth0**: 
  - Benutzerregistrierung und -anmeldung
  - JWT-Token-Generierung und -Validierung
  - Rollenbasierte Zugriffskontrolle

- **AWS S3**: 
  - Sicheres Hochladen von Dokumenten
  - Abrufen und Herunterladen von gespeicherten Dokumenten
  - Verwaltung von Dokumenten-URLs

- **Spring Boot**: 
  - Implementierung der Geschäftslogik
  - Bereitstellung von RESTful API-Endpunkten
  - Datenbankzugriff und -manipulation

- **JavaMailSender**: 
  - Erstellen und Versenden von E-Mail-Benachrichtigungen
  - Anhängen von signierten Dokumenten an E-Mails

- **PDFBox**: 
  - Hinzufügen von grafischen Signaturen zu PDF-Dokumenten
  - Speichern und Aktualisieren von PDF-Dateien
