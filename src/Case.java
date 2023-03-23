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

        if(c!=null){
            this.createLink(d, c, true);
            
            for(int i=0; i<2;i++){

                c.createLink(d.opposite().next(1+i), this.getVoisin(d.previous(2-i)), true);
                c.createLink(d.opposite().previous(1+i), this.getVoisin(d.next(2-i)), true);

            }

            if(this.getVoisin(d.next(1)) != null){

                c.createLink(d.next(1), this.getVoisin(d.next(1)).getVoisin(d), true);
                c.createLink(d, this.getVoisin(d.next(1)).getVoisin(d.previous(1)), true);
            }

            if(this.getVoisin(d.previous(1)) != null){

                c.createLink(d.next(), this.getVoisin(d.previous()).getVoisin(d), true);
            }
            
        }
        
    }

    public void setMine(){

        this.mine = true;
    }

    public boolean isMined(){

        return this.mine;
    }

    public boolean isDiscovered(){

        return this.etat == 1;
    }

    public int countNextMines(){

        int nbMines = 0;

        for(Direction d : Direction.values()){

            Case c = this.voisins.get(d);

            if(c != null && c.isMined()){

                nbMines++;
            }
        }

        return nbMines;
    }

    public int uncover(){   //renvoie le nombre de cases découvertes; si 0 alors mine

        this.etat = 1;

        if(this.mine){

            return 0;
        }

        if(this.countNextMines() == 0){

            int nb = 1;
            Case c;

            for(Direction d : Direction.values()){

                c = this.voisins.get(d);

                if(c != null && !c.isDiscovered()){

                    nb += c.uncover();     
                }  
            }

            return nb;
        }
        else{

            return 1;
        }
    }

    @Override
    public String toString(){
        
        if(this.mine == true && etat == 1){

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
        
        //return isMined()? "1" : "0";
    }



}