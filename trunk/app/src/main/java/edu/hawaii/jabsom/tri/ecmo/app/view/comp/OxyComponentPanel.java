package edu.hawaii.jabsom.tri.ecmo.app.view.comp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import king.lib.access.ImageLoader;
import king.lib.out.Error;

import edu.hawaii.jabsom.tri.ecmo.app.gui.ImageToggleButton;
import edu.hawaii.jabsom.tri.ecmo.app.model.comp.OxygenatorComponent;
import edu.hawaii.jabsom.tri.ecmo.app.model.comp.OxygenatorComponent.OxyType;

/**
 * The component panel. 
 *
 * @author   Christoph Aschwanden
 * @since    September 4, 2008
 */
public class OxyComponentPanel extends ComponentPanel implements Runnable {

  /** The oxi image. */
  private Image oxyImage;
  /** The oxigenator broken image. */
  private Image brokenImage;
  
  /** The rollover image. */
  private Image rolloverImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Btn-OxiRol.png");
  /** The selected image. */
  private Image selectedImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Btn-OxiSel.png");

  /** The red alert image. */
  private Image redAlertImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Alrt-RedSmall.png");
  /** The black alert image. */
  private Image blackAlertImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Alrt-BlackSmall.png");

  /** The top bar image. */
  private Image topBarImage = ImageLoader.getInstance().getImage("conf/image/interface/game/BarMini-Top.png");
  /** The middle bar image. */
  private Image middleBarImage = ImageLoader.getInstance().getImage("conf/image/interface/game/BarMini-Middle.png");
  /** The bottom bar image. */
  private Image bottomBarImage = ImageLoader.getInstance().getImage("conf/image/interface/game/BarMini-Bottom.png");
  
  /** The font color. */
  private final Color textColor = new Color(0.2f, 0.2f, 0.2f);
  
  /** The dripping blood image. */
  private Image drippingBloodImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Comp-Blood.png");
  /** The dripping blood time. */
  private long[] elapsedTime = new long[3];
  /** The dripping blood start flag. */
  private boolean[] startFlag = new boolean[3];
  /** The dripping blood random flag. */
  private boolean[] randomFlag = new boolean[3]; 
  /** The last update in nano second. */
  private long lastUpdate;
  /** The updater thread. */
  private Thread thread;

  /** The component. */
  protected OxygenatorComponent component;
  
