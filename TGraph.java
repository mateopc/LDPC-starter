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
        Matrix x = new Matrix(1,n_c);
        Matrix s = new Matrix(1,n_r);

        for(int i = 0; i<n_c;i++){
            right[i][0]=code.getElem(0,i);
        }

        for(int a=0;a<rounds;a++){

            for(int i=0;i<n_r;i++){
                left[i][0]=0;
                for(int k=1;k<=w_r;k++){
                    left[i][0]+=right[left[i][k]][0];
                }
                left[i][0]=left[i][0]%2;
                s.setElem(0,i,(byte)left[i][0]);
            }

            boolean verif=true;

            for(int i = 0; i<n_r;i++){
                if(left[i][0]==(byte)1){
                    verif=false;
                    break;
                }
            }
            if(verif){
                int ind = 0;
                for (int i=0;i<n_c;i++){
                    x.setElem(0,i,(byte)right[i][0]);
                }
                return x;
            }
            else{
                int max=0;
                for(int p=1;p<n_c;p++){
                    int count =0;
                    for(int q=1; q<=w_c;q++){
                        count += left[right[p][q]][0];
                    }
                    if(count>max){
                        max =count;
                    }
                }

                for(int p=1; p<n_c;p++){
                    int count=0;
                    for(int q=1;q<=w_c;q++){
                        count += left[right[p][q]][0];
                    }
                    if(count==max){
                        right[p][0] = (right[p][0]+1)%2;
                    }
                }
            }
        }

        for(int i=0;i<n_c;i++){
            x.setElem(0,i,(byte)-1);
        }
        return x;
    }

}
