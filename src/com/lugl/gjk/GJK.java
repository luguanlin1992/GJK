package com.lugl.gjk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luguanlin
 * 2021/6/12 11:19
 */
public class GJK {

    private static Point subtract(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    private static Point negate(Point v) {
        return new Point(-v.x, -v.y);
    }

    private static Point perpendicular(Point v) {
        return new Point(v.y, -v.x);
    }

    private static float dotProduct(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    private static float lengthSquared(Point v) {
        return v.x * v.x + v.y * v.y;
    }

    private static Point tripleProduct(Point a, Point b, Point c) {
        float ac = dotProduct(a, c);
        float bc = dotProduct(b, c);
        return new Point(b.x * ac - a.x * bc, b.y * ac - a.y * bc);
    }

    private static Point averagePoint(List<Point> vecList) {
        float x = (float) vecList.stream().mapToDouble(value -> value.x).average().getAsDouble();
        float y = (float) vecList.stream().mapToDouble(value -> value.y).average().getAsDouble();
        return new Point(x, y);
    }

    private static int indexOfFurthestPoint(List<Point> vecList, Point d) {
        float maxProduct = dotProduct(d, vecList.get(0));
        int index = 0;
        for (int i = 1; i < vecList.size(); i++) {
            float product = dotProduct(d, vecList.get(i));
            if (product > maxProduct) {
                maxProduct = product;
                index = i;
            }
        }
        return index;
    }

    private static Point support(List<Point> vecList1, List<Point> vecList2, Point d) {
        int index1 = indexOfFurthestPoint(vecList1, d);
        int index2 = indexOfFurthestPoint(vecList2, negate(d));
        return subtract(vecList1.get(index1), vecList2.get(index2));
    }

    /**
     * @param vecList1 point list of shape 1
     * @param vecList2 point list of shape 2
     * @return true is collision，false is no collision
     */
    public static boolean collision(List<Point> vecList1, List<Point> vecList2) {
        int index = 0;
        Point a, b, c, d, ao, ab, ac, abperp, acperp;
        Point[] simplex = new Point[3];
        Point position1 = averagePoint(vecList1);
        Point position2 = averagePoint(vecList2);
        d = subtract(position1, position2);
        if (d.x == 0f && d.y == 0f) {
            d.x = 1f;
        }
        a = simplex[0] = support(vecList1, vecList2, d);
        if (dotProduct(a, d) <= 0) {
            return false;
        }
        d = negate(a);
        while (true) {
            a = simplex[++index] = support(vecList1, vecList2, d);
            if (dotProduct(a, d) <= 0) {
                return false;
            }
            ao = negate(a);
            if (index < 2) {
                b = simplex[0];
                ab = subtract(b, a);
                d = tripleProduct(ab, ao, ab);
                if (lengthSquared(d) == 0) {
                    d = perpendicular(ab);
                }
                continue;
            }
            b = simplex[1];
            c = simplex[0];
            ab = subtract(b, a);
            ac = subtract(c, a);
            acperp = tripleProduct(ab, ac, ac);
            if (dotProduct(acperp, ao) > 0) {
                d = acperp;
            } else {
                abperp = tripleProduct(ac, ab, ab);
                if (dotProduct(abperp, ao) < 0) {
                    return true;
                }
                simplex[0] = simplex[1];
                d = abperp;
            }
            simplex[1] = simplex[2];
            --index;
        }
    }

    public static void main(String[] args) {
        List<Point> vecList1 = new ArrayList<>();
        List<Point> vecList2 = new ArrayList<>();
        vecList1.add(new Point(1f, 1f));
        vecList1.add(new Point(1f, 2f));
        vecList1.add(new Point(2f, 1f));
        vecList1.add(new Point(2f, 1f));
        vecList2.add(new Point(5f, 5f));
        vecList2.add(new Point(6f, 5f));
        vecList2.add(new Point(5f, 6f));
        boolean collisionDetected = collision(vecList1, vecList2);
        if (collisionDetected) {
            System.out.println("collision");
        } else {
            System.out.println("no collision");
        }
    }
}

class Point {
    public float x;
    public float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
