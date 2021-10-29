package ballcollision;

import processing.core.PApplet;
import processing.core.PVector;
import static processing.core.PApplet.map;

/**
 * Class Ball pour projet BallCollision. Une boule est un cercle qui se balade
 * dans le canvas, en rebondissant contre les bords et les autres boules.
 * @author Xavier
 */
public class Ball {
  /** PApplet parent */
  private final PApplet app;
  /** Position de la boule */
  private final PVector pos;
  /** Velocite de la boule */
  private final PVector vel;
  /** Diametre */
  private final float dia;
  /** Repere si la boule est autorisee a rebondir contre les autres boules */
  private boolean canBonk;

  /**
   * Constructeur d'une boule. Le diametre est choisi de maniere aleatoire,
   * entre 50 et 100 px. La vitesse est inversement proportionnelle a la taille,
   * une grosse boule ira plus lentement par exemple.
   * @param app PApplet parent
   */
  public Ball(PApplet app) {
    this.app = app;
    this.dia = app.random(50, 100);
    pos = new PVector(dia / 2, dia / 2);
    vel = new PVector(app.random(-1, 1), app.random(-1, 1));
    float velInitMag = map(dia, 50, 100, 3, 1);
    vel.setMag(velInitMag);
    canBonk = false;
  }

  /**
   * Toutes les boules sont creees les unes par dessus les autres. Une boule
   * n'est pas autorisee a rebondir contre les autres boules tant qu'elle ne
   * s'est pas isolee (pas confondue avec une autre boule).
   * @param others Tableau des boules
   */
  public void initBounce(Ball[] others) {
    for (Ball b : others) {
      if (b != this) {
        if (!canBonk) {
          PVector distance = PVector.sub(b.pos, pos);
          if (distance.mag() <= (dia + b.dia) / 2) {
            return;
          }
        }
      }
    }
    canBonk = true;
  }

  /**
   * Verifie si une boule doit rebondir avec une autre boule.
   * @param other Autre boule
   * @return True si les deux boules sont en collision
   */
  public boolean detectCollision(Ball other) {
    if (canBonk && other.canBonk) {
      PVector distance = PVector.sub(pos, other.pos);
      if (distance.mag() < (dia + other.dia) / 2) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gere le rebond en deux phases. On corrige d'abord les positions des deux
   * boules pour s'assurer qu'elles ne soient pas confondues au coup suivant, et
   * on calcule leurs nouvelles velocites. (Collision elastique)
   * @param other Autre boule
   */
  public void handleCollision(Ball other) {
    PVector normal = PVector.sub(pos, other.pos);
    
    // Correction position
    float distance = normal.mag();
    float correction = (dia + other.dia) / 2 - distance;
    normal.normalize();
    pos.add(PVector.mult(normal, correction / 2 + Float.MIN_VALUE));
    other.pos.sub(PVector.mult(normal, correction / 2 + Float.MIN_VALUE));

    // Calcul des nouvelles velocites
    float a = PVector.dot(vel, normal);
    float b = PVector.dot(other.vel, normal);
    vel.add(PVector.mult(normal, b - a));
    other.vel.add(PVector.mult(normal, a - b));
  }

  /** Fait bouger la boule. Assure le rebond contre les bords du canvas */
  public void update() {
    pos.add(vel);

    if (pos.x < dia / 2) {
      pos.x = dia / 2;
      vel.x *= -1;
    } else if (pos.x > app.width - dia / 2) {
      pos.x = app.width - dia / 2;
      vel.x *= -1;
    }

    if (pos.y < dia / 2) {
      pos.y = dia / 2;
      vel.y *= -1;
    } else if (pos.y > app.height - dia / 2) {
      pos.y = app.height - dia / 2;
      vel.y *= -1;
    }
  }

  /**
   * Affiche la boule en gris tant qu'elle n'est pas autorisee a rebondir,
   * sinon, s'affiche en vert.
   */
  public void show() {
    if (canBonk) {
      app.fill(191, 231, 0);
    } else {
      app.fill(200, 127);
    }
    app.noStroke();
    app.ellipse(pos.x, pos.y, dia, dia);
  }
}
