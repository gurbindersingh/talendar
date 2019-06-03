#################
#    BACKEND    #
#################

Boot Optionen:

1) Applikation mit Mockdaten initialisieren:

mvn spring-boot:run -Dspring-boot.run.arguments=--testData=yes

Initialisiert Database mit: Default Anzahl an Trainern, Kursen, Mieten, Geburtstagen, Beratungen über einen Zeitraum von X Tagen

Anzahl der Entitäten customizeable über Commandline Argumente:
trainers=int
courses=int
days=int
rents=int
bdays=int
consultations=int

e.g. 
mvn spring-boot:run -Dspring-boot.run.arguments=--testData=yes,courses=20,rents=2,days=30  ("optionen mit , trennen")

NOTE: Option wird noch erweitert, und enthält derzeit keine eingetragenen Urlaube und keine Buchungen für Kurse