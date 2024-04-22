package gui;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import procesy.Proces;
import procesy.ProcesManualni;

import java.util.Iterator;

import abstrlifo.IAbstrLifo;
import javafx.geometry.Pos;

public class KandidatiDialog {
    
    public static void zobrazOkno(IAbstrLifo<Proces> kandidati) {
        if (kandidati == null) {
            throw new NullPointerException("Nelze zobrazit kandidáty, protože dosud nebyli vytipováni.");
        } 

        if (kandidati.jePrazdny()) {
            throw new IllegalArgumentException("Program nenalezl žádné kandidáty k reorganizaci.");
        }

        Stage stage = new Stage();
        
        stage.initModality(Modality.APPLICATION_MODAL);

        ListView<String> listKandidati = new ListView<String>();
        Iterator<Proces> iterator = kandidati.iterator();
        while (iterator.hasNext()) {
            Proces p = iterator.next();

            if (p instanceof ProcesManualni pm) listKandidati.getItems().add(pm.toString());
        }

        VBox layout = new VBox(listKandidati);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 400);

        stage.setTitle("Vytipovaní kandidáti");
        stage.setScene(scene);
        stage.show();
    }

}
