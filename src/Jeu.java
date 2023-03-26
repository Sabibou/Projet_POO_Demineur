import java.io.Console;

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

    private void play(){

        int nbRow = Integer.parseInt(cons.readLine("Choississez un nombre de lignes :"));
        int nbColumn = Integer.parseInt(cons.readLine("Choississez un nombre de colonnes :"));
        int pourcentMine = Integer.parseInt(cons.readLine("Rentrez le pourcentage de mines (sous forme d'entier) : "));

        this.initPlate(nbRow, nbColumn, pourcentMine);

        int state = 0; //0:jeu continu, -1:perdu, 1:gagne
        int row;
        int column;
        String action;

        while(state == 0){

            cons.format(this.plate.tostring());
            cons.format(this.plate.toString());

            action = cons.readLine("\nQue faire ?\n j : jouer coup\n s : sauvegarder\n");

            if(action.equals("j")){

                do{

                    row = Integer.parseInt(cons.readLine("\nRentrez la ligne :"));
                }while(row > nbRow && row < 1);
    
                do{
    
                    column = Integer.parseInt(cons.readLine("\nRentrez la colonne :"));
                }while(column > nbColumn && column < 1);

                state = this.plate.uncover(row - 1, column - 1) > 0 ? this.plate.isAllDiscorvered() : -1;
            }
            
            else{

                do{

                    row = Integer.parseInt(cons.readLine("\nRentrez la ligne :"));
                }while(row > nbRow && row < 1);
    
                do{
    
                    column = Integer.parseInt(cons.readLine("\nRentrez la colonne :"));
                }while(column > nbColumn && column < 1);

                this.plate.getCase(row - 1, column - 1).showVoisins();
                //this.plate.save();
            }
            
        }

        cons.format(this.plate.toString());

        if(state == 1){

            cons.format("Vous avez gagné!\n");
        }
        else{

            cons.format("Vous avez perdu!\n");
        }
    }
    
    public static void main(String[] args){

        Jeu j = new Jeu();

        j.play();

    }
}
