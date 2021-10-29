package ballcollision;

import processing.core.PApplet;

/**
 * Class BallCollision pour projet BallCollision. Fait apparaitre des boules 
 * dans le coin haut gauche du canvas, et les fait rebondir entre elles suivant
 * une collision elastique.
 * @author Xavier
 */
public class BallCollision extends PApplet {
  /** Nombre de boules a faire apparaitre */
  private final int MAXBALLS = 12;
  /** Tableau de boules */
  private Ball[] balls;

  /** Point d'entree de l'application */
  public static void main(String[] args) {
    PApplet.main(BallCollision.class.getName());
  }

  /** Setup du PApplet */
  @Override
  public void settings() {
    size(600, 600);
  }

  /** Setup de la fenetre */
  @Override
  public void setup() {
    surface.setLocation(1040, 180);
    balls = new Ball[MAXBALLS];
    for (int i = 0; i < balls.length; i++) {
      balls[i] = new Ball(this);
    }
  }

  /** Gere l'animation */
  @Override
  public void draw() {
    background(39, 39, 34);

    for (Ball b : balls) {
      b.initBounce(balls);
      b.update();
      b.show();
    }

    for (int i = 0; i < balls.length; i++) {
      for (int j = i + 1; j < balls.length; j++) {
        if(balls[i].detectCollision(balls[j])) {
          balls[i].handleCollision(balls[j]);
        }
      }
    }
  }

}
