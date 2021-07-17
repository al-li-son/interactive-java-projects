import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class Minesweeper extends JPanel implements ActionListener, MouseListener
{
    JFrame frame;
    JPanel scoreboard, board, timerPanel, mineCountPanel;
    GridLayout scoreboardLayout, boardLayout;
    JMenuBar menuBar;
    JMenu game, icons, controls;
    JMenuItem beginner, intermediate, expert, defaultIcons, marioIcons, minecraftIcons;
    JTextField timer, mineCount;
    JEditorPane directions;
    JButton resetButton;
    ImageIcon reset, loseFace, winFace, pressFace, unopened, empty, flag, mine;
    ImageIcon[] numbers;
    JToggleButton[][] buttons;
    Timer t;
    Font f;
    GraphicsEnvironment ge;

    boolean started = false, end = false, won = false;

    int level = 0, time = 0, mines = 10, flags = 10, rows = 9, cols = 9, opened = 0, buttonSize = 40;
    String iconName = "";


    public Minesweeper()
    {
        frame = new JFrame("Minesweeper");
        frame.add(this);
        frame.setSize(360, 420);

        setIcons();

        menuBar = new JMenuBar();
        game = new JMenu("Game");
        icons = new JMenu("Icons");
        controls = new JMenu("Controls");

        beginner = new JMenuItem("Beginner (9x9, 10 mines)");
        beginner.addActionListener(this);
        intermediate = new JMenuItem("Intermediate (16x16, 40 mines)");
        intermediate.addActionListener(this);
        expert = new JMenuItem("Expert (16x30, 99 mines)");
        expert.addActionListener(this);

        defaultIcons = new JMenuItem("Default");
        defaultIcons.addActionListener(this);
        marioIcons = new JMenuItem("Mario");
        marioIcons.addActionListener(this);
        minecraftIcons = new JMenuItem("Minecraft");
        minecraftIcons.addActionListener(this);

        directions = new JEditorPane("text/html", "");
        directions.setText("<b>Left-click</b> an empty square to reveal it <br> <b>Right-Click</b> an empty square to flag it");
        directions.setPreferredSize(new Dimension(230, 50));
        directions.setEditable(false);
        controls.add(directions);

        game.add(beginner);
        game.add(intermediate);
        game.add(expert);
        icons.add(defaultIcons);
        icons.add(marioIcons);
        icons.add(minecraftIcons);
        menuBar.add(game);
        menuBar.add(icons);
        menuBar.add(controls);

        frame.setJMenuBar(menuBar);
        try
        {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            f = Font.createFont(Font.TRUETYPE_FONT, new File("DS-DIGI.TTF")).deriveFont(25f);
            ge.registerFont(f);
        }catch(IOException|FontFormatException e)
        {
        }

        timer = new JTextField(""+time, 3);
        timer.setEditable(false);
        timer.setFont(f);
        timer.setForeground(Color.RED);
        timer.setBackground(Color.BLACK);
        timer.setHorizontalAlignment(JTextField.RIGHT);
        mineCount = new JTextField(""+flags, 3);
        mineCount.setEditable(false);
        mineCount.setFont(f);
        mineCount.setForeground(Color.RED);
        mineCount.setBackground(Color.BLACK);
        mineCount.setHorizontalAlignment(JTextField.RIGHT);

        timerPanel = new JPanel();
        timerPanel.add(timer);
        mineCountPanel = new JPanel();
        mineCountPanel.add(mineCount);

        mineCount.setForeground(Color.RED);
        resetButton = new JButton(reset);
        resetButton.addActionListener(this);

        scoreboard = new JPanel();
        scoreboardLayout = new GridLayout(1,3);
        scoreboard.setLayout(scoreboardLayout);
        scoreboard.add(mineCountPanel);
        scoreboard.add(resetButton);
        scoreboard.add(timerPanel);

        frame.add(scoreboard, BorderLayout.NORTH);
        
        resetBoard();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void setIcons()
    {
        reset = new ImageIcon(iconName+"reset.png");
        reset = new ImageIcon(reset.getImage().getScaledInstance(buttonSize/2,buttonSize/2,Image.SCALE_SMOOTH));
        loseFace = new ImageIcon(iconName+"dead.png");
        loseFace = new ImageIcon(loseFace.getImage().getScaledInstance(buttonSize/2,buttonSize/2,Image.SCALE_SMOOTH));
        winFace = new ImageIcon(iconName+"cool.png");
        winFace = new ImageIcon(winFace.getImage().getScaledInstance(buttonSize/2,buttonSize/2,Image.SCALE_SMOOTH));
        pressFace = new ImageIcon(iconName+"shocked.png");
        pressFace = new ImageIcon(pressFace.getImage().getScaledInstance(buttonSize/2,buttonSize/2, Image.SCALE_SMOOTH));
        unopened = new ImageIcon(iconName+"unopened.png");
        unopened.setImage(unopened.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
        empty = new ImageIcon(iconName+"empty.png");
        empty.setImage(empty.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
        flag = new ImageIcon(iconName+"flag.png");
        flag.setImage(flag.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
        mine = new ImageIcon(iconName+"mine.png");
        mine.setImage(mine.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
        numbers = new ImageIcon[9];
        numbers[0] = empty;
        for(int x=1; x<9; x++)
        {
            numbers[x] = new ImageIcon(iconName+x+".png");
            numbers[x].setImage(numbers[x].getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH));
        }
    }
    public void resetBoard()
    {
        end = false;
        opened = 0;
        if(board != null)
            frame.remove(board);
        if(t != null)
            t.cancel();
        resetButton.setIcon(reset);

        if(level == 0)
        {
            buttons = new JToggleButton[9][9];
            rows = 9;
            cols = 9;
            mines = 10;
            flags = 10;
        }
        else if(level == 1)
        {
            buttons = new JToggleButton[16][16];
            rows = 16;
            cols = 16;
            mines = 40;
            flags = 40;
        }
        else
        {
            buttons = new JToggleButton[16][30];
            rows = 16;
            cols = 30;
            mines = 99;
            flags = 99;
        }

        frame.setSize(cols*buttonSize,rows*buttonSize + 60);

        time = 0;
        mineCount.setText(""+flags);
        timer.setText(""+time);

        boardLayout = new GridLayout(rows, cols);
        board = new JPanel();
        board.setLayout(boardLayout);

        for(int y=0; y<rows; y++)
        {
            for(int x=0; x<cols; x++)
            {
                buttons[y][x] = new JToggleButton(unopened);
                buttons[y][x].putClientProperty("row", y);
                buttons[y][x].putClientProperty("col", x);
                buttons[y][x].putClientProperty("state", 0);
                buttons[y][x].putClientProperty("selected", 0);
                buttons[y][x].setSelectedIcon(empty);
                buttons[y][x].addMouseListener(this);
            }
        }

        for(int y=0; y<buttons.length; y++)
        {
            for(int x=0; x<buttons[0].length; x++)
            {
                board.add(buttons[y][x]);
            }
        }        

        frame.add(board, BorderLayout.CENTER);
        frame.revalidate();
    }
    public void setBoard(int startR, int startC)
    {
        int count = 0;
        while(count < mines)
        {
            int r = (int)(Math.random()*rows);
            int c = (int)(Math.random()*cols);
            if(Integer.parseInt("" + buttons[r][c].getClientProperty("state")) == 0 && !((r == startR && c >= startC-1 && c <= startC+1) || (r == startR-1 && c >= startC-1 && c <= startC+1) || (r == startR+1 && c >= startC-1 && c <= startC+1)))
            {
                buttons[r][c].setSelectedIcon(mine);
                buttons[r][c].putClientProperty("state", 9);
                count++;
            }
        }
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                if(Integer.parseInt("" + buttons[r][c].getClientProperty("state")) == 0)
                {
                    int num = 0;
                    for(int x=-1; x<=1; x++)
                    {
                        for(int y=-1; y<=1; y++)
                        {
                            try
                            {
                                if(Integer.parseInt(""+buttons[r+x][c+y].getClientProperty("state")) == 9)
                                    num++;
                            } catch (ArrayIndexOutOfBoundsException e)
                            {
                            }
                        }
                    }
                    
                    buttons[r][c].putClientProperty("state", num);
                    buttons[r][c].setSelectedIcon(numbers[num]);
                }
            }
        }
    }
    public void expand(int r, int c)
    {
        int state = Integer.parseInt(""+buttons[r][c].getClientProperty("state"));
        buttons[r][c].setSelected(true);
        if(Integer.parseInt(""+buttons[r][c].getClientProperty("selected")) == 0)
            opened++;
        buttons[r][c].putClientProperty("selected", 1);
        
        if(state == 0)
        {
            for(int x=-1; x<=1; x++)
            {
                for(int y=-1; y<=1; y++)
                {
                    try
                    {
                        if(!buttons[r+x][c+y].isSelected() && !buttons[r+x][c+y].getIcon().equals(flag) && state != 9)
                            expand(r+x, c+y);
                    } catch (ArrayIndexOutOfBoundsException e)
                    {
                    }
                }                
            }
        }
    }
    public void ending()
    {
        resetButton.setIcon(loseFace);
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                if(Integer.parseInt(""+buttons[r][c].getClientProperty("state")) == 9)
                    buttons[r][c].setDisabledIcon(mine);   
                else
                    buttons[r][c].setDisabledIcon(unopened);
                buttons[r][c].setEnabled(false);
            }
        }
    }
    public void winning()
    {
        resetButton.setIcon(winFace);
        for(int r=0; r<rows; r++)
        {
            for(int c=0; c<cols; c++)
            {
                if(buttons[r][c].isEnabled())
                {
                    buttons[r][c].setSelected(false);
                    buttons[r][c].setDisabledIcon(buttons[r][c].getSelectedIcon());
                }
                buttons[r][c].setEnabled(false);
            }
        }        
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == beginner)
        {
            level = 0;
            resetBoard();
            started = false;
        }
        if(e.getSource() == intermediate)
        {
            level = 1;
            resetBoard();
            started = false;
        }
        if(e.getSource() == expert)
        {
            level = 2;
            resetBoard();
            started = false;
        }
        if(e.getSource() == resetButton)
        {
            resetBoard();
            started = false;
        }
        if(e.getSource() == defaultIcons)
        {
            iconName = "";
            setIcons();
            resetBoard();
            started = false;
        }
        if(e.getSource() == marioIcons)
        {
            iconName = "mario_";
            setIcons();
            resetBoard();
            started = false;
        }
        if(e.getSource() == minecraftIcons)
        {
            iconName = "minecraft_";
            setIcons();
            resetBoard();
            started = false;
        }
    }
    public void mouseEntered(MouseEvent e)
    {

    }
    public void mousePressed(MouseEvent e)
    {
        if(((JToggleButton)e.getComponent()).isEnabled() && !((JToggleButton)e.getComponent()).isSelected() && SwingUtilities.isLeftMouseButton(e))
            resetButton.setIcon(pressFace);
    }
    public void mouseClicked(MouseEvent e)
    {
        
    }
    public void mouseReleased(MouseEvent e)
    {
        resetButton.setIcon(reset);
        if(!started)
        {
            started = true;
            t = new Timer();
            t.schedule(new UpdateTimer(), 0, 1000);
            int r = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("row"));
            int c = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("col"));
            setBoard(r,c);
        }
        if(SwingUtilities.isLeftMouseButton(e) && !((JToggleButton)e.getComponent()).getIcon().equals(flag) && !end)
        {
            if(Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("state")) == 9)
            {
                end = true;
                ending();
            }
            else
            {
                int r = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("row"));
                int c = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("col"));
                expand(r,c);
            }
        }
        else if (SwingUtilities.isRightMouseButton(e) && !((JToggleButton)e.getComponent()).isSelected() && !end)
        {
            if(((JToggleButton)e.getComponent()).getIcon().equals(flag))
            {
                ((JToggleButton)e.getComponent()).setIcon(unopened);
                ((JToggleButton)e.getComponent()).setEnabled(true);
                flags++;
                mineCount.setText(""+flags);
            }
            else if(flags > 0)
            {
                ((JToggleButton)e.getComponent()).setIcon(flag);
                ((JToggleButton)e.getComponent()).setDisabledIcon(flag);
                ((JToggleButton)e.getComponent()).setEnabled(false);
                flags--;
                mineCount.setText(""+flags);
            }
            ((JToggleButton)e.getComponent()).setSelected(false);
        }
        if(flags == 0 && opened == rows*cols - mines)
        {
            end = true;
            won = true;
            winning();
        }
    }
    public void mouseExited(MouseEvent e)
    {

    }
    public void itemStateChanged(ItemEvent e)
    {

    }
    public class UpdateTimer extends TimerTask
    {
        public void run()
        {
            if(!end && time < 1000)
            {
                time++;
                timer.setText("" + time);
            }
        }
    }
    public static void main(String[] args)
    {
        Minesweeper app = new Minesweeper();
    }
}