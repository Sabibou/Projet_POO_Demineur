import java.util.EnumMap;

public class Case{

    private boolean mine;
    private int etat;  //-1 : marqué, 0: caché, 1: découverte
    private EnumMap<Direction, Case> voisins = new EnumMap<>(Direction.class);

    public Case(boolean mine, int etat){

        this.mine = mine;
        this.etat = etat;

        Direction[] val = Direction.values();

        for(Direction d : val){

            this.voisins.put(d, null);
        }
    }

    public Case getVoisin(Direction d){

        return this.voisins.get(d);
    }

    private void createLink(Direction d, Case c, boolean recursive){

        if(c != null){

            this.voisins.put(d, c);
            if(recursive){

                c.createLink(d.opposite(), this, false);
            }
        }
    }

    public void addVoisins(Direction d, Case c){

        this.createLink(d, c, true);

        for(int i=0; i<2; i++){

            c.createLink(d.opposite().next(i+1), this.getVoisin(d.previous(2-i)), true);
            c.createLink(d.opposite().previous(i+1), c.getVoisin(d.next(2-1)), true);
        }
    }

    public boolean isMined(){

        return this.mine;
    }

    public int countNextMines(){

        int nbMines = 0;

        for(Direction d : Direction.values()){

            if(this.voisins.containsKey(d) && this.voisins.get(d).isMined()){

                nbMines++;
            }
        }

        return nbMines;
    }

    @Override
    public String toString(){

        if(this.mine == true){

            return "M";
        }
        if(this.etat == -1){

            return "X";
        }
        else if(this.etat == 0){

            return "?";
        }
        else{

            return Integer.toString(countNextMines());
        }
    }



}