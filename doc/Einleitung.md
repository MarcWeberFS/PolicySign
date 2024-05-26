#  EXPLORE BOARD: PolicySign

## TRENDS & TECHNOLOGIE
- Trend ist gross von Papierbezogenen Dokumenten Weg zu kommen und durch digitaler Lösung unterschriften einzuholen. Viele alte und kleinere Finanzfirmen wie zum Beispiel Versicherungsbrokers arbeiten immernoch Papierbasiert.

## POTENTIELLE PARTNER & WETTBEWERB
### Partner
- Banken, Versicherungen, Finanzielle Instituten, Startups

### Wettbewerb
- DocuSign, AdobeSign, Swisscom Sign

## FAKTEN
- Es gibt keine bekannte Lösung mit dem Pay as you go Modell. Sprich für jeden Envelope würden Kosten entstehen, welche Ende Monat dem Benutzer via Rechnung zugestellt werden.

## POTENZIALFELDER
- Via API auf Ihrer eigenen Webseite Unterschriften einholen.
- Hohe Subscription Kosten bei Docusign, Adobe etc
- Weg von Papierbezogene Arbeit
- Sicherstellen, dass die digitale Signatur rechtlich anerkennt ist.

## USER
- Künftige User sind KMU und Startups, die nicht das Budget haben riesige Subscription Costs auf sich zu nehmen, für ein Ungetestetes Produkt von ihnen. Da der Setup mit API aufwendig wird, gibt es einen "vendor lock in" sprich der Wechsel zu eineme anderen Provider ist mühsam.

## BEDÜRFNISSE
- Pay as you go Envelopes zum Signieren verschicken.
- Signatur Rechtlich verhalten.
- Simples Aufsetzen von neuen Dokumenten zum signieren.

## ERKENNTNISSE
- Es gibt keinen grossen Provider von online Signaturen Diensten von Amerika oder Schweiz, welche ein Pay as you go Modell für die Zahlung anbieten.

## TOUCHPOINTS
- Template (Policy oder Dokument) hochladen auf der Website.
- Email erhalten nachdem das Dokument zu signieren ist mit einem Link.
- Email erhalten nachdem das Dokument signiert ist.

## WIE KÖNNEN WIR?
- Wie können wir KMU und Startups eine kostengünstige, digitale Signaturlösung anbieten, die ein Pay-as-you-go-Preismodell verwendet?
- Wie können wir den Prozess des Hochladens, Versendens und Signierens von Dokumenten so einfach und intuitiv wie möglich gestalten, um die Hürden für den Wechsel von papierbasierten zu digitalen Lösungen zu minimieren?
- Wie können wir sicherstellen, dass die digitalen Signaturen, die über unsere Plattform eingeholt werden, rechtlich anerkannt und verbindlich sind?
- Wie können wir einen nahtlosen Integrationsprozess für unsere API anbieten, der es Unternehmen ermöglicht, digitale Signaturen einfach in ihre bestehenden Websites und Systeme einzubinden, ohne einen umfangreichen technischen Aufwand?


#  CREATE BOARD: PolicySign

![Use case diagram](figures/use-case-diagram.svg)

## IDEEN-BESCHREIBUNG
- ermöglichen KMU und Startups das einfache Hochladen, Versenden und rechtsverbindliche Signieren von Dokumenten über eine digitale Plattform, basierend auf einem flexiblen Pay-as-you-go-Preismodell.

## ADRESSIERTE NUTZER
- Hobbyisten, KMU und Startups. Grossunternehmen sind auch willkommen, aber Docusign und andere Lösungen sind Kosteneffizienter für Grossunternehmen

## ADRESSIERTE BEDÜRFNISSE
- Bedarf an einem kostengünstigen, flexiblen digitalen Signaturdienst ohne langfristige Verträge.
- einfach, intuitiv Plattform für das Management von Dokumenten.
- Rechtliche Anerkennung digital signierter Dokumente.

## PROBLEME
- Hohe Kosten und Komplexität bestehender digitaler Signaturlösungen.
- Technische Herausforderungen beim Wechsel von papierbasierten zu digitalen Prozessen.
- Rechtsunsicherheit bei der Verwendung digitaler Signaturen.

## IDEENPOTENZIAL
Mehrwert: Digital vs. Papierbasiert

🔵🔵🔵🔵🔵🔵🔵🔵⚪️⚪️

Übertragbarkeit: Digitalisierung VS Papierbasiert bleiben

