package raytracer;

public final class Ray {

    public Vec P, D;

    public Ray(Vec pnt, Vec dir) {
        P = new Vec(pnt.x, pnt.y, pnt.z);
        D = new Vec(dir.x, dir.y, dir.z);
        D.normalize();
    }

    public Ray() {
        P = new Vec();
        D = new Vec();
    }

    public Vec point(double t) {
        return new Vec(P.x + D.x * t, P.y + D.y * t, P.z + D.z * t);
    }

    public String toString() {
        return "{" + P.toString() + " -> " + D.toString() + "}";
    }
}
