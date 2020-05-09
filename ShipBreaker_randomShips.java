import java.awt.* ;
import java.awt.event.*;
import javax.swing.* ;

class Game extends JFrame {

    private JLabel result, index ;
    private boolean[] shipLocate ;
    private JButton[] blockLocate;
    private int column, shipNum, shipLength ;
    private int hitCounter ;

    public Game( int col, int num, int length ) {

        column = col ; shipNum = num ; shipLength = length ;
        hitCounter = 0 ;

        shipLocate = new boolean[column*column] ;
        blockLocate = new JButton[column*column] ;

        setLayout( new GridLayout( column + 1, column + 1 ) );

        result = new JLabel( "" ) ;
        add( result ) ;

        for ( int x = 0 ; x < column ; x++ ) {
            index = new JLabel( String.valueOf( x ) ) ;
            add( index ) ;
        } // for

        Event e = new Event() ;

        for ( int i = 0, y = 0 ; y < column ; y++ ) {
            index = new JLabel( String.valueOf( y ) ) ;
            add( index ) ;
            for ( int x = 0 ; x < column ; x++, i++ ) {
                shipLocate[i] = false ; // initial boolean[] shipLocate
                blockLocate[i] = new JButton() ;
                add( blockLocate[i] ) ;
                blockLocate[i].addActionListener( e );
            } // for
        } // for

        if ( ! SetShipLocation() ) {
            JOptionPane.showMessageDialog( null, "Fail to set ships !", "ERROR", JOptionPane.ERROR_MESSAGE );
            System.exit( 0 );
        } // if

    } // Game()

    class Event implements ActionListener {
        public void actionPerformed( ActionEvent e ) {

            int target = 0 ;

            for ( ; target < column * column ; target++ ) {
                if ( e.getSource() == blockLocate[target] ) {
                    break ;
                } // if
            } // for

            if ( shipLocate[target] == true ) { // Hit

                if ( e.getActionCommand().equals( "H" ) ) { // Already Hit
                    result.setText( "Already Hit !" );
                    result.setForeground( Color.RED );
                } // if
                else {
                    blockLocate[target].setText( "H" );
                    result.setText( "Hit !" );
                    result.setForeground( Color.BLUE );
                    hitCounter++ ;
                    if ( hitCounter == shipNum * shipLength ) {
                        JOptionPane.showMessageDialog( null, "Complete !", "END", JOptionPane.PLAIN_MESSAGE );
                        dispose();
                    } // if
                } // else

            } // if
            else { // Not Hit
                result.setText( "Not Hit !" );
                result.setForeground( Color.RED );
            } // else

        } // actionPerformed()
    } // class Event

    private int dir ;

    boolean SetShipLocation() {

        int size = column * column ;

        /* random setting */

        int[] randomBox = GetRandom( size ) ;

        /* location setting */

        dir = -1 ;

        for ( int i = 0, j = 0 ; i < shipNum * shipLength ; i += shipLength, dir = -1 ) {

            for ( ; j < randomBox.length ; j++ ) {
                if ( shipLocate[randomBox[j]] == false && SetDirection( randomBox[j], size ) ) {
                    break ;
                } // if
            } // for

            switch ( dir ) {
                case 0 : // up
                    shipLocate[randomBox[j]] = shipLocate[randomBox[j]-column] = shipLocate[randomBox[j]-(2*column)] = true ;
                    break ;
                case 1 : // down
                    shipLocate[randomBox[j]] = shipLocate[randomBox[j]+column] = shipLocate[randomBox[j]+(2*column)] = true ;
                    break ;
                case 2 : // left
                    shipLocate[randomBox[j]] = shipLocate[randomBox[j]-1] = shipLocate[randomBox[j]-2] = true ;
                    break ;
                case 3 : // right
                    shipLocate[randomBox[j]] = shipLocate[randomBox[j]+1] = shipLocate[randomBox[j]+2] = true ;
                    break ;
                default :
                    return false ;
            } // switch

        } // for

        return true ;

    } // SetShipLocation()

