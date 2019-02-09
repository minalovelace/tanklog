# Tankbuch

Dies ist ein winziges Projekt, welches in der Freizeit erstellt wurde. Es wurde eine einfache
**DSL**  erstellt, um ein zweckmäßiges Dokument im pdf-Format auszugeben. Das Dokument
hat den Zweck, über das Betanken, Warten und eventuelle Reparaturen des eigenen Fahrzeugs
Buch zu führen.

## Lieber Benutzer

Vielen Dank für die Verwendung meines kleinen hausgemachten Programms. Mit diesem kleinen
Helfer sind Sie nun in der Lage, ein einfaches aber dennoch ansehnliches *pdf*-Dokument zu
erstellen. Dieses Dokument enthält alle Informationen bezüglich aller Betankungen über Ihr
Fahrzeug. Darüberhinaus enthält es Informationen über eventuelle Reparaturen, die Sie vorher
eingegeben haben. Dabei ist die Eingabe der Daten so einfach wie möglich gestaltet worden.

Wie genau diese Daten einzugeben sind, wird im Laufe dieses Dokuments beschrieben. Die Eingabe
der Informationen, welche in dem *pdf*-Dokument erscheinen werden, können mithilfe einer
eigenen **DSL** in einer Textdatei eingegeben werden. Diese Textdatei muss sich im *input*-
Ordner direkt neben der *jar*-Datei befinden. Ein Doppelklick auf die *jar*-Datei erzeugt das
*pdf*-Dokuemnt im *output*-Ordner. Alles, was Sie wissen müssen ist, wie man die Daten mithilfe
der **DSL** eingibt. Dies wird im nächsten Abschnitt beschrieben.

## Die DSL

Die **DSL** gibt einfache Schlüsselwörter vor, die für die Beschreibung der Informationen
genutzt werden können. Wir werden die einzelnen Schlüsselwörter und ihre Verwendung nach und
nach durchgehen.

Mit dem Schlüsselwort **carname** können Sie den Namen Ihres Fahrzeugs angeben. Die Eingabe
nach dem Schlüsselwort bis zum Ende der Zeile wird als Name Ihres Fahrzeugs benutzt.

```custom
carname Mein tolles Auto
```

Mit dem Schlüsselwort **numberplate** können Sie den Inhalt Ihres Kennzeichens eingeben. Wie
schon zuvor wird Ihre Eingabe nach dem Schlüsselwort bis zum Ende der Zeile wird als
Inhalt Ihres Kennzeichens interpretiert.

```custom
numberplate AB - C 1234
```

Mit dem Schlüsselwort **garage** können Sie das Datum eingeben, wann Sie Ihr Fahrzeug in die
Werkstatt gebracht haben und was dort gemacht wurde. Sie können das Datum in drei
unterschiedlichen Formaten eingeben. Alles nach dem Datum bis zum Ende der Zeile wird als
Information interpretiert, was in der Werkstatt gemacht wurde.

```custom
garage YYYY-MM-DD Winterreifen aufgezogen
```

```custom
garage DD.MM.YYYY Winterreifen aufgezogen
```

```custom
garage MM/DD/YYYY Winterreifen aufgezogen
```

Da ein Ölwechsel etwas Besonderes ist, weil es für die Berechnung der Ölkilometer
herangezogen wird, muss dieser separat eingegeben werden. Bitte benutzen Sie dafür
folgende Syntax:

```custom
oilchange YYYY-MM-DD 12,345 km
```

Mit dem Schlüsselwort **entry** können Sie eingeben, wann Sie getankt haben, was die
Tankfüllung gekostet hat, wie viele Liter Sie getankt haben und welchen Kilometerstand
Ihr Fahrzeug zum Zeitpunkt des Tankens hatte. Euro, Liter und Kilometer können
abgekürzt werden: **eur**, **l**, und **km**.

```custom
entry YYYY-MM-DD 45.98 euro 35.99 liter 23,420 kilometer
```

Mit den Schlüsselwörtern **airpressure** und **fullyladen** oder **halfladen** können Sie
den Reifendruck ihres Fahrzeugs angeben, wenn es voll oder nur halb beladen ist.

```custom
airpressure fullyladen 2.4
```

oder

```custom
airpressure halfladen 2.5
```

Sie können den Reifendruck Ihres Ersatzrades wie folgt angeben:

```custom
airpressure sparewheel 4.2
```

Mit den Schlüsselwörtern **oilchange** und **with** oder **without** **filter** können Sie
die Menge an Öl in Litern angeben, die für einen Ölwechsel mit oder ohne Filter benötigt wird.

```custom
oilchange without filter 3.5
```

oder

```custom
oilchange with filter 4.5
```

Mit dem Schlüsselwort **purchasedetail** können Sie das Kaufdatum und den Kilometerstand zum
Zeitpunkt des Kaufs angeben.

```custom
purchasedetail 2016-02-15 51,515 km
```

Mit dem Schlüsselwort **note** können Sie Ihre eigenen Notizen zum Tankbuch hinzufügen.
Diese Notizen werden am Ende des *pdf*-Dokuments erscheinen. Wie schon zuvor wird der
Inhalt nach dem Schlüsselwort bis zum Ende der Zeile als Notiz interpretiert.

```custom
note Wichtige Notiz
```

Das Schlüsselwort **wook** ist etwas Besonderes. Es ist eine Kurzform für **w**ater and
**o**il **ok**ay. Mit dem Schlüsselwort lässt sich das Datum spezifizieren, an dem Sie
nach Öl, Wasser und anderen Dingen bei Ihrem Fahrzeug geschaut haben.

```custom
wook YYYY-MM-DD
```

## Die Ausgabe

Die Ausgabe wird auf Basis der neuesten Textdatei im *input*-Ordner erzeugt. Das Erzeugen
findet statt, sobald Sie doppelt auf die *jar*-Datei klicken. Sofern Ihre Eingabe in
Ordnung war, erscheint nach dem Doppelklick im *output*-Ordner ein *pdf*-Dokument.

## Offene Punkte und fehlende Features

Ein bisher nicht implementiertes Feature wäre eine formatierte Ausgabe der Textdatei, welche
die Basis für die Generierung des *pdf*-Dokuments ist.

Ein anderes offenes Feature ist die Angabe der Sprache und des Formats für die Zahlen im
*pdf*-Dokument. Das ist bisher die größte Schwäche des Projekts, da das *pdf*-Dokument bisher
nur in Deutsch erstellt wird. Der Benutzer sollte in der Lage sein, seine eigene Sprache, die
Zahlformate und die Einheiten wie Kilomenter oder Liter unzugeben.
