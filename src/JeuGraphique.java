import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import javax.swing.event.*;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.Dimension;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.BorderLayout;

public class JeuGraphique extends JFrame implements ActionListener, MouseInputListener{
    
    private GridLayout layout;
    private Grille plate;
    private JButton[][] buttons;
    private boolean[][] clickable;
    JMenuItem newGameButton;
    JLabel mineLabel;
    JMenuBar mb;

    public JeuGraphique(int nbRow, int nbColumn, int pourcentMine){

        this.plate = new Grille(nbRow, nbColumn, (float)pourcentMine/100);
        this.plate.fillMine();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(2000,2000);
        layout = new GridLayout(nbRow, nbColumn);
        this.setLayout(layout);
        layout.minimumLayoutSize(this);

        newGameButton = new JMenuItem("new game");
        mineLabel = new JLabel("mines: " + this.plate.getNbMines());
        mb = new JMenuBar();
        newGameButton.addActionListener(this);
        mb.add(newGameButton);
        mb.add(mineLabel);
        this.setJMenuBar(mb);

        buttons = new JButton[nbRow][nbColumn];
        clickable = new boolean[nbRow][nbColumn];

        setup();

        for(int row=0; row<this.plate.getNbRow(); row++){

            for(int column=0; column<this.plate.getNbColumn(); column++){

                this.add(buttons[row][column]);
            }
        }
        this.pack();
        this.setVisible(true);
    }

    private void setup(){

        for(int row=0; row<this.plate.getNbRow(); row++){

            for(int column=0; column<this.plate.getNbColumn(); column++){

                clickable[row][column] = true;
                buttons[row][column] = new JButton();
                buttons[row][column].setPreferredSize(new Dimension(45, 45));
                buttons[row][column].addActionListener(this);
                buttons[row][column].addMouseListener(this);
            }
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
                        this.plate.uncover(row, column);
                    }
                    

                    else if(e.getButton() == MouseEvent.BUTTON3){

                        if(!this.plate.getCase(row, column).isFlaged()){

                            this.plate.setCaseFlag(row, column);
                            buttons[row][column].setText(this.plate.getCase(row, column).toString2());
                        }

                        else{

                            this.plate.removeCaseFlag(row, column);
                            buttons[row][column].setText(this.plate.getCase(row, column).toString2());
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

        for (int row = 0; row < this.plate.getNbRow(); row++) {

            for (int column = 0; column < this.plate.getNbColumn(); column++) {

                if(this.plate.getCase(row, column).isDiscovered() || this.plate.getCase(row, column).isFlaged()){

                    buttons[row][column].setText(this.plate.getCase(row, column).toString2());
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
