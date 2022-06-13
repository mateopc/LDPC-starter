import java.util.*;
import java.io.*;

public class Main {
    
    public static Matrix loadMatrix(String file, int r, int c) {
        byte[] tmp =  new byte[r * c];
        byte[][] data = new byte[r][c];
        try {
            FileInputStream fos = new FileInputStream(file);
            fos.read(tmp);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < r; i++)
            for (int j = 0; j< c; j++)
                data[i][j] = tmp[i * c + j];
            return new Matrix(data);
    }

    public static void affichage(Matrix e, Matrix x, Matrix h, String number, TGraph mg){
        Matrix y = x.add(e);
        Matrix sy = h.multiply(y.transpose());
        Matrix x1 = mg.decode(y, 100);
        //mg.display();

        // e1
        System.out.println("---------------------------------------------");
        System.out.println("Mot de code x");
        x.display();
        System.out.println("Vecteur d'erreur e"+number);
        e.display();
        System.out.println("Mot de code bruite y"+number+"=x+e"+number);
        y.display();
        System.out.println("Syndrome de y"+number);
        sy.transpose().display();
        System.out.println("Correction x"+number+" de y"+number);
        x1.display();

        boolean results = (x1.isEqualTo(x));
        String res = "x" + number +" = x : " + results;
        System.out.println(res);
    }
    
    public static void main(String[] arg){
        
        byte[][] tab = {{1,1,0},{0,1,0},{0,0,1}};
        Matrix m = new Matrix(tab);

        byte[][] tab2 = {{0,1,0},{1,1,0},{0,1,0}};
        Matrix m2 = new Matrix(tab2);

        // Première tâche : l'encodage
        Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        hbase.display();

        hbase.sysTransform().display();

        Matrix G = hbase.sysTransform().genG();
        G.display();

        // Exercice 6 à 9
        byte[][] utab = {{1,0,1,0,1}};
        Matrix u = new Matrix(utab);
        u.display();
        Matrix x = u.multiply(G);
        x.display();

        // Exercice 7
        byte[][] tabmym = {{1,0,1,0},{0,1,0,1}};
        Matrix mym = new Matrix(tabmym);
        mym.display();
        TGraph mg = new TGraph(hbase, 4, 3);
        mg.display();

        byte[][] mye1 = {{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        Matrix e1 = new Matrix(mye1);

        byte[][] mye2 = {{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        Matrix e2 = new Matrix(mye1);

        byte[][] mye3 = {{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}};
        Matrix e3 = new Matrix(mye1);

        byte[][] mye4 = {{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}};
        Matrix e4 = new Matrix(mye1);

        affichage(e1,x,hbase,"1",mg);
        affichage(e2,x,hbase,"2",mg);
        affichage(e3,x, hbase,"3",mg);
        affichage(e4,x,hbase,"4",mg);
        
        Matrix h2048 = loadMatrix("data/Matrix-2048-6144-5-15",2048,6144);
        Matrix hsys2048 = h2048.sysTransform();
        Matrix G2048 = hsys2048.genG();
        
        Matrix u2048 = new Matrix(1,G2048.getRows());
        for (int i=0; i<u2048.getCols() ; i++) {
            if(i%2==0) {
                u2048.setElem(0,i,(byte)1);
            }
            else {
                u2048.setElem(0,i,(byte)0);
            }
        }
        
        Matrix x = u2048.multiply(G2048);
            
        TGraph graph = new TGraph(h2048);
      
        
        
        //ex13
       
        int echec = 0;
        int succes = 0;
        int errone = 0;
        
        //boucle de 10^4 itérations
        for (int k=0; k< 10000 ; k++) {
            // générez un mot e à chaque itération de poids w = 124
            Matrix err = x.errGen(124);
            // calculez le mot y = x + e
            Matrix y = x.add(err);
            // décodez ce mot en utilisant la méthode decode sur round = 200 itérations
            Matrix y_dec = graph.decode(y,200);
            // comparez le résultat avec x
            int erreur = 0;
            if (y_dec.getElem(0, 0)==-1) {
                erreur = -1;
            }
            else {
                for (int i =0;i<x.getRows(); i++) {
                    if ((x.getElem(0,i)) != y_dec.getElem(0,i)) {
                    erreur += 1;
                }
            }
                
            // Déterminez si le décodage a réussi, échoué, ou s’il a renvoyé un mot erroné
            if (erreur == -1) {
                echec +=1;
            }
            if (erreur == 0) {
                succes+=1;
            }
            else {
                errone+=1;
            }
        }
            
        // on affiche le résultat
        System.out.print("Echecs : "+echec+"\n");
        System.out.print("Succes : "+succes+"\n");
        System.out.print("Corrections érronées : "+errone+"\n");
    }
}
