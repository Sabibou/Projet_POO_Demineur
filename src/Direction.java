public enum Direction{

    GAUCHE, HAUT_GAUCHE, HAUT, HAUT_DROITE, DROITE, BAS_DROITE, BAS, BAS_GAUCHE;

    public Direction opposite(){

        Direction[] array = values();

        return array[(ordinal() + 4) % 8];
    }

    private int mod(int n){

        return (((n % 8) + 8) % 8);
    }

    public Direction next(){

        Direction[] array = values();

        return array[mod((ordinal() + 1))];
    }

    public Direction next(int nb){

        Direction[] array = values();

        return array[mod((ordinal() + nb))];
    }

    public Direction previous(){

        Direction[] array = values();

        return array[mod((ordinal() - 1))];
    }

    public Direction previous(int nb){

        Direction[] array = values();

        return array[mod((ordinal() - nb))];
    }
}