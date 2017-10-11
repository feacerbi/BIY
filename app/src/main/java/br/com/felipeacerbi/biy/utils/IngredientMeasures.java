package br.com.felipeacerbi.biy.utils;

public class IngredientMeasures {

    public static String getFormattedMeasure(String rawMeasure, boolean isPlural) {
        String newMeasure = "";

        switch (rawMeasure) {
            case "CUP":
                newMeasure = "cup";
                break;
            case "TBLSP":
                newMeasure = "tblsp";
                break;
            case "TSP":
                newMeasure = "tsp";
                break;
            case "K":
                newMeasure = "kg";
                break;
            case "G":
                newMeasure = "gram";
                break;
            case "OZ":
                newMeasure = "ounce";
                break;
            case "UNIT":
                newMeasure = "unit";
                break;
        }

        if(isPlural) newMeasure += "s";

        return newMeasure;
    }
}
