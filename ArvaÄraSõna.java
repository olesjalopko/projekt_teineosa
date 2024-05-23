package com.example.projekt_teineosa;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ArvaÄraSõna extends Application {
    private ChoiceBox<String> kategooriaValik;
    private ChoiceBox<String> mängiTaseValik;
    private TextField arvamiseVäli;
    private Label vastusteVäli;
    private Button alustaNupp;
    private Button arvaNupp;
    private Button vihjeEsimeneNupp;
    private Button vihjeJärgmineNupp;
    private Button uusSõnaNupp;
    private Button lõpetaNupp;
    private Button alglehele;
    private Random random;
    private String valitudKategooria;
    private String valitudSõna;
    private int maxPikkus;
    private int vihjeIndeks;
    private SõnadeFailiTöötleja sõnadeFailiTöötleja;

    @Override
    public void start(Stage peaLava) {
        // Loon JavaFX kasutajaliidese
        VBox juur = new VBox(10);
        juur.setAlignment(Pos.CENTER);

        Label tervitus = new Label("Tere tulemast sõnade ära arvamise mängu!");
        Label selgitavTekst = new Label("Palun vali endale meeldiv kategooria ja mängutase:");
        kategooriaValik = new ChoiceBox<>();
        kategooriaValik.getItems().addAll("programmeerimine", "loodus", "matemaatika", "loomad", "tehnika");
        kategooriaValik.setValue("programmeerimine");
        mängiTaseValik = new ChoiceBox<>();
        mängiTaseValik.getItems().addAll("lihtne", "keskmine", "raske");
        mängiTaseValik.setValue("lihtne");
        alustaNupp = new Button("Alusta mängu");
        alustaNupp.setOnAction(e -> alustaMängu());

        arvamiseVäli = new TextField();
        vastusteVäli = new Label();
        arvaNupp = new Button("Kontrolli sõna");
        arvaNupp.setOnAction(e -> kontrolliVastust());


        vihjeEsimeneNupp = new Button("Vihje: Sõna esimene täht");
        vihjeEsimeneNupp.setOnAction(e -> annaVihje(0));

        vihjeJärgmineNupp = new Button("Vihje: Sõna järgmine täht");
        vihjeJärgmineNupp.setOnAction(e -> annaJärgmineVihje());

        uusSõnaNupp = new Button("Uus sõna");
        uusSõnaNupp.setOnAction(e -> alustaMängu());

        lõpetaNupp = new Button("Lõpeta mäng");
        lõpetaNupp.setOnAction(e -> peaLava.close());

        alglehele = new Button("Tagasi alglehele");
        alglehele.setOnAction(event -> {
            peaLava.close();
            Peaklass peaklass = new Peaklass();
            peaklass.start(new Stage());
        });

        juur.getChildren().addAll(tervitus, selgitavTekst, kategooriaValik, mängiTaseValik, alustaNupp, arvamiseVäli, arvaNupp, vihjeEsimeneNupp, vihjeJärgmineNupp, uusSõnaNupp, vastusteVäli, lõpetaNupp, alglehele);

        Scene stseen = new Scene(juur, 800, 600); // Akna suurus
        peaLava.setScene(stseen);
        peaLava.setTitle("Sõnade ära arvamise mäng");
        peaLava.show();

        // Loon sõnade failitöötleja
        sõnadeFailiTöötleja = new SõnadeFailiTöötleja();
    }

    private void alustaMängu() {
        valitudKategooria = kategooriaValik.getValue();
        String mängiTase = mängiTaseValik.getValue();

        switch (mängiTase) {
            case "lihtne":
                maxPikkus = 6;
                break;
            case "keskmine":
                maxPikkus = 8;
                break;
            case "raske":
                maxPikkus = 10;
                break;
            default:
                maxPikkus = 6;
                break;
        }

        // Loeb sõnad failist
        List<String> sõnad = sõnadeFailiTöötleja.loeSõnadFailist("sonastikud.txt");
        if (sõnad.isEmpty()) {
            vastusteVäli.setText("Viga sõnade lugemisel failist.");
            return;
        }



        random = new Random();
        arvaSõna(sõnad);
    }

    private void arvaSõna(List<String> sõnad) {
        valitudSõna = sõnad.get(random.nextInt(sõnad.size()));

        if (valitudSõna.length() > maxPikkus) {
            arvaSõna(sõnad);
            return;
        }

        vastusteVäli.setText("Uus sõna valitud. Pakutud sõna koosneb " + valitudSõna.length() + " tähest.\nAlustame äraarvamist!");
        vihjeIndeks = 0;
    }

    private void annaVihje(int indeks) {
        if (valitudSõna.length() > indeks) {
            vastusteVäli.setText("Vihje: " + valitudSõna.charAt(indeks));
        } else {
            vastusteVäli.setText("Sõna on liiga lühike, et anda vihjet.");
        }
    }

    private void annaJärgmineVihje() {
        vihjeIndeks++;
        annaVihje(vihjeIndeks);
    }

    private void kontrolliVastust() {
        String kasutajaVastus = arvamiseVäli.getText().trim().toLowerCase();
        if (kasutajaVastus.equals(valitudSõna)) {
            vastusteVäli.setText("Palju õnne! Sa arvasid sõna ära!");
        } else {
            vastusteVäli.setText("Vale vastus. Proovi uuesti!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
