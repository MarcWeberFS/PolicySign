# Fazit

## Stand der Implementation, Diskussion der Ergebnisse

Die Implementation unserer digitalen Signaturlösung ist weit fortgeschritten und umfasst folgende Hauptkomponenten:

1. **Frontend**: Mit Svelte entwickelt, bietet das Frontend eine simple Oberfläche zur Verwaltung von Dokumenten, Vorlagen und Signaturen. Die Anwendung ist responsiv und bietet eine intuitive Nutzererfahrung. Ich hatte hier zu wenig Zeit um das Frontend weiter zu entwickeln, aber die Hauptfeatures wurden umgesetzt.
2. **Backend**: Das Backend wurde mit Spring Boot realisiert und stellt die notwendigen API-Endpunkte zur Verfügung. Es ermöglicht das Speichern und Abrufen von Dokumenten, das Versenden von E-Mail-Benachrichtigungen und die Verwaltung der Benutzer.
3. **Integration von Drittsystemen**: Auth0 für die Authentifizierung, AWS S3 für die Dokumentenspeicherung und JavaMailSender für den E-Mail-Versand wurden erfolgreich integriert. AWS S3 habe ich zwei verschiedene Buckets erstellt, eines für die Dokumente und das andere für Templates. Während der Umsetzung wollte ich mich für einen anderen Approach mittels Uploadthing integrieren, jedoch ist der Support für Svelte und Java Anwendungen sehr klein. Bei Fragen in der Community habe ich keine Hilfe erhalten. Deswegen habe ich mich am Ende für AWS S3 entschieden, weil die Dokumentation viel umfänglicher ist.

### Diskussion der Ergebnisse

Die bisherige Implementation deckt alle grundlegenden Funktionen ab, die für eine voll funktionsfähige digitale Signaturlösung erforderlich sind. Die Benutzer können Dokumente hochladen, signieren und den Status ihrer Dokumente überwachen. Zudem können Vorlagen erstellt und wiederverwendet werden, was den Prozess effizienter gestaltet. 

Einige wichtige Aspekte, die besonders hervorzuheben sind:
- **Sicherheitsaspekte**: Durch die Integration von Auth0 wird eine sichere Benutzerverwaltung gewährleistet. Die Speicherung der Dokumente in AWS S3 ermöglicht eine skalierbare und sichere Datenhaltung. Beim signieren ist die Sicherheit ungenügend, es muss noch ein weiteres feature wie zum Beispiel ein Passwort mitgesendet werden, damit wirklich nur der Benutzer der das Email erhalten hat unterzeichnen kann.
- **Benutzerfreundlichkeit**: Die Anwendung ist leicht verständlich und einfach zu bedienen. Das responsive Design stellt sicher, dass die Anwendung auf verschiedenen Geräten gut funktioniert.
- **Kostenmodell**: Das Pay-As-You-Go Modell macht die Anwendung für verschiedene Benutzergruppen attraktiv, insbesondere für kleine Unternehmen und Start-ups, die nur gelegentlich Dokumente signieren müssen. Das Pay-As-You-Go Modell ist noch nicht implementiert, aber die Pricing page gibt eine Vorahnung wie es umgesetzt werden muss.

## Nächste Schritte

Obwohl die grundlegenden Funktionen implementiert sind, gibt es noch mehrere Bereiche, die weiterentwickelt werden können:

1. **Erweiterung der API**: Zusätzliche API-Endpunkte könnten entwickelt werden, um mehr Flexibilität und Funktionalität für Pro-Benutzer bereitzustellen.
2. **Erweiterte Sicherheitsmassnahmen**: Weitere Sicherheitsmassnahmen wie Zwei-Faktor-Authentifizierung und erweiterte Verschlüsselungstechniken könnten implementiert werden.
3. **Optimierung der Benutzeroberfläche**: Basierend auf Benutzerfeedback könnten weitere Verbesserungen an der Benutzeroberfläche vorgenommen werden, um die Nutzererfahrung weiter zu verbessern.
4. **Internationalisierung**: Die Anwendung könnte für den internationalen Markt angepasst und in mehrere Sprachen übersetzt werden.
5. **Automatisierte Tests**: Implementierung weiterer automatisierter Tests zur Sicherstellung der Softwarequalität und zur Erleichterung zukünftiger Wartungsarbeiten.
6. **Integration mit anderen Plattformen**: Erhöhung der Kompatibilität mit anderen Dokumentenverwaltungs- und Signatursystemen.

## Persönliches Fazit

Die Entwicklung dieser digitalen Signaturlösung war eine äusserst lehrreiche und bereichernde Erfahrung. Durch die Arbeit an diesem Projekt konnte ich meine Fähigkeiten in verschiedenen Bereichen der Softwareentwicklung erheblich erweitern:

- **Technische Fähigkeiten**: Ich habe tiefgehende Kenntnisse in modernen Frontend-Technologien wie Svelte und Backend-Technologien wie Spring Boot erworben. Zudem habe ich wertvolle Erfahrungen in der Integration von Cloud-Diensten und Authentifizierungssystemen gesammelt. Vorallem noch die Auseinandersetzung mit Azure war sehr gut für mich, da ich nur mit AWS bis jetzt gearbeitet habe.
- **Projektmanagement**: Die Planung und Durchführung eines solch umfassenden Projekts hat meine Fähigkeiten im Projektmanagement und in der Selbstorganisation verbessert. Auch wenn das Programm sehr einfach umzusetzen scheint, habe ich 3 Tage sicherlich weiter arbeiten können und das Ergebnis noch ein bisschen zu verbessern.
- **Problemlösung**: Die Bewältigung der verschiedenen Herausforderungen, die während der Entwicklung auftraten, hat meine Problemlösungsfähigkeiten gestärkt. 

Insgesamt bin ich mit dem aktuellen Stand der Implementation sehr zufrieden. Die Anwendung erfüllt die gestellten Anforderungen und bietet eine solide Grundlage für zukünftige Erweiterungen und Verbesserungen. Ich freue mich darauf, das Projekt weiterzuführen und die nächsten Schritte in der Entwicklung zu realisieren.
