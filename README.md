Pro uspesny build aplikace je potreba naimportovat vsechny zavislosti, ktere jsou uvedene v POM.xml.
Nasledne v souboru application.properties nastavit pristupy do Postgresql DB.
Po teto konfiguraci je potreba nastavit server na kterem aplikace pobezi, v danem pripade byl pouzity
Apache Tomcat 9.0. V nastavenich Idei "Edit configurations" pro build lze vybrat tomcat a v okne
s timto nastvenim je potreba se prekliknout na zalozku deployment a pridat artefakt "[jmeno] exploded".
Nyni lze aplikaci zkompilovat a spustit.

Nasledne primo v kodu pro spravne fungovani je potreba nastavit promenne:
WINDOW_SIZE_IN_MINUTES - okno v kterem se pridavaji a mazou ip adresy. Blacklisting.
MAX_REQUEST_PER_IP_IN_WINDOW - Max pocet req pro konkretni ip.
MAX_REQUEST_PER_IP_Before_Throttling_IN_WINDOW - Max pocet req pro throttling pro konkretni ip.
LIMITED_PATHS - cesty na kterych se bude uplatnovat blacklisting, throttling.
addSecondsTimeTillNextReq - pocet sukund,k tery musi ubehnout aby se odblokoval throttling.