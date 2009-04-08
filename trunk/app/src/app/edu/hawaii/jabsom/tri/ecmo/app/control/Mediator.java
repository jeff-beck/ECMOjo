package edu.hawaii.jabsom.tri.ecmo.app.control;

import king.lib.out.Error;
import edu.hawaii.jabsom.tri.ecmo.app.model.comp.*;
import edu.hawaii.jabsom.tri.ecmo.app.model.comp.TubeComponent.Mode;

/**
 * Mediator class to separate links between models from Apple Developer website.
 * http://developer.apple.com/documentation/Cocoa/Conceptual/CocoaFundamentals/
 * CocoaDesignPatterns/CocoaDesignPatterns.html
 * 
 * Collection of methods to simplify Updater.java
 * 
 * @author tanaka
 * @since April 1, 209
 * 
 */
public final class Mediator {
  
  /**
   * Unused constructor.
   */
  private Mediator() {
    // cannot be used
  }

  /**
   * flowToPH provides linkage information of Pump flow to patient pH. Dependent
   * on mode of pump, kind of lung, kind of heart, and flow.
   * 
   * @param ecmo
   *          Mode of ecmo {VA, VV}
   * @param flow
   *          Rate of blood flow in mL/kg/min
   * @param patient
   *          Current patient instantiation
   * @return pH by linear interpolation of Mark's chart
   */
  public static double flowToPH(Mode ecmo, double flow, Patient patient) {
    if (flow < 0) {
      Error.out("Flow cannot be less than 0");
      System.exit(0);
    }
    if (ecmo == Mode.VV) {
      if (patient.getHeartFunction() == Patient.HeartFunction.BAD) {
        return 7.0;
      }
      else { // Heart function is good
        if (patient.getLungFunction() == Patient.LungFunction.BAD) {
          if ((flow >= 0) && (flow < 50)) {
            return ((7.15 - 7.03) / 50) * flow + 7.03;
          }
          else if ((flow >= 50) && (flow < 75)) {
            return ((7.2 - 7.15) / 25) * flow + 7.05;
          }
          else if ((flow >= 75) && (flow < 100)) {
            return ((7.24 - 7.2) / 25) * flow + 7.08;
          }
          else if ((flow >= 100) && (flow < 120)) {
            return ((7.4 - 7.24) / 20) * flow + 6.44;
          }
          else if ((flow >= 120) && (flow < 150)) {
            return ((7.32 - 7.4) / 30) * flow + 7.72; // estimate and rounded
          }
          else if ((flow >= 150) && (flow <= 175)) {
            return ((7.24 - 7.32) / 25) * flow + 7.8;
          }
          else {
            return 7; // no data beyond 175cc/kg/min flow
          }
        }
        else { // Lungs are working why are we on ECMO?
          if ((flow >= 0) && (flow < 50)) {
            return ((7.25 - 7.15) / 50) * flow + 7.15;
          }
          else if ((flow >= 50) && (flow < 75)) {
            return ((7.3 - 7.25) / 25) * flow + 7.15;
          }
          else if ((flow >= 75) && (flow < 100)) {
            return ((7.35 - 7.3) / 25) * flow + 7.15;
          }
          else if ((flow >= 100) && (flow < 120)) {
            return ((7.4 - 7.35) / 20) * flow + 7.1;
          }
          else if ((flow >= 120) && (flow < 150)) {
            return ((7.32 - 7.4) / 30) * flow + 7.72; // estimate and rounded
          }
          else if ((flow >= 150) && (flow <= 175)) {
            return ((7.24 - 7.32) / 25) * flow + 7.8;
          }
          else {
            return 7; // no data beyond 175cc/kg/min flow
          }
        }
      }
    }
    else { // must be VA
      if (patient.getHeartFunction() == Patient.HeartFunction.BAD) {
        if (patient.getLungFunction() == Patient.LungFunction.BAD) {
          if ((flow >= 0) && (flow < 25)) {
            return ((7.1 - 7.05) / 25) * flow + 7.05;
          }
          else if ((flow >= 25) && (flow < 50)) {
            return ((7.17 - 7.1) / 25) * flow + 7.03;
          }
          else if ((flow >= 50) && (flow < 75)) {
            return ((7.2 - 7.17) / 25) * flow + 7.11;
          }
          else if ((flow >= 75) && (flow < 100)) {
            return ((7.4 - 7.2) / 25) * flow + 6.6;
          }
          else if ((flow >= 100) && (flow < 125)) {
            return ((7.37 - 7.4) / 25) * flow + 7.52;
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return ((7.35 - 7.37) / 25) * flow + 7.47;
          }
          else {
            return 7; // no data beyond 150cc/kg/min flow
          }
        }
        else { // Lungs are working but heart not so good
          if ((flow >= 0) && (flow < 25)) {
            return ((7.15 - 7.12) / 25) * flow + 7.12;
          }
          else if ((flow >= 25) && (flow < 50)) {
            return ((7.23 - 7.15) / 25) * flow + 7.07;
          }
          else if ((flow >= 50) && (flow < 75)) {
            return ((7.26 - 7.23) / 25) * flow + 7.17;
          }
          else if ((flow >= 75) && (flow < 100)) {
            return ((7.4 - 7.26) / 25) * flow + 6.84;
          }
          else if ((flow >= 100) && (flow < 125)) {
            return ((7.33 - 7.4) / 25) * flow + 7.68;
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return ((7.29 - 7.33) / 25) * flow + 7.53;
          }
          else {
            return 7; // no data beyond 150cc/kg/min flow
          }
        }

      }
      else { // Heart function is good
        if (patient.getLungFunction() == Patient.LungFunction.BAD) {
          if ((flow >= 0) && (flow < 25)) {
            return ((7.12 - 7.07) / 25) * flow + 7.07;
          }
          else if ((flow >= 25) && (flow < 50)) {
            return ((7.18 - 7.12) / 25) * flow + 7.0575;
          }
          else if ((flow >= 50) && (flow < 75)) {
            return ((7.25 - 7.18) / 25) * flow + 7.04;
          }
          else if ((flow >= 75) && (flow < 100)) {
            return ((7.4 - 7.25) / 25) * flow + 6.8;
          }
          else if ((flow >= 100) && (flow < 125)) {
            return ((7.29 - 7.4) / 25) * flow + 7.84;
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return ((7.25 - 7.29) / 25) * flow + 7.49;
          }
          else {
            return 7; // no data beyond 150cc/kg/min flow
          }
        }
        else { // Lungs & Heart are working why are we on ECMO?
          if ((flow >= 0) && (flow < 25)) {
            return ((7.18 - 7.12) / 25) * flow + 7.12;
          }
          else if ((flow >= 25) && (flow < 50)) {
            return ((7.23 - 7.18) / 25) * flow + 7.13;
          }
          else if ((flow >= 50) && (flow < 75)) {
            return ((7.29 - 7.23) / 25) * flow + 7.11;
          }
          else if ((flow >= 75) && (flow < 100)) {
            return ((7.4 - 7.29) / 25) * flow + 6.96;
          }
          else if ((flow >= 100) && (flow < 125)) {
            return ((7.35 - 7.4) / 25) * flow + 7.6;
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return ((7.31 - 7.35) / 25) * flow + 7.55;
          }
          else {
            return 7; // no data beyond 175cc/kg/min flow
          }
        }
      }
    }
  }

