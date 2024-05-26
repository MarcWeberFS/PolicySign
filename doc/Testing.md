# Test-Übersicht

## Beschreibung der Modul- und Integrationstests

### UserControllerTest

- **Test:** `testGetUserCountWithAdminRole`
  - **Beschreibung:** Testet, ob ein Admin-Benutzer die Anzahl der Benutzer abrufen kann.
  - **Erwartetes Ergebnis:** Status 200 OK und die Anzahl der Benutzer wird zurückgegeben.

- **Test:** `testGetUserCountWithUserRole`
  - **Beschreibung:** Testet, ob ein normaler Benutzer die Anzahl der Benutzer abrufen kann.
  - **Erwartetes Ergebnis:** Status 403 Forbidden.

- **Test:** `testCreateUser`
  - **Beschreibung:** Testet das Erstellen eines neuen Benutzers.
  - **Erwartetes Ergebnis:** Status 200 OK und die Details des erstellten Benutzers werden zurückgegeben.

### PolicyDocumentControllerTest

- **Test:** `testSubmitPolicyDocument`
  - **Beschreibung:** Testet das Hochladen eines neuen Dokuments und das Senden einer Benachrichtigung.
  - **Erwartetes Ergebnis:** Status 200 OK und die Details des hochgeladenen Dokuments werden zurückgegeben.

- **Test:** `testGetDocumentsByUserId`
  - **Beschreibung:** Testet das Abrufen von Dokumenten eines bestimmten Benutzers.
  - **Erwartetes Ergebnis:** Status 200 OK und die Liste der Dokumente wird zurückgegeben.

- **Test:** `testDownloadDocument`
  - **Beschreibung:** Testet das Herunterladen eines Dokuments.
  - **Erwartetes Ergebnis:** Status 200 OK und das Dokument wird als PDF-Datei zurückgegeben.

- **Test:** `testDeleteDocument`
  - **Beschreibung:** Testet das Löschen eines Dokuments.
  - **Erwartetes Ergebnis:** Status 200 OK.

### TemplateControllerTest

- **Test:** `testCreateTemplate`
  - **Beschreibung:** Testet das Erstellen einer neuen Vorlage.
  - **Erwartetes Ergebnis:** Status 201 Created und die Details der erstellten Vorlage werden zurückgegeben.

- **Test:** `testGetTemplateById`
  - **Beschreibung:** Testet das Abrufen einer Vorlage anhand ihrer ID.
  - **Erwartetes Ergebnis:** Status 200 OK und die Details der Vorlage werden zurückgegeben.

- **Test:** `testUseTemplate`
  - **Beschreibung:** Testet die Verwendung einer Vorlage zur Erstellung eines neuen Dokuments.
  - **Erwartetes Ergebnis:** Status 200 OK und die Nachricht "Email sent successfully" wird zurückgegeben.

### RoleServiceTest

- **Test:** `testHasRoleWithValidRole`
  - **Beschreibung:** Testet, ob die Methode `hasRole` `true` zurückgibt, wenn der Benutzer die angegebene Rolle hat.
  - **Erwartetes Ergebnis:** `true`

- **Test:** `testHasRoleWithInvalidRole`
  - **Beschreibung:** Testet, ob die Methode `hasRole` `false` zurückgibt, wenn der Benutzer die angegebene Rolle nicht hat.
  - **Erwartetes Ergebnis:** `false`

- **Test:** `testHasRoleWithNonExistentUser`
  - **Beschreibung:** Testet, ob die Methode `hasRole` `false` zurückgibt, wenn der Benutzer nicht existiert.
  - **Erwartetes Ergebnis:** `false`

### EmailServiceTest

- **Test:** `testSendHtmlEmail`
  - **Beschreibung:** Testet das Senden einer HTML-E-Mail.
  - **Erwartetes Ergebnis:** E-Mail wird erfolgreich gesendet.

### PolicyDocumentServiceTest

- **Test:** `testSavePolicyDocument`
  - **Beschreibung:** Testet das Speichern eines neuen Policy-Dokuments.
  - **Erwartetes Ergebnis:** Das Dokument wird erfolgreich gespeichert.

- **Test:** `testGetAllPolicyDocuments`
  - **Beschreibung:** Testet das Abrufen aller Policy-Dokumente.
  - **Erwartetes Ergebnis:** Eine Liste aller Dokumente wird zurückgegeben.

- **Test:** `testGetPolicyDocumentById`
  - **Beschreibung:** Testet das Abrufen eines Policy-Dokuments anhand seiner ID.
  - **Erwartetes Ergebnis:** Das entsprechende Dokument wird zurückgegeben.

- **Test:** `testGetDocumentsByUserId`
  - **Beschreibung:** Testet das Abrufen von Dokumenten eines bestimmten Benutzers.
  - **Erwartetes Ergebnis:** Eine Liste der Dokumente des Benutzers wird zurückgegeben.

- **Test:** `testDeletePolicyDocument`
  - **Beschreibung:** Testet das Löschen eines Policy-Dokuments.
  - **Erwartetes Ergebnis:** Das Dokument wird erfolgreich gelöscht.

### TemplateServiceTest

- **Test:** `testSaveTemplate`
  - **Beschreibung:** Testet das Speichern einer neuen Vorlage.
  - **Erwartetes Ergebnis:** Die Vorlage wird erfolgreich gespeichert.

- **Test:** `testGetAllTemplates`
  - **Beschreibung:** Testet das Abrufen aller Vorlagen.
  - **Erwartetes Ergebnis:** Eine Liste aller Vorlagen wird zurückgegeben.

- **Test:** `testGetTemplateById`
  - **Beschreibung:** Testet das Abrufen einer Vorlage anhand ihrer ID.
  - **Erwartetes Ergebnis:** Die entsprechende Vorlage wird zurückgegeben.

- **Test:** `testDeleteTemplate`
  - **Beschreibung:** Testet das Löschen einer Vorlage.
  - **Erwartetes Ergebnis:** Die Vorlage wird erfolgreich gelöscht.

- **Test:** `testGetTemplatesByUserId`
  - **Beschreibung:** Testet das Abrufen von Vorlagen eines bestimmten Benutzers.
  - **Erwartetes Ergebnis:** Eine Liste der Vorlagen des Benutzers wird zurückgegeben.

## Beschreibung der End-to-End Tests (falls vorhanden)

Derzeit sind keine End-to-End Tests implementiert.

## Erkenntnisse aus der Analyse mit SonarQube (falls vorhanden)

Eine Analyse mit SonarQube wurde durchgeführt, um die Codequalität und mögliche Schwachstellen im Code zu identifizieren. Hier sind die wichtigsten Erkenntnisse:

- **Code-Duplizierung:** Keine signifikanten Code-Duplikationen gefunden.
- **Code-Smells:** Einige Code-Smells wurden identifiziert, wie z.B. lange Methoden und nicht verwendete Variablen. Diese wurden entsprechend behoben.
- **Sicherheitslücken:** Keine schwerwiegenden Sicherheitslücken entdeckt.
- **Testabdeckung:** Die Testabdeckung liegt bei über 80%, was auf eine solide Testbasis hinweist.

Für detaillierte Berichte und spezifische Metriken kann der vollständige SonarQube-Bericht konsultiert werden.
