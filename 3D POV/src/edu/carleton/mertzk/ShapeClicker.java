package edu.carleton.mertzk;

/**
 * Created by keaton on 7/3/17.
 */

/*
import java.io.PrintWriter;
import java.lang.Math;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.util.Scanner;
import javax.swing.JSlider;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JTextField;
*/

import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.lang.Math;
import java.io.*;

public class ShapeClicker {
    private int screen=690;
    public ShapeClicker() {
        JFrame frame = new JFrame();
        frame.setTitle("Shape Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        initComponents(frame);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(1360-screen,0);

    }

    public static void main(String[] args) {

        //Finds how many sections are wanted

        //create frame and components on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShapeClicker();
            }
        });
    }


      /*JTextField fileName = new JTextField();
      Object[] message = {"Number of sides(int): ", fileName.getText()};//send text of filename
      String option = JOptionPane.showInputDialog(null, message, "Shape Clicker", JOptionPane.OK_CANCEL_OPTION);
      String s = fileName.getText();
      if ((s != null) && (s.length() > 0)) {
        return Integer.parseInt(s);
      }
      return 33;
    }*/

    private void initComponents(JFrame frame) {

        JFrame f = new JFrame();
        f.setTitle("Slider");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        GridLayout layout = new GridLayout(0,3);
        JPanel panel = new JPanel();

        panel.setLayout(layout);
        ShapePanel shapepanel = new ShapePanel(20, panel, screen, f);
        shapepanel.setBackground(Color.DARK_GRAY);
        frame.add(shapepanel);
        f.add(panel);
        f.pack();
        f.setVisible(true);
    }



}

//custom panel
class ShapePanel extends JPanel {

    //private Shape rect = new Rectangle2D.Double(50, 100, 200, 100);
    //private Ellipse2D cirlce = new Ellipse2D.Double(260, 100, 100, 100);
    private int screen;
    private boolean bool;
    private Dimension dim;
    public final ArrayList<Shape> shapes;
    private Path2D[] poly;
    public Color[] color;
    private int pixles;
    private int led;
    public int r;
    private int num;
    public float resolution=2;
    public Color cbut;
    //public Rectangle2D rect = new Rectangle2D.Double(20,20,20,20);
    public JSlider sliderR = new JSlider(0,(int) resolution);
    public JSlider sliderG = new JSlider(0,(int) resolution);
    public JSlider sliderB = new JSlider(0,(int) resolution);
    public JButton submit= new JButton("Submit");
    public JButton download= new JButton("download");
    public JTextField text= new JTextField("picture");
    public JButton colorselect= new JButton("color");
    public JTextField res= new JTextField(String.valueOf((int) resolution));
    public JButton upd= new JButton("Update");
    private String name;