  /**
   * flowToPCO2 provides linkage information of Pump flow to patient pCO2.
   * Dependent on mode of pump, kind of lung, kind of heart, and flow.
   * 
   * @param ecmo
   *          Mode of ecmo {VA, VV}
   * @param flow
   *          Rate of blood flow in mL/kg/min
   * @param patient
   *          Current patient instantiation
   * @return pCO2 by linear interpolation of Mark's chart
   */
  public static double flowToPCO2(Mode ecmo, double flow, Patient patient) {
    if (flow < 0) {
      Error.out("Flow cannot be less than 0");
      System.exit(0);
    }
    if (ecmo == Mode.VV) {
      if (patient.getHeartFunction() == Patient.HeartFunction.BAD) {
        return 120;
      }
      else { // Heart function is good
        if (patient.getLungFunction() == Patient.LungFunction.BAD) {
          if ((flow >= 0) && (flow < 50)) {
            return Math.rint(((66 - 83) / 50) * flow + 83);
          }
          else if ((flow >= 50) && (flow < 75)) {
            return Math.rint(((60 - 66) / 25) * flow + 78);
          }
          else if ((flow >= 75) && (flow < 100)) {
            return Math.rint(((55 - 60) / 25) * flow + 75);
          }
          else if ((flow >= 100) && (flow < 120)) {
            return Math.rint(((40 - 55) / 20) * flow + 130);
          }
          else if ((flow >= 120) && (flow < 150)) {
            return Math.rint(((46 - 40) / 30) * flow + 16);
          }
          else if ((flow >= 150) && (flow <= 175)) {
            return Math.rint(((53 - 46) / 25) * flow + 4);
          }
          else {
            return 120; // no data beyond 175cc/kg/min flow
          }
        }
        else { // Lungs are working why are we on ECMO?
          if ((flow >= 0) && (flow < 50)) {
            return Math.rint(((53 - 66) / 50) * flow + 66);
          }
          else if ((flow >= 50) && (flow < 75)) {
            return Math.rint(((48 - 53) / 25) * flow + 63);
          }
          else if ((flow >= 75) && (flow < 100)) {
            return Math.rint(((44 - 48) / 25) * flow + 60);
          }
          else if ((flow >= 100) && (flow < 120)) {
            return Math.rint(((40 - 44) / 20) * flow + 64);
          }
          else if ((flow >= 120) && (flow < 150)) {
            return Math.rint(((46 - 40) / 30) * flow + 16);
          }
          else if ((flow >= 150) && (flow <= 175)) {
            return Math.rint(((53 - 46) / 25) * flow + 4);
          }
          else {
            return 120; // no data beyond 175cc/kg/min flow
          }
        }
      }
    }
    else { // must be VA
      if (patient.getHeartFunction() == Patient.HeartFunction.BAD) {
        if (patient.getLungFunction() == Patient.LungFunction.BAD) {
          if ((flow >= 0) && (flow < 25)) {
            return Math.rint(((74 - 81) / 25) * flow + 81);
          }
          else if ((flow >= 25) && (flow < 50)) {
            return Math.rint(((64 - 74) / 25) * flow + 84);
          }
          else if ((flow >= 50) && (flow < 75)) {
            return Math.rint(((60 - 64) / 25) * flow + 72);
          }
          else if ((flow >= 75) && (flow < 100)) {
            return Math.rint(((41 - 60) / 25) * flow + 117);
          }
          else if ((flow >= 100) && (flow < 125)) {
            return Math.rint(((42 - 41) / 25) * flow + 37);
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return Math.rint(((44 - 42) / 25) * flow + 32);
          }
          else {
            return 120; // no data beyond 150cc/kg/min flow
          }
        }
        else { // Lungs are working but heart not so good
          if ((flow >= 0) && (flow < 25)) {
            return Math.rint(((68 - 72) / 25) * flow + 72);
          }
          else if ((flow >= 25) && (flow < 50)) {
            return Math.rint(((55 - 68) / 25) * flow + 81);
          }
          else if ((flow >= 50) && (flow < 75)) {
            return Math.rint(((51 - 55) / 25) * flow + 63);
          }
          else if ((flow >= 75) && (flow < 100)) {
            return Math.rint(((41 - 51) / 25) * flow + 81);
          }
          else if ((flow >= 100) && (flow < 125)) {
            return Math.rint(((46 - 41) / 25) * flow + 21);
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return Math.rint(((50 - 46) / 25) * flow + 26);
          }
          else {
            return 120; // no data beyond 150cc/kg/min flow
          }
        }

      }
      else { // Heart function is good
        if (patient.getLungFunction() == Patient.LungFunction.BAD) {
          if ((flow >= 0) && (flow < 25)) {
            return Math.rint(((71 - 80) / 25) * flow + 80);
          }
          else if ((flow >= 25) && (flow < 50)) {
            return Math.rint(((63 - 71) / 25) * flow + 79);
          }
          else if ((flow >= 50) && (flow < 75)) {
            return Math.rint(((47 - 63) / 25) * flow + 95);
          }
          else if ((flow >= 75) && (flow < 100)) {
            return Math.rint(((41 - 47) / 25) * flow + 65);
          }
          else if ((flow >= 100) && (flow < 125)) {
            return Math.rint(((50 - 41) / 25) * flow + 5);
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return Math.rint(((54 - 50) / 25) * flow + 30);
          }
          else {
            return 120; // no data beyond 150cc/kg/min flow
          }
        }
        else { // Lungs & Heart are working why are we on ECMO?
          if ((flow >= 0) && (flow < 25)) {
            return Math.rint(((63 - 71) / 25) * flow + 71);
          }
          else if ((flow >= 25) && (flow < 50)) {
            return Math.rint(((56 - 63) / 25) * flow + 70);
          }
          else if ((flow >= 50) && (flow < 75)) {
            return Math.rint(((50 - 56) / 25) * flow + 68);
          }
          else if ((flow >= 75) && (flow < 100)) {
            return Math.rint(((41 - 50) / 25) * flow + 77);
          }
          else if ((flow >= 100) && (flow < 125)) {
            return Math.rint(((44 - 41) / 25) * flow + 29);
          }
          else if ((flow >= 125) && (flow <= 150)) {
            return Math.rint(((48 - 44) / 25) * flow + 24);
          }
          else {
            return 120; // no data beyond 175cc/kg/min flow
          }
        }
      }
    }
  }
}