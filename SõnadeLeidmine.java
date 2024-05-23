package com.example.projekt_teineosa;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SõnadeLeidmine extends Application {

    private List<String> vastused;
    private List<Button> nupud;
    private List<Button> valitudnupud;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sõnade leidmise mäng");

        VBox vbox = new VBox();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label reeglid = new Label("Sõnade leidmise mäng.\n" +
                "Teil on antud tabel tähtedega, kus on peidetud sõnad.\n" +
                "Need võivad asuda vertikaalselt, horisontaalselt ja diagonaalselt." +
                " Sõnad võivad olla peidetud nii ülalt alla kui ka alt üles ning" +
                " nii vasakult paremale kui ka paremalt vasakule. \n" +
                "Teie ülesanne on leida kõik peidetud sõnad.\n");
        reeglid.setWrapText(true);
        reeglid.setPadding(new Insets(10,10,10,10));


        //juhuslikult valitakse mäng failist
        List<List<String[]>> mangud = loeSonad("SõnadeLeidmine.txt");
        int kokkumange = mangud.size();

        Random random = new Random();
        int mangunr = random.nextInt(kokkumange);
        List<String[]> mang = mangud.get(mangunr);
        int ridadearv = mang.size();

        //valitud mängu leiame vastused
        List<String> vastused = new ArrayList<>();
        if (ridadearv != 0) {
            String[] v = mang.get(ridadearv - 1);
            for (int i = 1; i < v.length; i++) {
                vastused.add(v[i].toLowerCase());
            }
        } else {
            System.out.println("Mängud puuduvad.");
        }

        nupud = new ArrayList<>();
        valitudnupud = new ArrayList<>();


        for (int i = 0; i < ridadearv - 1; i++) {
            String[] tahed = mang.get(i);
            for (int j = 0; j < tahed.length; j++) {
                Button button = new Button(tahed[j]);
                button.setOnAction(event -> {
                    Button vajutatud = (Button) event.getSource();
                    //kui kasutaja vajutab nuppu, muutub nupp halliks
                    if (!valitudnupud.contains(vajutatud)) {
                        vajutatud.setStyle("-fx-background-color: lightgrey;");
                        valitudnupud.add(vajutatud);
                    } else {
                        //kui kasutaja juhuslikult vajutas teise nuppu, siis saab ta
                        // uuesti nuppu vajutada ja valik tühistatakse
                        vajutatud.setStyle("-fx-background-color: lightgrey;");
                        int viimaneIndeks = valitudnupud.size() - 1;
                        valitudnupud.add(vajutatud);
                        int vajutatudIndeks = valitudnupud.size() - 1;
                        if (valitudnupud.get(viimaneIndeks) == valitudnupud.get(vajutatudIndeks)) {
                            StringBuilder valitudtahed = new StringBuilder();
                            boolean onolemas = false;
                            for (Button taht : valitudnupud) {
                                valitudtahed.append(taht.toString());
                            }
                            for (String vastus : vastused) {
                                if (!(vastus.contains(valitudtahed))) {
                                    onolemas = false;
                                }
                            }
                            if (!(onolemas)) {
                                vajutatud.setStyle("");
                                valitudnupud.remove(vajutatud);
                                valitudnupud.remove(vajutatud);
                                valitudtahed.lastIndexOf(String.valueOf(vajutatud));
                                valitudtahed.lastIndexOf(String.valueOf(vajutatud));
                            }
                        }
                    }
                });
                nupud.add(button);
                grid.add(button, j, i);
            }
        }

        Label teade = new Label();
        Button kontrolli = new Button("Kontrolli vastust");
        kontrolli.setOnAction(event -> {
            StringBuilder sona = new StringBuilder();
            List<Integer> valitudIndeksid = new ArrayList<>();
            //saame kasutajalt valitud sõna
            for (Button button : valitudnupud) {
                sona.append(button.getText().toLowerCase());
                valitudIndeksid.add(nupud.indexOf(button));
            }
            //kontrollime, kas valitud sõna on vastuste hulgas olemas
            if (vastused.contains(sona.toString()) && kasPaiknevadKorval(valitudIndeksid)) {
                vastused.remove(sona.toString());
                teade.setText("Õige vastus! On jäänud " + vastused.size() + " sõna.");
                valitudnupud.forEach(button -> button.setStyle("-fx-background-color: lightgreen;"));
                valitudnupud.clear();
            } else {
                teade.setText("Vale vastus! Proovige uuesti");
                valitudnupud.forEach(button -> button.setStyle(""));
                valitudnupud.clear();
            }
        });

        Button lopeta = new Button("Lõpeta mäng");
        lopeta.setOnAction(event -> {
                primaryStage.close();
                Peaklass peaklass = new Peaklass();
                try {
                    peaklass.start(new Stage());
                }catch (Exception e){
                    e.printStackTrace();
                }
            });


        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(kontrolli, lopeta, teade);
        vbox.getChildren().addAll(reeglid, grid, buttonBox);

        Scene scene = new Scene(vbox,510,600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //meetod failist lugemiseks
    public static List<List<String[]>> loeSonad(String failinimi) throws Exception{
        List<List<String[]>> tulemus = new ArrayList<>();
        File fail = new File(failinimi);
        try(Scanner scanner = new Scanner(fail, "UTF-8")){
            List<String[]> tul = new ArrayList<>();
            while (scanner.hasNextLine()){
                String rida = scanner.nextLine();
                String[] tahed = rida.split(",");
                if(tahed[0].equals("Vastus")){
                    tul.add(tahed);
                    tulemus.add(tul);
                    tul = new ArrayList<>();
                }else{
                    tul.add(tahed);
                }
            }

        }
        return tulemus;
    }

    //meetod selleks, et kontrollida, kas kasutaja poolt valitud nupud asuvad lähedal
    //(et ei oleks võimalik, et ta valiks sõna lihtsalt tähe kaupa erinevatest kohtadest)
    private boolean kasPaiknevadKorval(List<Integer> indeksid){
        //vertikaalselt,horisontaalselt
        int rida = -1;
        int veerg = -1;
        boolean horisontaalselt = true;
        boolean vertikaalselt = true;

        for (int i : indeksid){
            Button button = nupud.get(i);
            int buttonRida = GridPane.getRowIndex(button);
            int buttonVeerg = GridPane.getColumnIndex(button);

            if (rida == -1 && veerg == -1){
                rida = buttonRida;
                veerg = buttonVeerg;
            }else {
                if (rida != buttonRida){
                    horisontaalselt = false;
                }
                if (veerg!=buttonVeerg){
                    vertikaalselt = false;
                }
            }
        }

        //diagonaal
        boolean diagonaal = true;
        Button esimene = nupud.get(indeksid.get(0));
        int esimeneRida = GridPane.getRowIndex(esimene);
        int esimeneVeerg = GridPane.getColumnIndex(esimene);

        for (int i = 1; i < indeksid.size(); i++) {
            Button button2 = nupud.get(indeksid.get(i));
            int nuppRida = GridPane.getRowIndex(button2);
            int nuppVeerg = GridPane.getColumnIndex(button2);

            if(Math.abs(esimeneRida - nuppRida) != Math.abs(esimeneVeerg - nuppVeerg)){
                diagonaal = false;
            }
        }

        return horisontaalselt || vertikaalselt || diagonaal;
    }
}