🔵🔵🔵⚪️⚪️⚪️⚪️⚪️⚪️⚪️

Machbarkeit: AWS vs Technische Anforderungen

🔵🔵🔵🔵🔵🔵⚪️⚪️⚪️⚪️

## DAS WOW
- Simpler Workflow welcher es den Benutzern ermöglicht Dokumente zu versenden und signieren zu lassen. 

## HIGH-LEVEL-KONZEPT
- Digitales Postamt für rechtlich verbindliche Signaturen.

## WERTVERSPRECHEN
- Wir bieten KMU, Hobbyisten und Startups eine einfache, kosteneffektive und rechtlich verbindliche Lösung, um Dokumente digital zu signieren und signieren zu lassen, mit der Flexibilität eines Pay-as-you-go-Modells.

#  EVALUATE BOARD: PolicySign

## KANÄLE
- Sponsor YouTube creators, die über Entrepeneurs innerhalb der Finanzindustry Niche sind.

## UNFAIRER VORTEIL
- Einzigartiges Preismodell mit Pay as you go
- Vendor Lockin, nachdem ein Benutzer bei uns angefangen hat

## KPI
- Anzahl abgeschlossenen Envelopes
- Kundenakquisitionskosten 
- Durchschnittlicher Umsatz pro Benutzer

## EINNAHMEQUELLEN
- Pay as you go Modell: Jede abgeschlossener Envelope ist 5 CHF.
- Enterprise Lösungen für Grosskunden.

# Erkenntnisse aus dem Pitch

Es gab vier wesentliche Kritikpunkte aus dem Pitch, auf die ich eingehen möchte.

## Q: Wie wird die Lösung rechtlich anerkannt?
A: Rechtlich wird die Lösung in der Schweiz anerkannt, da eigenhändig in einem dafür vorgesehenen Feld unterschrieben wird. Dies entspricht den gesetzlichen Anforderungen an elektronische Signaturen und gewährleistet, dass die Unterschriften vor Gericht Bestand haben. Meine Lösung bietet die gleiche Rechtsgültigkeit wie eine handschriftliche Unterschrift auf Papier.

## Q: Wie kann sichergestellt werden, dass eine Person unterschriftsberechtigt ist?
A: Es wird eine einzigartige URL generiert, die ausschliesslich dem zu unterschreibenden Empfänger zugesendet wird. Diese URL ist personalisiert und enthält alle relevanten Informationen, um die Authentizität und Berechtigung der Person sicherzustellen. Zudem wird eine zweistufige Verifizierung eingesetzt, um die Identität des Unterzeichners zusätzlich zu bestätigen.

## Q: Daten müssten gut verschlüsselt sein, damit Start-Ups einem mit solchen Informationen wie Verträgen trauen würden.
A: Die Dokumente werden sicher in Amazon S3 gespeichert, wobei strenge Verschlüsselungsprotokolle zum Einsatz kommen. Der Zugang zu den Dokumenten ist durch starke Authentifizierungsmechanismen geschützt, um sicherzustellen, dass nur berechtigte Personen Zugriff haben. Obwohl theoretische Risiken wie Bruteforce-Angriffe bestehen, minimiere ich diese durch umfassende Sicherheitsmassnahmen und regelmässige Sicherheitsüberprüfungen.

## Q: Was ist der Unique Selling Point (USP)?
A: Mein Pay-As-You-Go-Modell bietet eine flexible und kosteneffiziente Lösung für die elektronische Signatur von Dokumenten. Im Vergleich zu bestehenden Anbietern wie DocuSign (Lizenzkosten ab 3600 USD) oder Adobe Sign (Lizenzkosten ab 2800 USD), biete ich eine preisgünstigere Alternative, die insbesondere für Start-Ups und kleine Unternehmen attraktiv ist. Zusätzlich zeichnet sich meine Lösung durch eine einfache Integration, Benutzerfreundlichkeit und höchste Sicherheitsstandards aus.

## Fazit
Meine Lösung adressiert die wesentlichen Bedenken und bietet eine rechtlich anerkannte, sichere und kosteneffiziente Methode zur elektronischen Signatur von Dokumenten. Sie stellt eine wettbewerbsfähige Alternative zu bestehenden Lösungen dar und ist besonders für Start-Ups und kleinere Unternehmen geeignet.

[Diskussion auf Moodle](https://moodle.zhaw.ch/mod/forum/discuss.php?d=126567)