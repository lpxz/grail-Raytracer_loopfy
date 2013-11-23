package raytracer;

public class Sphere extends Primitive implements java.io.Serializable {

    Vec c;

    double r, r2;

    Vec v, b;

    public Sphere(Vec center, double radius) {
        c = center;
        r = radius;
        r2 = r * r;
        v = new Vec();
        b = new Vec();
    }

    public Isect intersect(Ray ry) {
        double b, disc, t;
        Isect ip;
        v.sub2(c, ry.P);
        b = Vec.dot(v, ry.D);
        disc = b * b - Vec.dot(v, v) + r2;
        if (disc < 0.0) {
            return null;
        }
        disc = Math.sqrt(disc);
        t = (b - disc < 1e-6) ? b + disc : b - disc;
        if (t < 1e-6) {
            return null;
        }
        ip = new Isect();
        ip.t = t;
        ip.enter = Vec.dot(v, v) > r2 + 1e-6 ? 1 : 0;
        ip.prim = this;
        ip.surf = surf;
        return ip;
    }

    public Vec normal(Vec p) {
        Vec r;
        r = Vec.sub(p, c);
        r.normalize();
        return r;
    }

    public String toString() {
        return "Sphere {" + c.toString() + "," + r + "}";
    }

    public Vec getCenter() {
        return c;
    }

    public void setCenter(Vec c) {
        this.c = c;
    }
}