    boolean SetDirection( int cur, int size ) {

        int[] dirBox = GetRandom( 4 ) ; // 0 1 2 3 random
        int curLevel = -1 ;

        for ( int i = 0 ; i < dirBox.length ; i++ ) {

            switch ( dirBox[i] ) {
                case 0 : // up
                    if ( cur-column >= 0 && shipLocate[cur-column] == false ) {
                        if ( cur-(2*column) >= 0 && shipLocate[cur-(2*column)] == false ) {
                            dir = 0 ;
                            return true ;
                        } // if
                    } // if
                    break ;
                case 1 : // down
                    if ( cur+column < size && shipLocate[cur+column] == false ) {
                        if ( cur+(2*column) < size && shipLocate[cur+(2*column)] == false ) {
                            dir = 1 ;
                            return true ;
                        } // if
                    } // if
                    break ;
                case 2 : // left
                    curLevel = LevelOf( cur, size ) ;
                    if ( cur-1 >= 0 && curLevel == LevelOf( cur-1, size ) && shipLocate[cur-1] == false ) {
                        if ( cur-2 >= 0 && curLevel == LevelOf( cur-2, size ) &&  shipLocate[cur-2] == false ) {
                            dir = 2 ;
                            return true ;
                        } // if
                    } // if
                    break ;
                case 3 : // right
                    curLevel = LevelOf( cur, size ) ;
                    if ( cur+1 < size && curLevel == LevelOf( cur+1, size ) && shipLocate[cur+1] == false ) {
                        if ( cur+2 < size && curLevel == LevelOf( cur+2, size ) && shipLocate[cur+2] == false ) {
                            dir = 3 ;
                            return true ;
                        } // if
                    } // if
                    break ;
            } // switch

        } // for

        return false ;

    } // SetDirection()

    int[] GetRandom( int size ) { // [ 0, size-1 ]

        int[] temp = new int[size] ;

        for ( int i = 0 ; i < size ; i++ ) {
            temp[i] = i ;
        } // for

        int g = temp.length + 1 ;

        for ( int i = 1 ; i < g ; i++ ) {
            int j = (int)(Math.random()*1000) % ( g - i ) + i ;
            int t = temp[j-1] ;
            temp[j-1] = temp[i-1] ;
            temp[i-1] = t ;
        } // for

        return temp ;

    } // GetRandom()

    int LevelOf( int num, int size ) {

        for ( int last = column - 1, level = 1 ; last < size ; last += column, level++ ) {
            if ( num <= last ) {
                return level ;
            } // if
        } // for

        return -1 ;

    } // LevelOf()

} // class Game

public class ShipBreaker_randomShips extends JFrame {

    private int column = 7, shipNum = 3, shipLength = 3  ;
    private JLabel intro ;
    private JButton newGame ;
    private GridBagConstraints gbc;

    public ShipBreaker_randomShips() {

        setLayout( new GridBagLayout() );
        gbc = new GridBagConstraints() ;

        intro = new JLabel( "There are " + shipNum + " ships."  ) ;
        gbc.fill = GridBagConstraints.HORIZONTAL ;
        gbc.gridx = 0 ;
        gbc.gridy = 0 ;
        add( intro, gbc ) ;

        intro = new JLabel( "Each ship contain " + shipLength + " blocks."  ) ;
        gbc.fill = GridBagConstraints.HORIZONTAL ;
        gbc.gridx = 0 ;
        gbc.gridy = 1 ;
        add( intro, gbc ) ;

        newGame = new JButton( "New Game" ) ;
        gbc.fill = GridBagConstraints.HORIZONTAL ;
        gbc.gridx = 0 ;
        gbc.gridy = 3 ;
        add( newGame, gbc ) ;

        Event e = new Event() ;
        newGame.addActionListener( e );

    } // ShipBreaker_randomShips()

    class Event implements ActionListener {
        public void actionPerformed( ActionEvent e ) {

            Game gameGui = new Game( column, shipNum, shipLength ) ;
            gameGui.setVisible( true );
            gameGui.setSize( 500, 500 );
            gameGui.setTitle( "Game" ) ;

        } // actionPerformed()
    } // class Event

    public static void main( String[] args ) {
        ShipBreaker_randomShips gui = new ShipBreaker_randomShips() ;
        gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        gui.setVisible( true );
        gui.setSize( 200, 200 ); ;
        gui.setTitle( "Ship Breaker" ) ;
    } // main()

} // class ShipBreaker_randomShips