    public ShapePanel(int num, JPanel panel, int screen, JFrame f) {
        this.screen=screen;
        //System.out.print((int)(int) Math.round(0.9));
        this.num=num;
        cbut = new Color((int) Math.round(sliderR.getValue() / resolution *255.0),(int) Math.round(sliderG.getValue() / resolution *255.0),(int) Math.round(sliderB.getValue() / resolution *255.0));
        r=15;
        led = 21;
        dim = new Dimension(this.screen, this.screen);
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
        //sliderG.addChangeListener(new SliderListener());
        //sliderR.addChangeListener(new SliderListener());
        submit.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Save();
            }
        });
        download.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Download();
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

        panel.add(sliderR);
        panel.add(sliderG);
        panel.add(sliderB);
        panel.add(submit);
        panel.add(download);
        panel.add(colorselect);
        //panel.add(new JLabel(""));
        panel.add(new JLabel("    File name: "));
        panel.add(text);
        name = text.getText();
        panel.add(new JLabel(""));
        panel.add(new JLabel("    Number of colors: "));
        panel.add(res);
        //resolution = Integer.parseInt(res.getText());
        panel.add(upd);
        //panel.setVisible(true);
        pixles = num*led;
        this.color = new Color[pixles];
        this.poly= new Path2D[pixles];
        shapes = new ArrayList<>();
        //for(int i=0; i<num; i++)
        //  color[i]=new Color(resolution,resolution,resolution);
        for (int j = 0; j < led; j++) {//i changed j from 1 to 0
            for(int i = 0; i < num; i++)
                color[num*j+i]=new Color(0,0,0);}
        drawer();
        mouseEventHandler handler = new mouseEventHandler(this);
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
        Clicker click = new Clicker(this);
        this.addMouseListener(click);
        this.addMouseMotionListener(click);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;
        grphcs.setColor(cbut);
        colorselect.setBackground(cbut);
        for (Shape s : shapes) {
            grphcs.setColor(this.color[shapes.indexOf(s)]);
            g2d.fill(s);
        }
    }

    private void Change(JSlider source) {

        if (!source.getValueIsAdjusting()) {
            cbut = new Color((int) Math.round(sliderR.getValue() / resolution *255.0),(int) Math.round(sliderG.getValue() / resolution *255.0),(int) Math.round(sliderB.getValue() / resolution *255.0));
            repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }
    public void Download(){
        try{

            name = text.getText();
            BufferedReader br = new BufferedReader(new FileReader(name+"123.txt"));
            String line = null;
            int counter=0;
            int index;
            int index1;
            int val;
            while ((line = br.readLine()) != null) {
                if (counter==0){
                    num= Integer.parseInt(line);
                }
                else if (counter==1){
                    led= Integer.parseInt(line);
                }
                else if (counter==2){
                    resolution = Integer.parseInt(line);
                    res.setText(String.valueOf((int) resolution));
                    sliderB.setMaximum((int)resolution);
                    sliderG.setMaximum((int)resolution);
                    sliderR.setMaximum((int)resolution);
                    cbut = new Color((int) Math.round(sliderR.getValue() / resolution *255.0),(int) Math.round(sliderG.getValue() / resolution *255.0),(int) Math.round(sliderB.getValue() / resolution *255.0));

                }else{
                    pixles = num*led;
                    this.color = new Color[pixles];
                    this.poly= new Path2D[pixles];
                    int count=0;
                    String[] numstr = line.split(" ");
                    int[] arrnum= new int [numstr.length];

                    for(int i=0;i<numstr.length;i++){
                        arrnum[i]=Integer.parseInt(numstr[i]);
                    }
                    //if(arrnum[count]!=0 && arrnum[count+1]!=0 && arrnum[count+2]!=0)
                    //(int) Math.round(sliderR.getValue() / resolution *255.0)
                    System.out.println("A " +arrnum.length);
                    System.out.println("A " +3*(num*(led)+(num-1)));
                    System.out.println("B " +pixles);
                    for (int j=0; j<led; j++){
                        for(int i=0; i<num ; i++){
                            //  Color temp = color[num*j+i];
                            //color[count/3]= new Color (
                            System.out.println("C " + ((num*j)+i) +", " + (3*((num*j)+i))+", " + j+", " + i);
                            //System.out.print();
                            color[((num*j)+i)]= new Color (
                                    (int) Math.round(arrnum[(3*((num*j)+i))] / resolution *255.0),
                                    (int) Math.round(arrnum[((3*((num*j)+i))+1)] / resolution *255.0),
                                    (int) Math.round(arrnum[((3*((num*j)+i))+2)] / resolution *255.0));
                        }
                    }
                }
                counter++;
            }
            br.close();
            shapes.clear();
            drawer();
            this.repaint();
        }catch(IOException ex) {
            System.out.println("error");
        }
    }
    public void resUpd(){
        System.out.print("here");
        sliderR.setMaximum(Integer.parseInt(res.getText()));
        sliderG.setMaximum(Integer.parseInt(res.getText()));
        sliderB.setMaximum(Integer.parseInt(res.getText()));
        resolution=Integer.parseInt(res.getText());
        cbut = new Color((int) Math.round(sliderR.getValue() / resolution *255.0),(int) Math.round(sliderG.getValue() / resolution *255.0),(int) Math.round(sliderB.getValue() / resolution *255.0));
        for (int j = 0; j < led; j++) {//i changed j from 1 to 0
            for(int i = 0; i < num; i++){
                //System.out.print(" "+Math.round(Math.round(color[num*j+i].getRed() / oldres *resolution)*oldres /resolution));
                if(Math.round( Math.round(color[num*j+i].getRed() /resolution) *resolution)<=255 &&
                        Math.round( Math.round(color[num*j+i].getGreen() /resolution) *resolution)<=255 &&
                        Math.round( Math.round(color[num*j+i].getBlue() /resolution) *resolution)<=255){
                    color[num*j+i]=new Color(
                            Math.round( Math.round(color[num*j+i].getRed() /resolution) *resolution),
                            Math.round( Math.round(color[num*j+i].getGreen() /resolution) *resolution),
                            Math.round( Math.round(color[num*j+i].getBlue() /resolution) *resolution));
                }else{
                    int xx = Math.round( Math.round(color[num*j+i].getRed() /resolution) *resolution);
                    int yy = Math.round( Math.round(color[num*j+i].getGreen() /resolution) *resolution);
                    int zz = Math.round( Math.round(color[num*j+i].getBlue() /resolution) *resolution);
                    if(Math.round( Math.round(color[num*j+i].getRed() /resolution) *resolution)>255){
                        xx = 255;}
                    if(Math.round( Math.round(color[num*j+i].getRed() /resolution) *resolution)<0){
                        xx = 0;}
                    if(Math.round( Math.round(color[num*j+i].getGreen() /resolution) *resolution)>255){
                        yy = 255;}
                    if(Math.round( Math.round(color[num*j+i].getGreen() /resolution) *resolution)<0){
                        yy = 0;}
                    if(Math.round( Math.round(color[num*j+i].getBlue() /resolution) *resolution)>255){
                        zz = 255;}
                    if(Math.round( Math.round(color[num*j+i].getBlue() /resolution) *resolution)<0){
                        zz = 0;}
                    color[num*j+i]=new Color(xx,yy,zz);
                }
            }
        }

        this.repaint();
    }

    public void neutral(){
        System.out.print("Im here");
        sliderR.setValue((int) Math.round(resolution / 2));
        sliderG.setValue((int) Math.round(resolution / 2));
        sliderB.setValue((int) Math.round(resolution / 2));
    }
    public void Save(){
        write();
    }
    public void write(){
        System.out.print("saved");
        try {
            name=text.getText();
            PrintWriter pr = new PrintWriter(name+".txt", "UTF-8");
            PrintWriter wr = new PrintWriter(name+"123.txt", "UTF-8");
            pr.println(num);
            pr.println(led);
            pr.println((int)Math.round(resolution));
            wr.println(num);
            wr.println(led);
            wr.println((int)Math.round(resolution));

            for (int i=0; i<num ; i++){
                pr.print("[");
                for(int j=0; j<led; j++){
                    Color temp = color[((num*j)+i)];
                    if (j!=0){
                        pr.print(", ");
                    }
                    if(temp.getRed() !=127 && temp.getGreen() !=127 && temp.getBlue() !=127 ){
                        pr.print((int)Math.round(temp.getRed() / 255.0 *resolution));
                        pr.print(", ");
                        pr.print((int)Math.round(temp.getGreen() / 255.0 * resolution));
                        pr.print(", ");
                        pr.print((int)Math.round(temp.getBlue() / 255.0 * resolution));
                    }else{ pr.print("0, 0, 0");}

                }
                pr.println("]");
            }
            pr.flush();
            pr.close();
            System.out.println(num*led+num);
            for (int j=0; j<led; j++){
                for(int i=0; i<num ; i++){
                    Color temp = color[((num*j)+i)];
                    if(temp.getRed()/2 !=63 && temp.getGreen()/2 !=63 && temp.getBlue()/2 !=63 ){
                        wr.print((int)Math.round(temp.getRed() / 255.0 * resolution));wr.print(" ");
                        wr.print((int)Math.round(temp.getGreen() / 255.0 * resolution));wr.print(" ");
                        wr.print((int)Math.round(temp.getBlue() / 255.0 * resolution));wr.print(" ");
                    }else{ wr.print("0 0 0 ");}
                }
            }
            wr.flush();
            wr.close();
        }catch(IOException ex) {
            System.out.println("error");
        }

    }
    public void drawer(){
        int radius=r;
        double degree;
        double side;
        double angle;
        double[] start= new double[4];
        double[] end=new double[4];
        int center=(screen/2);
        degree=360.0/(num);
        side = 2.0*radius*Math.sin(Math.toRadians(Math.PI/(num)));
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
    }
}



