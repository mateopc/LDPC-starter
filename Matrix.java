import java.util.*;
import java.io.*;

public class Matrix {
    private byte[][] data = null;
    private int rows = 0, cols = 0;
    
    public Matrix(int r, int c) {
        data = new byte[r][c];
        rows = r;
        cols = c;
    }
    
    public Matrix(byte[][] tab) {
        rows = tab.length;
        cols = tab[0].length;
        data = new byte[rows][cols];
        for (int i = 0 ; i < rows ; i ++)
            for (int j = 0 ; j < cols ; j ++) 
                data[i][j] = tab[i][j];
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public byte getElem(int i, int j) {
        return data[i][j];
    }
    
    public void setElem(int i, int j, byte b) {
        data[i][j] = b;
    }
    
    public boolean isEqualTo(Matrix m){
        if ((rows != m.rows) || (cols != m.cols))
            return false;
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                if (data[i][j] != m.data[i][j])
                    return false;
                return true;
    }
    
    public void shiftRow(int a, int b){
        byte tmp = 0;
        for (int i = 0; i < cols; i++){
            tmp = data[a][i];
            data[a][i] = data[b][i];
            data[b][i] = tmp;
        }
    }
    
    public void shiftCol(int a, int b){
        byte tmp = 0;
        for (int i = 0; i < rows; i++){
            tmp = data[i][a];
            data[i][a] = data[i][b];
            data[i][b] = tmp;
        }
    }
     
    public void display() {
        System.out.print("[");
        for (int i = 0; i < rows; i++) {
            if (i != 0) {
                System.out.print(" ");
            }
            
            System.out.print("[");
            
            for (int j = 0; j < cols; j++) {
                System.out.printf("%d", data[i][j]);
                
                if (j != cols - 1) {
                    System.out.print(" ");
                }
            }
            
            System.out.print("]");
            
            if (i == rows - 1) {
                System.out.print("]");
            }
            
            System.out.println();
        }
        System.out.println();
    }
    
    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);
        
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                result.data[j][i] = data[i][j];
    
        return result;
    }
    
    public Matrix add(Matrix m){
        Matrix r = new Matrix(rows,m.cols);
        
        if ((m.rows != rows) || (m.cols != cols))
            System.out.printf("Erreur d'addition\n");
        
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                r.data[i][j] = (byte) ((data[i][j] + m.data[i][j]) % 2);
        return r;
    }
    
    public Matrix multiply(Matrix m){
        Matrix r = new Matrix(rows,m.cols);
        
        if (m.rows != cols)
            System.out.printf("Erreur de multiplication\n");
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                r.data[i][j] = 0;
                for (int k = 0; k < cols; k++){
                    r.data[i][j] =  (byte) ((r.data[i][j] + data[i][k] * m.data[k][j]) % 2);
                }
            }
        }
        
        return r;
    }


    // b est la ligne de destination soit b += a
    public void addRow(int a, int b){
        for(int i=0; i < cols; i++) {
            data[b][i] = (byte) ((data[b][i] + data[a][i])%2);
        }
    }

    // b est la colonne de destination soit b += a
    public void addCol(int a, int b){
        for(int i=0; i < rows; i++) {
            data[i][b] = (byte) ((data[i][b] + data[i][a])%2);
        }
    }

    // Exercice 4
    public Matrix sysTransform(){
        int i, j, m; // i = working row, j = working col, m = other row
        Matrix mat = new Matrix(data);

        // étape 1 de la vidéo
        for(i=0;i<getRows(); i++){
            for(j=getCols()-getRows(); j<getCols(); j++){
                if((j == i+getCols()-getRows())){
                    if((mat.data[i][j] != 1)) {
                        for (m = i; m < getRows(); m++) {
                            if (mat.data[m][j] == 1) {
                                mat.shiftRow(i, m);
                                break;
                            }
                        }
                    }
                    for(m=i+1;m<getRows();m++){
                        if(mat.data[m][j]==1) {
                            mat.addRow(i,m);
                        }
                    }
                }
            }
        }

        // étape 2 de la vidéo
        for(i=getRows()-1;i>=0; i--){
            for(j=getCols()-1; j>=getCols()-getRows(); j--){
                if((j == i+getCols()-getRows())){
                    for(m=i-1;m>=0;m--){
                        if(mat.data[m][j]==1) {
                            mat.addRow(i,m);
                        }
                    }
                }
            }
        }

        return mat;
    }

    // Exercice 5
    public Matrix genG(){
        int i, j;
        byte[][] tab = new byte[getCols()][getCols()-getRows()];

        for(i=0;i<getCols(); i++) {
            for (j = 0; j < getCols() - getRows(); j++) {
                // ajout de la matrice identité
                if(i<getCols()-getRows()) {
                    if(i==j){
                        tab[i][j] = 1;
                    }
                    else{
                        tab[i][j] = 0;
                    }
                }
                // ajout des valeurs de la matrice de contrôle systématique
                else {
                    tab[i][j] = data[i-(getCols()-getRows())][j];
                }
            }
        }
        Matrix my_mat = new Matrix(tab);
        return my_mat.transpose();
    }
    
    public Matrix errGen(int w) {
        Matrix err = new Matrix(1,cols);
        for (int i = 0; i<w ; i++) {
            int tmp = 0;
            do {
                Random random = new Random();
                tmp = random.nextInt(cols);
            } while (err.data[0][tmp]==1);
            err.data[0][tmp]=1;
        }
        return err;
    }


}



