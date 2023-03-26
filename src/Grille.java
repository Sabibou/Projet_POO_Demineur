import java.util.Random;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
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

    public Case getCase(int row, int column){

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

    public int uncover(int row, int column){

        Case c = getCase(row, column);

        int nbUncover = c.uncover();

        this.discovered += nbUncover; 
        
        return nbUncover;
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
    
    
    public void load(){

        try {
            
            File fichier = new File("chemin/vers/fichier.txt");

            
            FileReader fileReader = new FileReader(fichier);

            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            this.nbRow = Integer.parseInt(bufferedReader.readLine());
            this.nbColumn = Integer.parseInt(bufferedReader.readLine());
            
            for(int i=0; i<nbRow; i++){

                
            }

            // Fermer les ressources
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    @Override
    public String toString(){

        String s = "\n";
        Case currentC;

        for(int i=1; i<=nbColumn; i++){

            s += i + "   ";
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
