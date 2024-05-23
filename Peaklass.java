package com.example.projekt_teineosa;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Peaklass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sõnamängud");

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));

        Label tere = new Label("Tere tulemast!");
        Label valimine = new Label("Sisestage kasutajanimi ja valige mäng");
        TextField kasutajanimi = new TextField();
        kasutajanimi.setPromptText("Sisestage oma kasutajanimi");

        Button sonadeleidmine = new Button("Sõnade leidmine");
        sonadeleidmine.setOnAction(event -> {
            kirjutaFaili(String.valueOf(kasutajanimi), "Sõnade leidmine");
            SõnadeLeidmine sonadeLeidmine = new SõnadeLeidmine();
            try {
                sonadeLeidmine.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        Button araArvamine = new Button("Sõnade ära arvamine");
        araArvamine.setOnAction(event -> {
            kirjutaFaili(String.valueOf(kasutajanimi), "Sõnade ära arvamine");
            ArvaÄraSõna arvaÄraSõna = new ArvaÄraSõna();
            arvaÄraSõna.start(new Stage());
            try {
                arvaÄraSõna.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        Button lopeta = new Button("Lõpeta mäng");
        lopeta.setOnAction(event -> primaryStage.close());

        vBox.getChildren().addAll(tere,valimine,kasutajanimi,sonadeleidmine, araArvamine, lopeta);
        Scene scene = new Scene(vBox, 350, 225);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //meetod faili kirjutamiseks
    //kirjutame kasutajanimi, tema poolt valitud mängu ja kuupäeva faili "MänguAndmed.txt"
    private void kirjutaFaili(String kasutajanimi, String valitudmäng){
        String failinimi = "MänguAndmed.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(failinimi));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String paev = now.format(formatter);
            writer.write("Mängija: \""+kasutajanimi+ "\", Mäng: \"" +valitudmäng+"\", Kuupäev: "+ paev+"\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
