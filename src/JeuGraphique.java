import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import javax.swing.event.*;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.color.*;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException; 

public class JeuGraphique extends JFrame implements ActionListener, MouseInputListener{
    
    private GridLayout layout;
    private Grille plate;
    private JButton[][] buttons;
    private boolean[][] clickable;
    JMenuItem newGameButton;
    JMenuItem save;
    JMenuItem load;
    JLabel mineLabel;
    JMenuBar mb;
    JMenu menu;
    JPanel p; 
    Font f = new Font("SERIF", 1, 40);
    Font f1 = new Font("SERIF", 1, 30);
    JTextField inputNbRow;
    JTextField inputNbColumn;
    JTextField pourcentNbMine;
    int state;
    JLabel text;

    public JeuGraphique(int nbRow, int nbColumn, int pourcentMine){

        this.plate = new Grille(nbRow, nbColumn, (float)pourcentMine/100);
        this.plate.fillMine();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(2000, 2000));;
        
        p = new JPanel();

        setup();

        this.setVisible(true);
    }

    private void setup(){

        int nbRow = this.plate.getNbRow();
        int nbColumn = this.plate.getNbColumn();

        state = 0;

        layout = new GridLayout(nbRow, nbColumn);
        p.setLayout(layout);
        layout.minimumLayoutSize(p);

        buttons = new JButton[nbRow][nbColumn];
        clickable = new boolean[nbRow][nbColumn];

        for(int row=0; row<this.plate.getNbRow(); row++){

            for(int column=0; column<this.plate.getNbColumn(); column++){

                clickable[row][column] = true;
                buttons[row][column] = new JButton();
                buttons[row][column].setPreferredSize(new Dimension(45, 45));
                buttons[row][column].addActionListener(this);
                buttons[row][column].addMouseListener(this);
            }
        }

        for(int row=0; row<this.plate.getNbRow(); row++){

            for(int column=0; column<this.plate.getNbColumn(); column++){

                p.add(buttons[row][column]);
            }
        }

        menu = new JMenu("Menu");
        newGameButton = new JMenuItem("new game");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        mineLabel = new JLabel("mines: " + this.plate.getNbMines());
        inputNbRow = new JTextField("Nb lignes : 1-20");
        inputNbColumn = new JTextField("Nb colonnes : 1-20");
        pourcentNbMine = new JTextField("Pourcentage mines : 0-100");

        menu.setFont(f);
        newGameButton.setFont(f);
        save.setFont(f);
        load.setFont(f);
        mineLabel.setFont(f);
        inputNbRow.setFont(f1);
        inputNbColumn.setFont(f1);
        pourcentNbMine.setFont(f1);

        mb = new JMenuBar();
        newGameButton.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this);
        menu.add(newGameButton);
        menu.add(save);
        menu.add(load);
        mb.add(menu);
        mb.add(inputNbRow);
        mb.add(inputNbColumn);
        mb.add(pourcentNbMine);
        mb.add(mineLabel);
        this.setJMenuBar(mb);

        this.add(p);
        this.pack();
    }

    private boolean verifyInput(){

        String nbRow = this.inputNbRow.getText();
        String nbColumn = this.inputNbColumn.getText();
        String nbMines = this.pourcentNbMine.getText();

        String regex1 = "^(1?[0-9]|20)$";
        String regex2 = "^(100|[1-9][0-9]?|0)$";

        return nbColumn.matches(regex1) && nbRow.matches(regex1) && nbMines.matches(regex2);
    }

    private void setup(int nbRow, int nbColumn, int pourcentMine){

        if(verifyInput()){

            this.remove(p);
            p = new JPanel();
            

            this.plate = new Grille(Integer.parseInt(inputNbRow.getText()), Integer.parseInt(inputNbColumn.getText()), (float)Integer.parseInt(pourcentNbMine.getText())/100);
            this.plate.fillMine();

            setup();
        }
    }

    private void load(){

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

            this.remove(p);
            p = new JPanel();

            setup();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mouseEntered(MouseEvent e) {
    }
 
    public void mouseExited(MouseEvent e) {
    }
 
    public void mousePressed(MouseEvent e) {

        for (int row = 0; row < this.plate.getNbRow(); row++) {

            for (int column = 0; column < this.plate.getNbColumn(); column++) {

                if (e.getSource() == buttons[row][column]) {

                    if(e.getButton() == MouseEvent.BUTTON1 && clickable[row][column]){

                        clickable[row][column] = !clickable[row][column]; //le bouton ne peut être plus cliquable
                        this.state = this.plate.play(row, column);
                    }
                    

                    else if(e.getButton() == MouseEvent.BUTTON3){

                        if(!this.plate.getCase(row, column).isFlaged()){

                            this.plate.setCaseFlag(row, column);
                            buttons[row][column].setText(this.plate.getCase(row, column).toString2());
                            buttons[row][column].setFont(f);
                        }

                        else{

                            this.plate.removeCaseFlag(row, column);
                            buttons[row][column].setText(this.plate.getCase(row, column).toString2());
                            buttons[row][column].setFont(f);
                        }
                    }
                }
            }
        }
    }
 
    public void mouseReleased(MouseEvent e) {
    }
 
    public void mouseClicked(MouseEvent e) {
    }

    private void printButton(int row, int column){

        if(this.plate.getCase(row, column).isDiscovered()){
                        
            String s = this.plate.getCase(row, column).toString2();
            buttons[row][column].setText(s);
            buttons[row][column].setFont(f);
            buttons[row][column].setBackground(Color.white);
            
            if(s.equals("0")){

                buttons[row][column].setText("");
            }
            else if(s.equals("M")){

                if(this.state == 1){

                    buttons[row][column].setText("D");
                }
                else{

                    buttons[row][column].setBackground(Color.red);
                }
            }
            else if(Integer.parseInt(s) < 3){

                buttons[row][column].setBackground(Color.GREEN);
            }
            else if(Integer.parseInt(s) < 6){

                buttons[row][column].setBackground(Color.ORANGE);
            }
            else{

                buttons[row][column].setBackground(Color.red);
            }
            
        }
    }
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == newGameButton){

            this.setup(20, 20, 20);
        }
        else if(e.getSource() == save){

            this.plate.save();
        }
        else if(e.getSource() == load){

            this.load();
        }
        else{

            for (int row = 0; row < this.plate.getNbRow(); row++) {

                for (int column = 0; column < this.plate.getNbColumn(); column++) {
    
                    this.printButton(row, column);
                    
                }
            }
        }

        if(state != 0){

            for (int row = 0; row < this.plate.getNbRow(); row++) {

                for (int column = 0; column < this.plate.getNbColumn(); column++) {

                    this.plate.play(row, column);
                    this.printButton(row, column);
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}

    public static void main(String[] args){

        new JeuGraphique(10, 10, 20);
    }

}

