package fr.wiiznokes.horloge11.utils;

public class StorageUtils {

    public static void afficher(String[][] tab){
        for(int i=0; i<tab.length; i++){
            for(int j=0; j<tab[i].length; j++){
                System.out.println("Array["+i+"]["+j+"]="+tab[i][j]);
            }
        };
    }
}
