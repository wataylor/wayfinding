package zetek.test.path;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class TestShape {

  private String name;
  private Shape shape;
  private Shape origShape;
  private double currentScale = 1.0;
  private double x = 0;
  private double y = 0;
  private AffineTransform currentTransform;

  public TestShape(Shape shape, String name, JPanel panel) {
    setShape(shape);
    setName(name);
    fitToPanel(panel);
    System.out.println("yo:"+name);
  }

  public void fitToPanel(JPanel panel) {
    double pw = panel.getWidth();
    double ph = panel.getHeight();
    double sw = origShape.getBounds().getWidth()+40;
    double sh = origShape.getBounds().getHeight()+100;
    double xScale = pw/sw;
    double yScale = ph/sh;
    double scaleFactor = (xScale < yScale) ? xScale : yScale;
    double centerX = origShape.getBounds().getCenterX();
    double centerY = origShape.getBounds().getCenterY();
    x = (pw / 2 - (centerX * scaleFactor));
    y = (ph / 2 - (centerY * scaleFactor));
    currentScale = scaleFactor;
    System.out.println("currentScale="+currentScale);
    transform();
  }

  public void updatePan(double x, double y) {
    this.x += x;
    this.y += y;
    transform();
  }

  public void updateZoom(Point2D point, double scaleFactor) {
    currentScale += scaleFactor;
    if (point != null) {
      transform();
      Point2D ptJ = currentTransform.transform(point, null);
      x -= (ptJ.getX() - point.getX());
      y -= (ptJ.getY() - point.getY());
    }
    transform();
  }

  public void updateRotation() {
    //TODO
  }

  private void transform() {
    currentTransform = new AffineTransform();
    currentTransform.translate(x, y);
    currentTransform.scale(currentScale, currentScale);
    shape = currentTransform.createTransformedShape(origShape);
  }

  public Shape getShape() {
    return shape;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
    this.origShape = new Area(shape);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
