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

    public int getEtat(){

        return this.etat;
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

            if(this.getVoisin(d.next()) != null){

                c.createLink(d.next(), this.getVoisin(d.next()).getVoisin(d), true);
                c.createLink(d, this.getVoisin(d.next()).getVoisin(d.previous()), true);
            }

            if(this.getVoisin(d.previous()) != null){

                c.createLink(d.previous(), this.getVoisin(d.previous()).getVoisin(d), true);
                c.createLink(d, this.getVoisin(d.previous()).getVoisin(d.next()), true);
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

    public void setFlag(){
        
        if(this.etat == 0){

            this.etat = -1;
        }
    }

    public void removeFlag(){

        if(this.etat == -1){

            this.etat = 0;
        }
    }

    public boolean isFlaged(){

        return this.etat == -1;
    }

    @Override
    public String toString(){
        
        if(this.mine == true && etat == 1){

            return "\033[41m M \033[0m";
        }
        if(this.etat == -1){

            return "\033[43m X \033[0m";
        }
        else if(this.etat == 0){

            return "\033[44m ? \033[0m";
        }
        else{

            return String.format("\033[42m %s \033[0m", Integer.toString(countNextMines()));
        }
    }

    public String toString2(){  //pour le jeu graphique
        
        if(this.mine == true && etat == 1){

            return "M";
        }
        if(this.etat == -1){

            return "X";
        }
        else if(this.etat == 0){

            return "";
        }
        else{

            return String.format(Integer.toString(countNextMines()));
        }
    }

}
