import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException; 

public class Jeu {
    
    private Console cons;
    private Grille plate;

    private Jeu(){

        this.cons = System.console();
    }

    private void initPlate(int nbRow, int nbColumn, int pourcentMine){

        this.plate = new Grille(nbRow, nbColumn, (float)pourcentMine/100);
        this.plate.fillMine();
    }

    private void load(){ //compter cases découvertes 

        try {
            
            File fichier = new File("./save.txt");

            
            FileReader fileReader = new FileReader(fichier);

            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int nbRow = Integer.parseInt(bufferedReader.readLine());
            int nbColumn = Integer.parseInt(bufferedReader.readLine());
            
            this.plate = new Grille(nbRow, nbColumn);

            int discovered = 0;
            int nbMines = 0;
            Case[] row = new Case[nbColumn];
            String line;
            String[] words;

            for(int i=0; i<nbRow; i++){

                for(int j=0; j<nbColumn; j++){

                    line =  bufferedReader.readLine();
                    words = line.split(" ");
                    row[j] = new Case(Boolean.parseBoolean(words[1]), Integer.parseInt(words[0]));

                    if(row[i].isDiscovered()){

                        discovered++;
                    }

                    if(row[i].isMined()){

                        nbMines++;
                    }
                }

                this.plate.addRow(row);
            }

            this.plate.setDiscovered(discovered);
            this.plate.setNbMines(nbMines);

            // Fermer les ressources
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void clearConsole() {
        try{

            if(System.getProperty("os.name").contains("Windows")){

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }

            else{

                System.out.print("\033\143");
            }
        } 
        catch (IOException | InterruptedException ex){

            ex.printStackTrace();
        }
    }

    private void play(){

        cons.format("Bienvenue au jeu du demineur%n");

        String choice;

        choice = cons.readLine("N - Nouvelle partie%nC - Charger une partie%nAutre - Quitter%n");

        if(choice.equals("n") || choice.equals("N")){

            int nbRow = Integer.parseInt(cons.readLine("Choississez un nombre de lignes :"));
            int nbColumn = Integer.parseInt(cons.readLine("Choississez un nombre de colonnes :"));
            int pourcentMine = Integer.parseInt(cons.readLine("Rentrez le pourcentage de mines (sous forme d'entier) : "));

            this.initPlate(nbRow, nbColumn, pourcentMine);
        }
        else if(choice.equals("C") || choice.equals("c")){

            this.load();

        }
        else{

            System.exit(0);
        }

        int state = 0;  //0: jeu continu  -1:perdu   1:gagne 
        int row;
        int column;
        String action;

        while(state == 0){

            this.clearConsole();

            cons.format(this.plate.toString());

            cons.format("%nNombre de mines : %d%nNombre de cases marquées : %d%n", this.plate.getNbMines(), this.plate.getNbFlags());

            action = cons.readLine("%nQue faire ?%n j : jouer coup%n m : marquer/démarquer une case%n s : sauvegarder%n q : quitter%n");

            if(action.equals("j") || action.equals("J")){

                row = this.plate.getNbRow() + 1;
                column = this.plate.getNbColumn() + 1;

                while(row > this.plate.getNbRow() || row < 1){

                    row = Integer.parseInt(cons.readLine("%nRentrez la ligne :"));
                }
    
                while(column > this.plate.getNbColumn() || column < 1){
    
                    column = Integer.parseInt(cons.readLine("%nRentrez la colonne :"));
                }

                state = this.plate.play(row -1, column - 1);
            }
            else if(action.equals("m") || action.equals("M")){

                do{

                    row = Integer.parseInt(cons.readLine("%nRentrez la ligne :"));
                }while(row > this.plate.getNbRow() && row < 1);
    
                do{
    
                    column = Integer.parseInt(cons.readLine("%nRentrez la colonne :"));
                }while(column > this.plate.getNbColumn() && column < 1);

                if(this.plate.getCase(row - 1, column - 1).isFlaged()){

                    this.plate.removeCaseFlag(row - 1, column - 1);
                }
                else {

                    this.plate.setCaseFlag(row -1, column - 1);
                }
            }
            
            else if(action.equals("s") || action.equals("S")){

                this.plate.save();
            }

            else if(action.equals("q") || action.equals("Q")){

                System.exit(0);
            }
            
        }

        cons.format(this.plate.toString());

        if(state == 1){

            cons.format("%nVous avez gagné!%n%n");
        }
        else if(state == -1){

            for(int i=0;i<this.plate.getNbRow();i++){
                for(int j=0;j<this.plate.getNbColumn();j++){
                    this.plate.play(i,j);
                }
            }
            cons.format(this.plate.toString());
            cons.format("%nVous avez perdu!%n%n");

        }
    }
    
    public static void main(String[] args){

        Jeu j = new Jeu();

        j.play();

    }
}
