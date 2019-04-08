/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package bsu.fpmi.educational_practice;

import java.applet.Applet;
import java.awt.*;

public class Test extends Applet {

    private static final long serialVersionUID = 1L;
    private Ellipse ellipse;

    @Override
    public void init() {
        int x = 580;
        int y = 580;
        setSize(x, y);
        ellipse = new Ellipse(130, 150, new Color(0x6C689F));
    }

    @Override
    public void paint(Graphics g) {
        ellipse.paint(g);
        ellipse.setX_START_COORDINATE(250);
        ellipse.setY_COORDINATE(250);
    }
}