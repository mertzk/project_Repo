import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.Robot; 
import java.awt.AWTException; 
import processing.serial.*; 
import java.awt.MouseInfo; 
import java.awt.Point; 
import java.awt.event.InputEvent; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class N64_Controller extends PApplet {

 







Serial myPort;
Robot robby;
Point globalMouse;

boolean updated = false;
boolean [] status = {true,false,false,false};

int data;
boolean A;
boolean B;
boolean START;
boolean Z;
boolean U;
boolean D;
boolean L;
boolean R;

int globalMouseX = 0;
int globalMouseY = 0;
int rangeX = 1366; // output range of X
int rangeY = 768; 
int positionX = rangeX/2; // output range of X
int positionY = rangeY/2;
int velocityX = 0;
int velocityY = 0;
int incrementX = 5;
int incrementY = 5;

public void setup() {
  try{
    robby = new Robot();
  }
  catch (AWTException e){
    println("Robot class not supported by your system!");
    exit();
  }
  myPort = new Serial(this, Serial.list()[0], 38400);
  myPort.clear();
}

public void draw() {
  updated = update();
  if(updated == true){
    updated=false;
    action();
    if(status[2]){exit();}
    moveUpdate();
  }
  if (status[0]){
    move();
  }
  if (status[1]){
    wheelMove();
  }  
}

public boolean update(){
  if (myPort.available() > 0) {
    String dataString = myPort.readStringUntil(10);
    if (dataString!=null){
      dataString = dataString.trim();
      try{data = Integer.parseInt(dataString);}
      catch(NumberFormatException e){println("not a number"); return false;}
      R = intToBool(9);
      L = intToBool(8);
      D = intToBool(7);
      U = intToBool(6);
      START = intToBool(5);
      Z = intToBool(4);
      B = intToBool(3);
      A = intToBool(2);
      //println(data);
      return true;
    }
  }
  return false;
}

public void move(){
    if(velocityX!=0||velocityY!=0){
    globalMouse = MouseInfo.getPointerInfo().getLocation();
    globalMouseX = globalMouse.x;
    globalMouseY = globalMouse.y;
    if((velocityX<0&&globalMouseX>velocityX)||(velocityX>0&&globalMouseX<rangeX-velocityX)){
      positionX=globalMouseX+velocityX;
    }else{positionX=globalMouseX;}
    if((velocityY<0&&globalMouseY>velocityY)||(velocityY>0&&globalMouseY<rangeY-velocityY)){
      positionY=globalMouseY+velocityY;
    }else{positionY=globalMouseY;}
    robby.mouseMove(positionX,positionY);
  }
}

public void moveUpdate(){
  if(U==true&&D==false){
    velocityY=-incrementY;
  }else if(U==false&&D==true){
    velocityY=incrementY;
  }else{velocityY=0;}
  if(L==true&&R==false){
    velocityX=-incrementX;
  }else if(L==false&&R==true){
    velocityX=incrementX;
  }else{velocityX=0;}
}

public void action(){
  if(!START||!Z||!B||!A){
    println(data);
    if(!A&&!B&&!Z&&!START&&!status[3]){status[1]=false; status[0]=true;}
    else if(START&&Z&&!A&&!B&&!(D&&U)){
      status[2]=true;
    }
    else if(START&&!A&&!B&&!Z&&!(D&&U)){
      status[1]=false;
      status[0]=!status[0];
      status[3]=!status[3];
      delay(500);
      myPort.clear();
      println(data);
    }
    else if(A&&B&&!START&&!Z){
      status[1]=true;
      status[0]=false;
    }
    else if(A&&!B&&!Z&&!START){
      status[1]=false;
      robby.mousePress(InputEvent.BUTTON1_MASK);
      robby.mouseRelease( InputEvent.BUTTON1_MASK );
      delay(100);
      myPort.clear();
    }
    else if(B&&!A&&!Z&&!START){
      status[1]=false;
      robby.mousePress(InputEvent.BUTTON3_MASK);
      robby.mouseRelease( InputEvent.BUTTON3_MASK );
      delay(200);
      myPort.clear();
    }
  }
}

public void wheelMove(){
  if(velocityY!=0){
    robby.mouseWheel(velocityY/abs(velocityY));
    delay(90);
  }
}

public boolean intToBool(int boolPosition) {
  return (data / PApplet.parseInt(pow(10,9-boolPosition))) % 10 == 1 ? true : false;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "N64_Controller" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
