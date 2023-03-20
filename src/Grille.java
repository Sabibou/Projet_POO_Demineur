public class Grille {
    
    private Case beginning; //case tout en haut a gauche de la grille
    private Case end; //case tout en bas a droite
    private int nbTotalMines;

    public Grille(int dimension){

        this.beginning = new Case(false, 0);

        Case rowC = this.beginning;

        for(int i=0; i<dimension; i++){

            Case currentC = new Case(false, 0);

            rowC.addVoisins(Direction.DROITE, currentC);

            for(int j=0; j<i; j++){

                Case newC = new Case(false, 0);
                currentC.addVoisins(Direction.BAS, newC);
                currentC = newC;

            }

            if(dimension == i-1){

                this.end = currentC;
            }
            
            for(int j=0; j<i; j++){

                Case newC = new Case(false, 0);
                currentC.addVoisins(Direction.GAUCHE, newC);
                currentC = newC;

            }

        }
    }

    @Override
    public String toString(){

        String s = "";

        Case currentRow = this.beginning;

        while(currentRow != null){

            Case currentC = currentRow;

            while(currentC != null){

                s += currentC.toString() + " | ";
                currentC = currentC.getVoisin(Direction.DROITE);
            }

            s += "\n";

            currentRow = currentRow.getVoisin(Direction.BAS);
        }

        return s;
    }
}
