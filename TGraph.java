import java.lang.reflect.Array;
import java.util.Arrays;

public class TGraph {

    private int n_r, n_c; // nombre de lignes et de colonnes
    private int w_r, w_c; // poids des lignes et des colonnes
    private int[][] left, right; // pour modéliser les 2 parties du graphe de Tanner

    public TGraph(Matrix H, int w_r, int w_c){
        this.w_r = w_r;
        this.w_c = w_c;
        this.n_r = H.getRows();
        this.n_c = H.getCols();
        left = new int[H.getRows()][w_r+1];
        right = new int[H.getCols()][w_c+1];
        int inc = 1; // pour prendre en compte le fait que la première colonne contient soit que des 0 soit la valeur d'un mot

        // left
        for(int i=0; i<H.getRows();i++) {
            left[i][0] = 0;
            for(int j=0; j<H.getCols();j++){
                if(H.getElem(i,j) == 1){
                    left[i][inc++] = j;
                }
            }
            inc = 1;
        }
        // right
        for(int j=0; j<H.getCols();j++){
            right[j][0] = 0;
            for(int i=0; i<H.getRows();i++) {
                if(H.getElem(i,j) == 1){
                    right[j][inc++] = i;
                }
            }
            inc=1;
        }

    }

    public void display(){
        System.out.println("LEFT :");
        for(int[] nb : left){
            System.out.println(Arrays.toString(nb));
        }
        System.out.println("RIGHT :");
        for(int[] nb : right){
            System.out.println(Arrays.toString(nb));
        }
    }

    public Matrix decode(Matrix code, int rounds){
        int i,j,k;
        boolean verif = true;
        byte[][] bcorrect = new byte[code.getRows()][code.getCols()];

        // Initialisation
        for(j=0;j<right.length;j++){
            right[j][0]=code.getElem(0,j);
        }

        // Boucle principale
        for(int lim=0;lim<rounds;lim++){
            // Calcul des parités
            for(i=0;i<left.length;i++){
                left[i][0] = 0;
                for(k=1;k<w_r+1;k++){
                    left[i][0] = (left[i][0] + right[left[i][k]][0])%2;
                }
            }

            // Vérification
            for(i = 0; i< left.length;i++){
                if(left[i][0]==(byte)1) {
                    verif = false;
                    break;
                }
            }
            if(verif){
                for(j=0;j<right.length;j++){
                    bcorrect[0][j] = (byte) right[j][0];
                }
                Matrix correct = new Matrix(bcorrect);
                return correct;
            }

            // Calcul du max
            int max = 0;
            int count;
            for(j=0;j<right.length;j++){
                count = 0;
                for(k=1;k<=w_c;k++){
                    count += left[right[j][k]][0];
                }
                if(count>max){max = count;}

                // Renversement de bits
                if(count == max){
                    right[j][0] = (1 - right[j][0])%2;
                }

            }


        }
        for(j=0;j<right.length;j++){
            bcorrect[0][j] = -1;
        }
        Matrix correct = new Matrix(bcorrect);
        return correct;
    }

}