class mouseEventHandler extends MouseAdapter {
    ShapePanel p;
    public mouseEventHandler(ShapePanel p){
        this.p=p;
    }

    public void mouseDragged(MouseEvent me){
        for (Shape s : p.shapes) {

            if (s.contains(me.getPoint())) {

                //System.out.println("Clicked a "+p.shapes.indexOf(s));
                p.color[p.shapes.indexOf(s)]=new Color((int) Math.round(p.sliderR.getValue()/ p.resolution * 255.0),(int) Math.round(p.sliderG.getValue() / p.resolution * 255.0),(int) Math.round(p.sliderB.getValue() / p.resolution * 255.0));
                p.repaint();
            }
        }
    }
}
class Clicker extends MouseAdapter{
    ShapePanel p;
    public Clicker(ShapePanel p){
        this.p=p;
    }

    public void mousePressed(MouseEvent me){
        if (SwingUtilities.isLeftMouseButton(me)) {
            for (Shape s : p.shapes) {
                if (s.contains(me.getPoint())) {

                    System.out.println("A "+(int) Math.round(p.sliderR.getValue()/ p.resolution * 255.0));
                    System.out.println("B "+ Math.round(p.sliderR.getValue()/ p.resolution));
                    System.out.println("C "+p.sliderR.getValue()/ p.resolution );
                    System.out.println("D "+p.sliderR.getValue());
                    System.out.println("E "+p.resolution );
                    p.color[p.shapes.indexOf(s)]=new Color((int) Math.round(p.sliderR.getValue()/ p.resolution * 255.0),(int) Math.round(p.sliderG.getValue() / p.resolution * 255.0),(int) Math.round(p.sliderB.getValue() / p.resolution * 255.0));
                    p.repaint();
                }
            }
        }
        if (SwingUtilities.isRightMouseButton(me)) {
            for (Shape s : p.shapes) {
                if (s.contains(me.getPoint())) {
                    Color tempcolor = p.color[p.shapes.indexOf(s)];
                    System.out.println((int) Math.round(tempcolor.getRed() / 255.0 * p.resolution));
                    p.sliderR.setValue((int) Math.round(tempcolor.getRed() / 255.0 * p.resolution));
                    p.sliderG.setValue((int) Math.round(tempcolor.getGreen() / 255.0 * p.resolution));
                    p.sliderB.setValue((int) Math.round(tempcolor.getBlue() / 255.0 * p.resolution));
                }
            }
        }

    }
}
/*class SliderListener implements ChangeListener {
  ShapePanel p;
  public SliderListener(ShapePanel p){
      this.p=p;
  }
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
          p.crect = new Color((int)p.sliderR.getValue()*2,(int)p.sliderG.getValue()*2,(int)p.sliderB.getValue()*2);
          p.repaint();

        }
    }
  }*/
