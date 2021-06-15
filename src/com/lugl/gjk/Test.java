package com.lugl.gjk;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author luguanlin
 * 2021/6/15 14:45
 */
public class Test {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("GJK collision!");
        jFrame.setSize(800, 600);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TestPanel jPanel = new TestPanel();
        jFrame.setContentPane(jPanel);
        JLabel jLabel = new JLabel(jPanel.collisionDetected ? "collision" : "no collision");
        jFrame.add(jLabel);
        jFrame.setVisible(true);
    }
}

class TestPanel extends JPanel {
    public TestPanel() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            list1.add(new Point(random.nextInt(800), random.nextInt(600)));
            list2.add(new Point(random.nextInt(800), random.nextInt(600)));
        }
        collisionDetected = GJK.collision(list1, list2);
    }

    public List<Point> list1 = new ArrayList<>();
    public List<Point> list2 = new ArrayList<>();
    public boolean collisionDetected;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (list1.size() > 1 && list2.size() > 1) {
            Graphics2D graphics2D = (Graphics2D) g;
            drawShape(list1, graphics2D);
            drawShape(list2, graphics2D);
        }
    }

    private void drawShape(List<Point> list, Graphics2D graphics2D) {
        Point start = list.get(0);
        Point end;
        for (int i = 1; i < list.size(); i++) {
            end = list.get(i);
            graphics2D.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            start = end;
        }
        end = list.get(0);
        graphics2D.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
    }
}
