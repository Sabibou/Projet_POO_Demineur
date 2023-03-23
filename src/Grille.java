import java.util.Random;
import java.io.FileWriter;
import java.io.IOException; 

public class Grille {
    
    private Case beginning; //case tout en haut a gauche de la grille
    private Case end; //case tout en bas a droite
    private int nbMine;
    private int dimension;
    private int discovered;

    public Grille(){

        this.beginning = null;
        this.end = null;
        this.nbMine = 0;
        this.dimension = 0;
        this.discovered = 0;
    }

    public Grille(int dimension, float pourcentMine){

        this.dimension = dimension;
        this.discovered = 0;
        this.beginning = new Case(false, 0);
        this.nbMine = (int)((float)(dimension * dimension) * pourcentMine);

        Case rowC = this.beginning;

        for(int i=1; i<dimension; i++){

            Case currentC = new Case(false, 0);

            rowC.addVoisins(Direction.DROITE, currentC);

            for(int j=0; j<i; j++){

                //System.out.println(1);
                Case newC = new Case(false, 0);
                currentC.addVoisins(Direction.BAS, newC);
                currentC = newC;
                //System.out.println(newC.getVoisin(Direction.HAUT).toString());
            }

            if(dimension == i+1){

                this.end = currentC;
            }
            
            for(int j=0; j<i; j++){

                Case newC = new Case(false, 0);
                currentC.addVoisins(Direction.GAUCHE, newC);
                currentC = newC;

            }

            rowC = rowC.getVoisin(Direction.DROITE);

        }
    }

    public void fillMine(){

        Random rnd = new Random();
        int nb = 0;
        int x = 0;
        int y = 0;
        Case c;

        while(nb < nbMine){

            x = rnd.nextInt(this.dimension);
            y = rnd.nextInt(this.dimension);

            c = this.beginning;

            for(int i=0; i<x; i++){

                if(i < y){

                    c = c.getVoisin(Direction.BAS_DROITE);
                }
                else{

                    c = c.getVoisin(Direction.DROITE);
                }
            }

            for(int i=0; i<y-x; i++){

                c = c.getVoisin(Direction.BAS);
            }

            if(!c.isMined()){

                c.setMine();
                nb++;
            }
        }

    }

    public int uncover(int x, int y){

        Case c;

        if(x <= dimension/2 || y <= dimension/2){

            c = this.beginning;

            for(int i=1; i<y; i++){

                if(i < x){

                    c = c.getVoisin(Direction.BAS_DROITE);
                }
                else{

                    c = c.getVoisin(Direction.DROITE);
                }
            }

            for(int i=0; i<x-y; i++){

                c = c.getVoisin(Direction.BAS);
            }
        }
        else{

            c = this.end;

            for(int i=0; i<dimension - y; i++){

                if(i < dimension - x){

                    c = c.getVoisin(Direction.HAUT_GAUCHE);
                }
                else{

                    c = c.getVoisin(Direction.GAUCHE);
                }
            }

            for(int i=0; i<y-x; i++){

                c = c.getVoisin(Direction.HAUT);
            }
        }
        int nbUncover = c.uncover();
        this.discovered += nbUncover; 
        
        return nbUncover;
    }

    public void save(){

        try{
            FileWriter myWriter = new FileWriter("../save.txt");

            myWriter.write(this.dimension + "\n");

            Case rowC = this.beginning;

            String s = rowC.getEtat() + " " + rowC.isMined() + "\n";

            rowC = rowC.getVoisin(Direction.DROITE);

            myWriter.write(s);

            for(int i=1; i<dimension; i++){

                Case currentC = rowC;

                for(int j=0; j<i; j++){
    
                    s = currentC.getEtat() + " " + currentC.isMined() + "\n";
                    myWriter.write(s);

                    currentC = currentC.getVoisin(Direction.BAS);
                }
    
                for(int j=0; j<i; j++){
    
                    s = currentC.getEtat() + " " + currentC.isMined() + "\n";
                    myWriter.write(s);

                    currentC = currentC.getVoisin(Direction.GAUCHE);
                }

                s = currentC.getEtat() + " " + currentC.isMined() + "\n";
                myWriter.write(s);

                rowC = rowC.getVoisin(Direction.DROITE);
    
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.\n");
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    /*
    public void load(){

        this.dimension = dimension;
        this.discovered = 0;
        this.beginning = new Case(false, 0);
        this.nbMine = (int)((float)(dimension * dimension) * pourcentMine);

        Case rowC = this.beginning;

        for(int i=1; i<dimension; i++){

            Case currentC = new Case(false, 0);

            rowC.addVoisins(Direction.DROITE, currentC);

            for(int j=0; j<i; j++){

                //System.out.println(1);
                Case newC = new Case(false, 0);
                currentC.addVoisins(Direction.BAS, newC);
                currentC = newC;
                //System.out.println(newC.getVoisin(Direction.HAUT).toString());
            }

            if(dimension == i+1){

                this.end = currentC;
            }
            
            for(int j=0; j<i; j++){

                Case newC = new Case(false, 0);
                currentC.addVoisins(Direction.GAUCHE, newC);
                currentC = newC;

            }

            rowC = rowC.getVoisin(Direction.DROITE);

        }
    }
    */

    public int isAllDiscorvered(){

        return this.dimension * this.dimension - this.nbMine <= this.discovered ? 1 : 0;
    }

    public int getDimension(){

        return this.dimension;
    }

    @Override
    public String toString(){

        String s = "\n";
        int row = 1;

        Case currentRow = this.beginning;

        for(int i=1; i<=dimension; i++){

            s += i + "   ";
        }

        s += "\n\n";

        while(currentRow != null){

            Case currentC = currentRow;

            while(currentC != null){

                s += currentC.toString() + " | ";
                currentC = currentC.getVoisin(Direction.DROITE);
            }

            s += "  " + row + "\n";

            currentRow = currentRow.getVoisin(Direction.BAS);

            row++;
        }

        return s;
    }
}
