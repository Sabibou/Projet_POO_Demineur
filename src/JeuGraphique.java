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

public class JeuGraphique extends JFrame implements ActionListener, MouseInputListener{
    
    private GridLayout layout;
    private Grille plate;
    private JButton[][] buttons;
    private boolean[][] clickable;
    JMenuItem newGameButton;
    JLabel mineLabel;
    JMenuBar mb;
    JPanel p; 
    Font f = new Font("SERIF", 1, 40);
    JTextField inputNbRow;
    JTextField inputNbColumn;
    JTextField pourcentNbMine;
    int state;

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

        newGameButton = new JMenuItem("new game");
        mineLabel = new JLabel("mines: " + this.plate.getNbMines());
        inputNbRow = new JTextField("Nb lignes");
        inputNbColumn = new JTextField("Nb colonnes");
        pourcentNbMine = new JTextField("Pourcentage mines");

        newGameButton.setFont(f);
        mineLabel.setFont(f);

        mb = new JMenuBar();
        newGameButton.addActionListener(this);
        mb.add(newGameButton);
        mb.add(inputNbRow);
        mb.add(inputNbColumn);
        mb.add(pourcentNbMine);
        mb.add(mineLabel);
        this.setJMenuBar(mb);

        this.add(p);
        this.pack();
    }

    public void setup(int nbRow, int nbColumn, int pourcentMine){

        this.remove(p);
        p = new JPanel();

        this.plate = new Grille(Integer.parseInt(inputNbRow.getText()), Integer.parseInt(inputNbColumn.getText()), (float)Integer.parseInt(pourcentNbMine.getText())/100);
        this.plate.fillMine();

        setup();
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

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == newGameButton){

            this.setup(20, 20, 20);
        }
        else{

            for (int row = 0; row < this.plate.getNbRow(); row++) {

                for (int column = 0; column < this.plate.getNbColumn(); column++) {
    
                    if(this.plate.getCase(row, column).isDiscovered()){
                        
                        String s = this.plate.getCase(row, column).toString2();
                        buttons[row][column].setText(s);
                        buttons[row][column].setFont(f);
                        
                        if(s.equals("M") || s.equals("0")){

                            buttons[row][column].setBackground(new Color(255, 255, 255));
                        }
                        else if(Integer.parseInt(s) < 3){

                            buttons[row][column].setBackground(Color.GREEN);
                        }
                        else if(Integer.parseInt(s) < 5){

                            buttons[row][column].setBackground(Color.ORANGE);
                        }
                        else{

                            buttons[row][column].setBackground(Color.RED);
                        }
                        
                    }
                    
                }
            }
        }

        if(state == 1){

            JLabel text = new JLabel("Vous avez gagné");
            text.setFont(f);
            text.setHorizontalAlignment(SwingConstants.CENTER);

            this.remove(p);

            this.add(text, BorderLayout.CENTER);
        }
        else if(state == -1){

            JLabel text = new JLabel("Vous avez perdu");
            text.setFont(f);
            text.setHorizontalAlignment(SwingConstants.CENTER);

            this.remove(p);

            this.add(text, BorderLayout.CENTER);
        }
    }

    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}

    public static void main(String[] args){

        new JeuGraphique(10, 10, 20);
    }

}

