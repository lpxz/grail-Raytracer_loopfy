package raytracer;

public class View implements java.io.Serializable {

    public final Vec from;

    public final Vec at;

    public final Vec up;

    public final double dist;

    public final double angle;

    public final double aspect;

    public View(Vec from, Vec at, Vec up, double dist, double angle, double aspect) {
        this.from = from;
        this.at = at;
        this.up = up;
        this.dist = dist;
        this.angle = angle;
        this.aspect = aspect;
    }
}
