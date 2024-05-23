package com.example.projekt_teineosa;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SõnadeFailiTöötleja {
    private static final String SISSELAETUD_SÕNADE_FAIL = "sonastikud.txt";
    private static final String LOGIFAIL = "logifail.txt";

    public List<String> loeSõnadFailist(String failiNimi) {
        List<String> sõnad = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(failiNimi))) {
            while (scanner.hasNextLine()) {
                String rida = scanner.nextLine();
                sõnad.addAll(Arrays.asList(rida.split(":")[1].trim().split(", ")));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Viga: Faili ei leitud: " + failiNimi);
            e.printStackTrace();
        }
        return sõnad;
    }


    public static void kirjutaLogi(String teade) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOGIFAIL, true))) {
            bw.write(teade);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Viga logi kirjutamisel: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<String> sõnad = new ArrayList<>();
        for (String sõna : sõnad) {
            System.out.println(sõna);
        }
        kirjutaLogi("Kasutaja sisestas sõnad failist.");
    }
}

