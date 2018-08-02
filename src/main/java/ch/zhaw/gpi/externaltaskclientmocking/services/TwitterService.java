package ch.zhaw.gpi.externaltaskclientmocking.services;

/**
 * In diesem gemockten Beispiel, enthält diese Klasse lediglich eine Methode, um
 * einen Tweet zu posten. Typischerweise würde die Klasse aber weitere Funktionen
 * der Twitter REST API zugänglich machen als Java-Methoden.
 * 
 * @author scep
 */
public class TwitterService {
    
    /**
     * Simuliert das Posten eines Tweets, indem der Tweet-Inhalt in die Output-Konsole
     * geschrieben wird.
     * 
     * @param statusText Der zu postende Text
     */
    public void updateStatus (String statusText){
        // Ausgabe in die Output-Konsole
        System.out.println("Tweet wird geposted:" + statusText);
    }
}
