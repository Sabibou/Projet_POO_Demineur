import java.util.Random;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.LinkedList;

public class Grille {

    private int nbMine;
    private int nbRow;
    private int nbColumn;
    private int discovered;
    private LinkedList<Case> plate;

    public Grille(){

        this.nbColumn = 0;
        this.nbMine = 0;
        this.nbRow = 0;
        this.plate = new LinkedList<Case>();
        this.discovered = 0;
    }

    public Grille(int nbRow, int nbColumn){

        this();
        this.nbColumn = nbColumn;
        this.nbRow = nbRow;
    }

    public Grille(int nbRow, int nbColumn, float pourcentMine){

        this();
        this.nbColumn = nbColumn;
        this.nbRow = nbRow;
        this.nbMine = (int)((float)(nbColumn * nbRow) * pourcentMine);

        Case rowC = new Case(false, 0);
        this.plate.add(rowC);

        Case currentC = rowC;

        Case newC;

        for(int i=1; i<nbRow; i++){

            for(int j=1; j<nbColumn; j++){

                newC = new Case(false, 0);
                currentC.addVoisins(Direction.DROITE, newC);

                currentC = newC;
            }

            this.plate.add(currentC);

            currentC = new Case(false, 0);

            rowC.addVoisins(Direction.BAS, currentC);

            rowC = currentC;

            this.plate.add(rowC);

        }

        for(int j=1; j<nbColumn; j++){

            newC = new Case(false, 0);
            currentC.addVoisins(Direction.DROITE, newC);
            currentC = newC;
        }

        this.plate.add(currentC);
    }

    public Case getCase(int row, int column){ //ca commence à 0 

        int index = row * 2;
        Case c;

        if(column > nbColumn/2){

            index++;

            c = this.plate.get(index);

            for(int i=nbColumn; i>column+1; i--){

                c = c.getVoisin(Direction.GAUCHE);
            }
        }
        else{

            c = this.plate.get(index);

            for(int i=0; i<column; i++){

                c = c.getVoisin(Direction.DROITE);
            }
        }

        return c;
    }

    public void fillMine(){

        if(this.nbMine == this.nbColumn * this.nbRow){

            for(int i=0; i<nbRow; i++){

                for(int j=0; j<nbColumn; j++){

                    getCase(i, j).setMine();
                }
            }
        }
        else{   

            Random rnd = new Random();
            int nb = 0;
            int row = 0;
            int column = 0;
            Case c;

            while(nb < this.nbMine){

                column = rnd.nextInt(this.nbColumn);
                row = rnd.nextInt(this.nbRow);

                c = getCase(row, column);

                if(!c.isMined()){

                    c.setMine();
                    nb++;
                }
            }
        }

    }

    private int uncover(int row, int column){

        Case c = getCase(row, column);

        int nbUncover = c.uncover();

        this.discovered += nbUncover; 
        
        return nbUncover;
    }

    public void addRow(Case[] array){

        if(array.length == this.nbColumn){
            
            Case currentC;

            if(!this.plate.isEmpty()){

                currentC = this.plate.get(this.plate.size() - 2);

                currentC.addVoisins(Direction.BAS, array[0]);
            }

            currentC = array[0];

            this.plate.add(currentC);

            for(int i=1; i<this.nbColumn; i++){

                currentC.addVoisins(Direction.DROITE, array[i]);
                currentC = array[i];
            }

            this.plate.add(currentC);

            if(this.plate.size()/2 > this.nbRow){

                this.nbRow++;
            }
        }
    }
    
    public void save(){

        try{
            FileWriter myWriter = new FileWriter("./save.txt");

            myWriter.write(this.nbRow + "\n");
            myWriter.write(this.nbColumn + "\n");

            Case rowC = this.plate.getFirst();
            String s;

            for(int i=0; i<nbRow; i++){

                Case currentC = rowC;

                for(int j=0; j<nbColumn; j++){
    
                    s = currentC.getEtat() + " " + currentC.isMined() + "\n";
                    myWriter.write(s);

                    currentC = currentC.getVoisin(Direction.DROITE);
                }

                rowC = rowC.getVoisin(Direction.BAS);
    
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.\n");
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }


    public Case[][] toArray(){

        Case[][] array = new Case[this.nbRow][this.nbColumn];

        Case c;

        for(int i=0; i<nbRow; i++){

            c = this.plate.get(i*2);

            for(int j=0; j<nbColumn; j++){

                array[i][j] = c;
                c = c.getVoisin(Direction.DROITE);

            }
        }

        return array;
    }
    

    public int isAllDiscorvered(){

        return this.nbColumn * this.nbRow - this.nbMine <= this.discovered ? 1 : 0;
    }

    public int getNbColumn(){

        return this.nbColumn;
    }

    public int getNbRow(){

        return this.nbRow;
    }

    public int getNbMines(){

        return this.nbMine;
    }

    public void setDiscovered(int discovered){

        this.discovered = discovered;
    }

    public  void setNbMines(int nbMine){

        this.nbMine = nbMine;
    }

    public void setCaseFlag(int row, int column){

        this.getCase(row, column).setFlag();
    }

    public void removeCaseFlag(int row, int column){

        this.getCase(row, column).removeFlag();
    }

    public int play(int nbRow, int nbColumn){

        return this.uncover(nbRow, nbColumn) > 0 ? this.isAllDiscorvered() : -1;
    }

    @Override
    public String toString(){

        String s = "\n";
        Case currentC;

        for(int i=1; i<=nbColumn; i++){

            s += " " + i + "    ";
        }

        s += "\n\n";

        for(int i=0; i<this.nbRow; i++){

            currentC = this.plate.get(i*2);

            for(int j=1; j<nbColumn; j++){
                
                s += currentC.toString() + " | ";
                currentC = currentC.getVoisin(Direction.DROITE);
            }

            s += currentC.toString() + " |   " + (i+1) + "\n";
            //s += " |   " + i+1 + "\n";
        }

        return s;
    }
}
