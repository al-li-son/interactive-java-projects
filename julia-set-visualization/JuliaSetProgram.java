import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener
{
    JFrame frame;
    JPanel scrollerPanel, westPanel, southPanel, eastPanel, exponentPanel;
    JLabel aLabel, bLabel, zoomLabel, satLabel, brightLabel, colLabel, exponentLabel;
    JTextField aField, bField, zoomField, satField, brightField, colField, exponentField;

    JScrollBar aBar, bBar, zoomBar, moveXBar, moveYBar, satBar, brightBar, colBar;
    double zoom = 1, moveX = 0, moveY = 0, saturation = 1, brightness = 1;
    float maxItr = 300, colShift = 1;
    ComplexNumber c = new ComplexNumber(-1.146, -0.206);

    JButton resetButton;

    int w, h, exponent = 2;
    BufferedImage image;
    
    GridLayout twoRowLayout, threeRowLayout, sixRowLayout, labelLayout;

    public JuliaSetProgram()
    {
        frame = new JFrame("Julia Set Program");
        frame.add(this);

        aBar = new JScrollBar(JScrollBar.HORIZONTAL, (int)(c.getReal() * 1000), 0, -6000, 6000);
        aBar.addAdjustmentListener(this);
        aBar.setUnitIncrement(1);

        bBar = new JScrollBar(JScrollBar.HORIZONTAL, (int)(c.getImaginary() * 1000), 0, -6000, 6000);
        bBar.addAdjustmentListener(this);
        bBar.setUnitIncrement(1);

        zoomBar = new JScrollBar(JScrollBar.HORIZONTAL, 10, 1, 1, 1000);
        zoomBar.addAdjustmentListener(this);
        zoomBar.setUnitIncrement(10);

        moveXBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -5000, 5000);
        moveXBar.addAdjustmentListener(this);
        moveXBar.setUnitIncrement(100);

        moveYBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, -5000, 5000);
        moveYBar.addAdjustmentListener(this);
        moveYBar.setUnitIncrement(100);

        satBar = new JScrollBar(JScrollBar.HORIZONTAL, 1000, 0, 0, 1000);
        satBar.addAdjustmentListener(this);
        satBar.setUnitIncrement(10);

        brightBar = new JScrollBar(JScrollBar.HORIZONTAL, 1000, 0, 0, 1000);
        brightBar.addAdjustmentListener(this);
        brightBar.setUnitIncrement(10);     
        
        colBar = new JScrollBar(JScrollBar.HORIZONTAL, 1000, 0, 0, 1000);
        colBar.addAdjustmentListener(this);
        colBar.setUnitIncrement(10);

        frame.add(moveXBar, BorderLayout.NORTH);
        frame.add(moveYBar, BorderLayout.EAST);

        sixRowLayout = new GridLayout(6,1);
        scrollerPanel = new JPanel();
        scrollerPanel.setLayout(sixRowLayout);
        scrollerPanel.add(aBar);
        scrollerPanel.add(bBar);
        scrollerPanel.add(zoomBar);
        scrollerPanel.add(satBar);
        scrollerPanel.add(brightBar);
        scrollerPanel.add(colBar);

        aLabel = new JLabel("A: ", SwingConstants.RIGHT);
        bLabel = new JLabel("B: ", SwingConstants.RIGHT);
        zoomLabel = new JLabel("Zoom: ", SwingConstants.RIGHT);
        satLabel = new JLabel("Saturation: ", SwingConstants.RIGHT);
        brightLabel = new JLabel("Brightness: ", SwingConstants.RIGHT);
        colLabel = new JLabel("Color Shift: ", SwingConstants.RIGHT);

        aField = new JTextField("" + c.getReal());
        aField.setEditable(true);
        aField.addActionListener(this);
        bField = new JTextField("" + c.getImaginary());
        bField.setEditable(true);
        bField.addActionListener(this);
        zoomField = new JTextField("" + zoom);
        zoomField.setEditable(true);
        zoomField.addActionListener(this);
        satField = new JTextField("" + saturation);
        satField.setEditable(true);
        satField.addActionListener(this);
        brightField = new JTextField("" + brightness);
        brightField.setEditable(true);
        brightField.addActionListener(this);      
        colField = new JTextField("" + colShift);
        colField.setEditable(true);
        colField.addActionListener(this);  

        labelLayout = new GridLayout(6,2);
        westPanel = new JPanel();
        westPanel.setLayout(labelLayout);
        westPanel.add(aLabel);
        westPanel.add(aField);
        westPanel.add(bLabel);
        westPanel.add(bField);
        westPanel.add(zoomLabel);
        westPanel.add(zoomField);
        westPanel.add(satLabel);
        westPanel.add(satField);
        westPanel.add(brightLabel);
        westPanel.add(brightField);
        westPanel.add(colLabel);
        westPanel.add(colField);
        
        exponentLabel = new JLabel("Exponent: ");
        exponentField = new JTextField("" + exponent);
        exponentField.setEditable(true);
        exponentField.addActionListener(this);
        exponentPanel = new JPanel();
        exponentPanel.add(exponentLabel, BorderLayout.WEST);
        exponentPanel.add(exponentField, BorderLayout.CENTER);

        eastPanel = new JPanel();
        twoRowLayout = new GridLayout(2,1);
        eastPanel.setLayout(twoRowLayout);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        eastPanel.add(exponentPanel);
        eastPanel.add(resetButton);

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout(5,5));
        southPanel.add(scrollerPanel, BorderLayout.CENTER); 
        southPanel.add(westPanel, BorderLayout.WEST);
        southPanel.add(eastPanel, BorderLayout.EAST);   
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setSize(1200, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(Color.HSBtoRGB(colShift, (float)saturation, (float)brightness)));
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        complexJulia();
        g.drawImage(image, 0, 0, null);
    } 
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if(e.getSource() == aBar)
        {
            c.setReal(aBar.getValue()/1000.0);
            aField.setText("" + c.getReal());
        }
        if(e.getSource() == bBar)
        {
            c.setImaginary(bBar.getValue()/1000.0);
            bField.setText("" + c.getImaginary());
        }      
        if(e.getSource() == zoomBar)
        {
            zoom = zoomBar.getValue()/10.0;
            zoomField.setText(""+zoom);
            moveXBar.setMinimum(moveXBar.getValue() - (int)(2500/zoom));
            moveXBar.setMaximum(moveXBar.getValue() + (int)(2500/zoom));
            moveXBar.setUnitIncrement((int)(2500/(zoom * 2.5)));
            moveYBar.setMinimum(moveYBar.getValue() - (int)(2500/zoom));
            moveYBar.setMaximum(moveYBar.getValue() + (int)(2500/zoom));            
            moveYBar.setUnitIncrement((int)(2500/(zoom * 2.5)));
        }
        if(e.getSource() == moveXBar)
        {
            moveX = (moveXBar.getValue()/1000.0);
        }
        if(e.getSource() == moveYBar)
        {
            moveY = (moveYBar.getValue()/1000.0);
        }
        if(e.getSource() == satBar)
        {
            saturation = satBar.getValue()/1000.0;
            satField.setText("" + saturation);
        }
        if(e.getSource() == brightBar)
        {
            brightness = brightBar.getValue()/1000.0;
            brightField.setText("" + brightness);
        }    
        if(e.getSource() == colBar)
        {
            colShift = (float)(colBar.getValue()/1000.0);
            colField.setText("" + colShift);
        }
        repaint();
    }  
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == resetButton)
        {
            c = new ComplexNumber(0,0);
            aBar.setValue(0);
            bBar.setValue(0);
            aLabel.setText("A: " + c.getReal());
            bLabel.setText("B: " + c.getImaginary());
            zoom = 1;
            zoomBar.setValue(10);
            zoomLabel.setText("Zoom: " + zoom + "x");
            moveX = 0;
            moveY = 0;
            moveXBar.setMinimum(-500);
            moveXBar.setMaximum(500);
            moveXBar.setValue(0);
            moveXBar.setUnitIncrement(100);
            moveYBar.setMinimum(-500);
            moveYBar.setMaximum(500);
            moveYBar.setValue(0);
            moveYBar.setUnitIncrement(100);
            saturation = 1;
            brightness = 1;
            satBar.setValue(1000);
            brightBar.setValue(1000);
            colShift = 0;
            colBar.setValue(0);
        }
        if(e.getSource() == aField)
        {
            try
            {
                c.setReal((int)(Double.parseDouble(aField.getText()) * 1000)/1000.0);
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse double");
            }
            aBar.setValue((int)(c.getReal() * 1000));
        }
        if(e.getSource() == bField)
        {
            try
            {
                c.setImaginary((int)(Double.parseDouble(bField.getText()) * 1000)/1000.0);
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse double");
            }
            bBar.setValue((int)(c.getImaginary() * 1000));
        }
        if(e.getSource() == zoomField)
        {
            try
            {
                zoom = (int)(Double.parseDouble(zoomField.getText()) * 10)/10.0;
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse double");
            }
            zoomBar.setValue((int)(zoom * 10));
            moveXBar.setMinimum(moveXBar.getValue() - (int)(2500/zoom));
            moveXBar.setMaximum(moveXBar.getValue() + (int)(2500/zoom));
            moveXBar.setUnitIncrement((int)(2500/(zoom * 2.5)));
            moveYBar.setMinimum(moveYBar.getValue() - (int)(2500/zoom));
            moveYBar.setMaximum(moveYBar.getValue() + (int)(2500/zoom));            
            moveYBar.setUnitIncrement((int)(2500/(zoom * 2.5)));
        }        
        if(e.getSource() == satField)
        {
            try
            {
                saturation = (int)(Double.parseDouble(satField.getText()) * 1000)/1000.0;
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse double");
            }
            satBar.setValue((int)(saturation * 1000));            
        }
        if(e.getSource() == brightField)
        {
            try
            {
                brightness = (int)(Double.parseDouble(brightField.getText()) * 1000)/1000.0;
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse double");
            }
            brightBar.setValue((int)(brightness * 1000));            
        }
        if(e.getSource() == colField)
        {
            try
            {
                colShift = (float)((int)(Double.parseDouble(colField.getText()) * 1000)/1000.0);
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse double");
            }
            colBar.setValue((int)(colShift * 1000));             
        }
        if(e.getSource() == exponentField)
        {
            try
            {
                exponent = Integer.parseInt(exponentField.getText());
            }
            catch(NumberFormatException x)
            {
                System.err.println("Cannot parse integer");
            }           
        }
        repaint();
    }
    public void complexJulia()
    {
        w = frame.getWidth();
        h = (int)(.81 * frame.getHeight());
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB); 
        ComplexNumber z = new ComplexNumber(0, 0);
        for(int x=0; x<w; x++)
        {
            for(int y=0; y<h; y++)
            {
                z.setReal(((double)w/h) * ((x - w/2)/(.5 * zoom * w)) + moveX);
                z.setImaginary((y - h/2)/(.5 * zoom * h) + moveY);
                float i = maxItr;
                while(z.getReal()*z.getReal() + z.getImaginary()*z.getImaginary() < 6 && i > 0)
                {
                    ComplexNumber temp = new ComplexNumber(z.getReal(), z.getImaginary());
                    for(int j=0; j<exponent-1; j++)
                        z.setValue(z.multiply(temp));
                    z.setValue(z.add(c));
                    i--;
                }
                int c;
                float num = maxItr/i;
                if(i > 0)
                {
                    //float temp = ((num % 1) + colShift) % 1;
                    float temp = colShift*num % 1;
                    c = Color.HSBtoRGB(temp, (float)saturation, (float)brightness);
                }
                else
                    c = Color.HSBtoRGB(num, 1, 0);
                image.setRGB(x, y, c);
            }
        }
    }  
    public static void main(String[] args)
    {
        JuliaSetProgram app = new JuliaSetProgram();
    }
}