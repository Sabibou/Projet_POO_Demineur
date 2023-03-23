import java.io.Console;

public class Jeu {
    
    private Console cons;
    private Grille plate;

    private Jeu(){

        this.cons = System.console();
    }

    private void initPlate(int dimension, int pourcentMine){

        this.plate = new Grille(dimension, (float)pourcentMine/100);
        this.plate.fillMine();
    }

    private void play(){

        int dimension = Integer.parseInt(cons.readLine("Choississez une dimension :"));
        int pourcentMine = Integer.parseInt(cons.readLine("Rentrez le pourcentage de mines (sous forme d'entier) : "));

        this.initPlate(dimension, pourcentMine);

        int state = 0; //0:jeu continu, -1:perdu, 1:gagne
        int x;
        int y;
        String action;

        while(state == 0){

            cons.format(this.plate.toString());

            action = cons.readLine("\nQue faire ?\n j : jouer coup\n s : sauvegarder\n");

            if(action.equals("j")){

                do{

                    x = Integer.parseInt(cons.readLine("\nRentrez la ligne :"));
                }while(x > dimension && x < 1);
    
                do{
    
                    y = Integer.parseInt(cons.readLine("\nRentrez la colonne :"));
                }while(y > dimension && y < 1);

                state = this.plate.uncover(x, y) > 0 ? this.plate.isAllDiscorvered() : -1;
            }
            else{

                this.plate.save();
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
