package edu.carleton.mertzk;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;

/**
 * Created by keaton on 7/4/17.
 */
public class MainScreen {
    private JFrame frame;
    private JPanel panel;

    private JFrame toolBarFrame;
    private JPanel toolBarPanel;
    public float resolution=2;
    public JSlider sliderR = new JSlider(0,(int) resolution);
    public JSlider sliderG = new JSlider(0,(int) resolution);
    public JSlider sliderB = new JSlider(0,(int) resolution);
    public JSlider sliderView = new JSlider(1,0,(int) resolution,0);

    public JButton save= new JButton("Save");
    public JButton open= new JButton("Open");
    public JTextField fileNameTextField= new JTextField("picture");
    public JButton colorselect= new JButton("Switch View");
    public JTextField resolutionTextField= new JTextField(String.valueOf((int) resolution));
    public JButton upd= new JButton("Update");

    private Color colorSelected;
    public Color currentColor = Color.DARK_GRAY;
    private Color defaultColor = Color.DARK_GRAY;

    private int screen = 600;
    private int screenShift = 500;
    private int ledUpdatesPerRevolution;
    private int numberOfLedInARow;
    private int numberOfLedRows;
    private double radius = 5.0;

    public MainScreen(int rows, int height, int updates){
        this.ledUpdatesPerRevolution = updates;
        this.numberOfLedInARow = rows;
        this.numberOfLedRows = height;
        ToolBar();
        frame = new JFrame("3D POV Display Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(screen,screen);
        frame.setLocation(screenShift,0);
        JPanel panelView = new JPanel();
        panelView.setLayout(null);
        panelView.add(sliderView);
        panelView.setLocation(0,0);
        frame.add(panelView);
        frame.setVisible(true);
    }

    /*private void populatePanel() {
        double currentRadius = radius;
        double degree;
        double angle;
        double[] start= new double[4];
        double[] end=new double[4];
        int center=(screen/2);
        degree=360.0/(num);
        for(int i=0; i<num; i++){
            //color[i]=new Color(127,127,127);
            poly[i]= new Path2D.Double();
            angle=degree*i;
            start[0]=radius*Math.cos(Math.toRadians(angle+(degree*0.5)));
            start[1]=radius*Math.sin(Math.toRadians(angle+(degree*0.5)));
            end[0]=radius*Math.cos(Math.toRadians(angle-(degree*0.5)));
            end[1]=radius*Math.sin(Math.toRadians(angle-(degree*0.5)));

            poly[i].moveTo(center,center);
            poly[i].lineTo(((int)start[0]+center),((int)start[1]+center));
            poly[i].lineTo(((int)end[0]+center),((int)end[1]+center));
            poly[i].closePath();
            shapes.add(poly[i]);
        }
        for (int j = 1; j < led; j++) {
            for(int i = 0; i < num; i++) {
                //color[num*j+i]=new Color(127,127,127);
                poly[num*j+i]= new Path2D.Double();

                radius=r+r*(j);
                angle=degree*i;
                side = 2.0*radius*Math.sin(Math.toRadians(Math.PI/(num)));
                start[0]=radius*Math.cos(Math.toRadians(angle+(degree*0.5)));
                start[1]=radius*Math.sin(Math.toRadians(angle+(degree*0.5)));

                start[2]=(radius-(r))*Math.cos(Math.toRadians(angle+(degree*0.5)));
                start[3]=(radius-(r))*Math.sin(Math.toRadians(angle+(degree*0.5)));

                end[0]=radius*Math.cos(Math.toRadians(angle-(degree*0.5)));
                end[1]=radius*Math.sin(Math.toRadians(angle-(degree*0.5)));

                end[2]=(radius-(r))*Math.cos(Math.toRadians(angle-(degree*0.5)));
                end[3]=(radius-(r))*Math.sin(Math.toRadians(angle-(degree*0.5)));

                poly[num*j+i].moveTo((int)start[0]+center,(int)start[1]+center);
                poly[num*j+i].lineTo((int)end[0]+center,(int)end[1]+center);
                poly[num*j+i].lineTo((int)end[2]+center ,(int)end[3]+center);
                poly[num*j+i].lineTo((int)start[2]+center ,(int)start[3]+center);
                poly[num*j+i].closePath();
                shapes.add(poly[num*j+i]);
            }
        }
    }*/

    private void ToolBar(){
        toolBarFrame = new JFrame("3D POV ToolBar");
        toolBarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        toolBarFrame.setResizable(false);
        toolBarFrame.setSize(screenShift-100,100);
        toolBarFrame.setLocation(0,0);
        toolBarPanel = new JPanel();
        GridLayout toolBarLayout = new GridLayout(0,3);
        toolBarPanel.setLayout(toolBarLayout);
        ListenerSetup();
        toolBarPanel.add(sliderR);
        toolBarPanel.add(sliderG);
        toolBarPanel.add(sliderB);
        toolBarPanel.add(save);
        toolBarPanel.add(open);
        toolBarPanel.add(colorselect);
        //panel.add(new JLabel(""));
        toolBarPanel.add(new JLabel("    File name: "));
        toolBarPanel.add(fileNameTextField);
        //name = text.getText();
        toolBarPanel.add(new JLabel(""));
        toolBarPanel.add(new JLabel("    Bit Depth: "));
        toolBarPanel.add(resolutionTextField);
        //resolution = Integer.parseInt(res.getText());
        toolBarPanel.add(upd);
        toolBarFrame.add(toolBarPanel);
        toolBarFrame.setVisible(true);
    }




    private void ListenerSetup() {
        sliderView.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                Change(source);
            }
        });
        sliderB.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                Change(source);
            }
        });
        sliderR.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                Change(source);
            }
        });
        sliderG.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                Change(source);
            }
        });
        save.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Save();
            }
        });
        open.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Open();
            }
        });
        colorselect.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                neutral();
            }
        });
        upd.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resUpd();
            }
        });

    }

    private void neutral() {
    }

    private void resUpd() {
    }

    private void Open() {
    }

    private void Save() {
    }

    private void Change(JSlider source) {
        if (!source.getValueIsAdjusting()) {
            currentColor = new Color((int) Math.round(sliderR.getValue() / resolution *255.0),(int) Math.round(sliderG.getValue() / resolution *255.0),(int) Math.round(sliderB.getValue() / resolution *255.0));
            //repaint();
        }
    }
}

