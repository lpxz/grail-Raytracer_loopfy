package raytracer;

public class RayTracer {

    Scene scene;

    /**
   * Lights for the rendering scene
   */
    Light lights[];

    /**
   * Objects (spheres) for the rendering scene
   */
    Primitive prim[];

    /**
   * The view for the rendering scene
   */
    View view;

    /**
   * Temporary ray
   */
    Ray tRay = new Ray();

    /**
   * Alpha channel
   */
    static final int alpha = 255 << 24;

    /**
   * Null vector (for speedup, instead of <code>new Vec(0,0,0)</code>
   */
    static final Vec voidVec = new Vec();

    /**
   * Temporary vect
   */
    Vec L = new Vec();

    /**
   * Current intersection instance (only one is needed!)
   */
    Isect inter = new Isect();

    /**
   * Height of the <code>Image</code> to be rendered
   */
    int height;

    /**
   * Width of the <code>Image</code> to be rendered
   */
    int width;

    int datasizes[] = { 10, 10 };

    long checksum = 0;

    int size;

    int numobjects;

    /**
   * Create and initialize the scene for the rendering picture.
   * @return The scene just created
   */
    Scene createScene() {
        int x = 0;
        int y = 0;
        Scene scene = new Scene();
        Primitive p;
        int nx = 4;
        int ny = 4;
        int nz = 4;
        edu.hkust.clap.monitor.Monitor.loopBegin(9);
for (int i = 0; i < nx; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(9);
{
            edu.hkust.clap.monitor.Monitor.loopBegin(10);
for (int j = 0; j < ny; j++) { 
edu.hkust.clap.monitor.Monitor.loopInc(10);
{
                edu.hkust.clap.monitor.Monitor.loopBegin(11);
for (int k = 0; k < nz; k++) { 
edu.hkust.clap.monitor.Monitor.loopInc(11);
{
                    double xx = 20.0 / (nx - 1) * i - 10.0;
                    double yy = 20.0 / (ny - 1) * j - 10.0;
                    double zz = 20.0 / (nz - 1) * k - 10.0;
                    p = new Sphere(new Vec(xx, yy, zz), 3);
                    p.setColor(0, 0, (i + j) / (double) (nx + ny - 2));
                    p.surf.shine = 15.0;
                    p.surf.ks = 1.5 - 1.0;
                    p.surf.kt = 1.5 - 1.0;
                    scene.addObject(p);
                }} 
edu.hkust.clap.monitor.Monitor.loopEnd(11);

            }} 
edu.hkust.clap.monitor.Monitor.loopEnd(10);

        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(9);

        scene.addLight(new Light(100, 100, -50, 1.0));
        scene.addLight(new Light(-100, 100, -50, 1.0));
        scene.addLight(new Light(100, -100, -50, 1.0));
        scene.addLight(new Light(-100, -100, -50, 1.0));
        scene.addLight(new Light(200, 200, 0, 1.0));
        View v = new View(new Vec(x, 20, -30), new Vec(x, y, 0), new Vec(0, 1, 0), 1.0, 35.0 * 3.14159265 / 180.0, 1.0);
        scene.setView(v);
        return scene;
    }

    public void setScene(Scene scene) {
        int nLights = scene.getLights();
        int nObjects = scene.getObjects();
        lights = new Light[nLights];
        prim = new Primitive[nObjects];
        edu.hkust.clap.monitor.Monitor.loopBegin(12);
for (int l = 0; l < nLights; l++) { 
edu.hkust.clap.monitor.Monitor.loopInc(12);
{
            lights[l] = scene.getLight(l);
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(12);

        edu.hkust.clap.monitor.Monitor.loopBegin(13);
for (int o = 0; o < nObjects; o++) { 
edu.hkust.clap.monitor.Monitor.loopInc(13);
{
            prim[o] = scene.getObject(o);
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(13);

        view = scene.getView();
    }

    public void render(Interval interval) {
        int row[] = new int[interval.width * (interval.yto - interval.yfrom)];
        int pixCounter = 0;
        int x, y, red, green, blue;
        double xlen, ylen;
        Vec viewVec;
        viewVec = Vec.sub(view.at, view.from);
        viewVec.normalize();
        Vec tmpVec = new Vec(viewVec);
        tmpVec.scale(Vec.dot(view.up, viewVec));
        Vec upVec = Vec.sub(view.up, tmpVec);
        upVec.normalize();
        Vec leftVec = Vec.cross(view.up, viewVec);
        leftVec.normalize();
        double frustrumwidth = view.dist * Math.tan(view.angle);
        upVec.scale(-frustrumwidth);
        leftVec.scale(view.aspect * frustrumwidth);
        Ray r = new Ray(view.from, voidVec);
        Vec col = new Vec();
        edu.hkust.clap.monitor.Monitor.loopBegin(14);
for (y = interval.yfrom + interval.threadid; y < interval.yto; y += JGFRayTracerBench.nthreads) { 
edu.hkust.clap.monitor.Monitor.loopInc(14);
{
            ylen = (double) (2.0 * y) / (double) interval.width - 1.0;
            edu.hkust.clap.monitor.Monitor.loopBegin(15);
for (x = 0; x < interval.width; x++) { 
edu.hkust.clap.monitor.Monitor.loopInc(15);
{
                xlen = (double) (2.0 * x) / (double) interval.width - 1.0;
                r.D = Vec.comb(xlen, leftVec, ylen, upVec);
                r.D.add(viewVec);
                r.D.normalize();
                col = trace(0, 1.0, r);
                red = (int) (col.x * 255.0);
                if (red > 255) red = 255;
                green = (int) (col.y * 255.0);
                if (green > 255) green = 255;
                blue = (int) (col.z * 255.0);
                if (blue > 255) blue = 255;
                checksum += red;
                checksum += green;
                checksum += blue;
                row[pixCounter++] = alpha | (red << 16) | (green << 8) | (blue);
            }} 
edu.hkust.clap.monitor.Monitor.loopEnd(15);

        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(14);

    }

    boolean intersect(Ray r, double maxt) {
        Isect tp;
        int i, nhits;
        nhits = 0;
        inter.t = 1e9;
        edu.hkust.clap.monitor.Monitor.loopBegin(16);
for (i = 0; i < prim.length; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(16);
{
            tp = prim[i].intersect(r);
            if (tp != null && tp.t < inter.t) {
                inter.t = tp.t;
                inter.prim = tp.prim;
                inter.surf = tp.surf;
                inter.enter = tp.enter;
                nhits++;
            }
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(16);

        return nhits > 0 ? true : false;
    }

    /**
   * Checks if there is a shadow
   * @param r The ray
   * @return Returns 1 if there is a shadow, 0 if there isn't
   */
    int Shadow(Ray r, double tmax) {
        if (intersect(r, tmax)) return 0;
        return 1;
    }

    /**
   * Return the Vector's reflection direction
   * @return The specular direction
   */
    Vec SpecularDirection(Vec I, Vec N) {
        Vec r;
        r = Vec.comb(1.0 / Math.abs(Vec.dot(I, N)), I, 2.0, N);
        r.normalize();
        return r;
    }

    /**
   * Return the Vector's transmission direction
   */
    Vec TransDir(Surface m1, Surface m2, Vec I, Vec N) {
        double n1, n2, eta, c1, cs2;
        Vec r;
        n1 = m1 == null ? 1.0 : m1.ior;
        n2 = m2 == null ? 1.0 : m2.ior;
        eta = n1 / n2;
        c1 = -Vec.dot(I, N);
        cs2 = 1.0 - eta * eta * (1.0 - c1 * c1);
        if (cs2 < 0.0) return null;
        r = Vec.comb(eta, I, eta * c1 - Math.sqrt(cs2), N);
        r.normalize();
        return r;
    }

    /**
   * Returns the shaded color
   * @return The color in Vec form (rgb)
   */
    Vec shade(int level, double weight, Vec P, Vec N, Vec I, Isect hit) {
        double n1, n2, eta, c1, cs2;
        Vec r;
        Vec tcol;
        Vec R;
        double t, diff, spec;
        Surface surf;
        Vec col;
        int l;
        col = new Vec();
        surf = hit.surf;
        R = new Vec();
        if (surf.shine > 1e-6) {
            R = SpecularDirection(I, N);
        }
        edu.hkust.clap.monitor.Monitor.loopBegin(17);
for (l = 0; l < lights.length; l++) { 
edu.hkust.clap.monitor.Monitor.loopInc(17);
{
            L.sub2(lights[l].pos, P);
            if (Vec.dot(N, L) >= 0.0) {
                t = L.normalize();
                tRay.P = P;
                tRay.D = L;
                if (Shadow(tRay, t) > 0) {
                    diff = Vec.dot(N, L) * surf.kd * lights[l].brightness;
                    col.adds(diff, surf.color);
                    if (surf.shine > 1e-6) {
                        spec = Vec.dot(R, L);
                        if (spec > 1e-6) {
                            spec = Math.pow(spec, surf.shine);
                            col.x += spec;
                            col.y += spec;
                            col.z += spec;
                        }
                    }
                }
            }
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(17);

        tRay.P = P;
        if (surf.ks * weight > 1e-3) {
            tRay.D = SpecularDirection(I, N);
            tcol = trace(level + 1, surf.ks * weight, tRay);
            col.adds(surf.ks, tcol);
        }
        if (surf.kt * weight > 1e-3) {
            if (hit.enter > 0) tRay.D = TransDir(null, surf, I, N); else tRay.D = TransDir(surf, null, I, N);
            tcol = trace(level + 1, surf.kt * weight, tRay);
            col.adds(surf.kt, tcol);
        }
        tcol = null;
        surf = null;
        return col;
    }

    /**
   * Launches a ray
   */
    Vec trace(int level, double weight, Ray r) {
        Vec P, N;
        boolean hit;
        if (level > 6) {
            return new Vec();
        }
        hit = intersect(r, 1e6);
        if (hit) {
            P = r.point(inter.t);
            N = inter.prim.normal(P);
            if (Vec.dot(r.D, N) >= 0.0) {
                N.negate();
            }
            return shade(level, weight, P, N, r.D, inter);
        }
        return voidVec;
    }

    public static void main(String argv[]) {
        RayTracer rt = new RayTracer();
        rt.scene = rt.createScene();
        rt.setScene(rt.scene);
        Interval interval = new Interval(0, rt.width, rt.height, 0, rt.height, 1, 0);
        rt.render(interval);
    }
}