  /** The selection button. */
  private AbstractButton selectionButton;
  
  
  /**
   * Constructor for panel.
   * 
   * @param component  The associated component.
   */
  protected OxyComponentPanel(OxygenatorComponent component) {
    super(component);
    this.component = component;
    
    // set size and location
    setLocation(530, 303);
    setSize(270, 297);
    
    // set layout
    setLayout(null);
    
    // load images
    if (component.getOxyType() == OxyType.QUADROX_D) {
      oxyImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Comp-QuadroxDOxygenator.png");
      brokenImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Comp-QuadroxDOxygenatorBroken.png");
    }
    else {
      oxyImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Comp-SciMedOxygenator.png");
      brokenImage = ImageLoader.getInstance().getImage("conf/image/interface/game/Comp-SciMedOxygenatorBroken.png");
    }
    
    // setup blood dripping
    lastUpdate = 0;
    for (int i = 0; i < 3; i++) {
      elapsedTime[i] = 0;
      startFlag[i] = false;
      randomFlag[i] = false;
    }
    
    // add toggle button
    selectionButton = new ImageToggleButton(null, rolloverImage, selectedImage, selectedImage);
    selectionButton.setToolTipText(component.getName());
    selectionButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        notifyActivationListeners();
      }
    });
    selectionButton.setLocation(125, 28);
    selectionButton.setSize(121, 163);
    add(selectionButton);
  }

  /**
   * Called when the component got updated.
   */
  public void handleUpdate() {
    repaint();
  }

  /**
   * Called when panel is added.
   */
  @Override
  public void addNotify() {
    super.addNotify();
    
    // start thread
    this.thread = new Thread(this);
    this.thread.setPriority(Thread.MIN_PRIORITY);
    this.thread.start();
  }
  
  /**
   * Called when panel is removed.
   */
  @Override
  public void removeNotify() {
    // stop thread
    this.thread = null;
    
    super.removeNotify();
  }
  
  /**
   * The time updater thread.
   */
  public void run() {
    Thread currentThread = Thread.currentThread();
    while (this.thread == currentThread) {
      // update
      repaint();
      
      // wait for next update
      try {
        Thread.sleep(50);
      }
      catch (InterruptedException e) {
        Error.out(e);
      }
    }
  }
  
  /**
   * Paints this component.
   * 
   * @param g  Where to draw to.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // set antialised text
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    // draws the image
    if (component.getOxyType() == OxyType.QUADROX_D) {
      g.drawImage(oxyImage, 13, 0, this);
    }
    else {
      g.drawImage(oxyImage, 5, 33, this);
    }
        
    // total sweep integer bar
    g.drawImage(bottomBarImage, 199, 138, this);
    int totalSweepIntegerBarTopHeight = (int)component.getTotalSweep() * 6;
    for (int i = 0; i < (totalSweepIntegerBarTopHeight - 2); i++) {
      g.drawImage(middleBarImage, 199, 137 - i, this); 
    }  
    g.drawImage(topBarImage, 199, 138 - totalSweepIntegerBarTopHeight, this);
    
    // total sweep decimal bar
    g.drawImage(bottomBarImage, 216, 138, this);
    int totalSweepDecimalBarTopHeight = ((int)(component.getTotalSweep() * 10.0)
                                      - (int)component.getTotalSweep() * 10) * 6;
    for (int i = 0; i < (totalSweepDecimalBarTopHeight - 2); i++) {
      g.drawImage(middleBarImage, 216, 137 - i, this);    
    }
    g.drawImage(topBarImage, 216, 138 - totalSweepDecimalBarTopHeight, this);
  
    // draw blinking red light if there is an alarm
    if (component.isAlarm()){
      if ((((System.nanoTime()) / 500000000) % 2) == 0) {
        g.drawImage(redAlertImage, 178, 77, this);
      }
      else {
        g.drawImage(blackAlertImage, 178, 77, this);        
      }      
    } 
    
    // set text properties
    g.setColor(textColor);
    g.setFont(g.getFont().deriveFont(Font.BOLD, 15f));
    
    // draw text
    String text = Math.round(component.getFiO2() * 100) + "%";
    g.drawString(text, 148, 120);
    
    // draw blood dripping as needed
    if (component.isBroken()) {
      int[] positions = new int[3];
      
      // draws the broken image
      if (component.getOxyType() == OxyType.QUADROX_D) {
        g.drawImage(brokenImage, 13, 0, this);
      }
      else {
        g.drawImage(brokenImage, 5, 33, this);
        // draws the dripping blood
        for (int i = 0; i < 3; i++) {
          if (elapsedTime[i] > 0) {
            positions[i] = (int) (0.00000001 * elapsedTime[i] * 0.00000001 * elapsedTime[i]);
            g.drawImage(drippingBloodImage, 45, 154 + positions[i], this);
            if (positions[i] > 145) {
              elapsedTime[i] = 0;
              startFlag[i] = false;
            }
          }
        }
      }

      long currentUpdate = System.nanoTime();
      long delta = currentUpdate - lastUpdate;
      if (lastUpdate > 0) {
        for (int i = 0; i < 3; i++) {
          if (startFlag[i]) {
            elapsedTime[i] += delta;
          }
          else {
            // random start the flag
            if ((((System.nanoTime()) / 200000000) % 2) == 0) {
              // run one time
              if (randomFlag[i]) {
                if (Math.random() > 0.5) {
                  startFlag[i] = true;
                }
              }
              randomFlag[i] = false;
            } 
            else {
              randomFlag[i] = true;  
            }
          }
        }
      }
      lastUpdate = currentUpdate;
    }
    else {
      lastUpdate = 0;
    }
  }
  
  /**
   * Assigns the component to a group.
   * 
   * @param group  The group.
   */
  public void assign(ButtonGroup group) {
    group.add(selectionButton);
  }
}
