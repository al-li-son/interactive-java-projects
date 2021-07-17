import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class JuliaSetExample extends JPanel implements AdjustmentListener, ActionListener
{
    JFrame frame;
    JPanel scrollerPanel, checkPanel, southPanel;

    JCheckBox[] boxes;

    JScrollBar redBar, greenBar, blueBar;
    int redValue = 0, greenValue = 0, blueValue = 0;
    
    GridLayout layout, boxLayout;
    public JuliaSetExample()
    {
        frame = new JFrame("Julia Set Program");
        frame.add(this);

        //1: starting point of scroller thing, 2: size of scroller thing, 3,4: min & max values
        redBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
        redBar.addAdjustmentListener(this);
        redBar.setUnitIncrement(10);

        greenBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
        greenBar.addAdjustmentListener(this);
        greenBar.setUnitIncrement(10);

        blueBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
        blueBar.addAdjustmentListener(this);
        blueBar.setUnitIncrement(10);

        layout = new GridLayout(3,1);
        scrollerPanel = new JPanel();
        scrollerPanel.setLayout(layout);
        scrollerPanel.add(redBar);
        scrollerPanel.add(greenBar);
        scrollerPanel.add(blueBar);

        checkPanel = new JPanel();
        boxes = new JCheckBox[4];
        boxLayout = new GridLayout(2,2);
        checkPanel.setLayout(boxLayout);
        for(int x=0; x<boxes.length; x++)
        {
            boxes[x] = new JCheckBox();
            boxes[x].addActionListener(this);
            checkPanel.add(boxes[x]);
        }

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(checkPanel, BorderLayout.WEST);
        southPanel.add(scrollerPanel, BorderLayout.CENTER);      
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setSize(1000, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(redValue, greenValue, blueValue));
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    } 
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(e.getSource() == redBar)
        {
            redValue = redBar.getValue();
        }
        if(e.getSource() == greenBar)
        {
            greenValue = greenBar.getValue();
        }
        if(e.getSource() == blueBar)
        {
            blueValue = blueBar.getValue();
        }        
        repaint();
    }  
    public void actionPerformed(ActionEvent e)
    {
        if(boxes[0].isSelected())
            redValue = 0;
        else
            redValue = redBar.getValue();
        repaint();
    }
    public static void main(String[] args)
    {
        JuliaSetExample app = new JuliaSetExample();
    }
}