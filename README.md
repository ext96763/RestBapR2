Pro uspesny build aplikace je potreba naimportovat vsechny zavislosti, ktere jsou uvedene v POM.xml.
Nasledne v souboru application.properties nastavit pristupy do Postgresql DB.
Po teto konfiguraci je potreba nastavit server na kterem aplikace pobezi, v danem pripade byl pouzity
Apache Tomcat 9.0. V nastavenich Idei "Edit configurations" pro build lze vybrat tomcat a v okne
s timto nastvenim je potreba se prekliknout na zalozku deployment a pridat artefakt "[jmeno] exploded".
Nyni lze aplikaci skompilovat a spustit.