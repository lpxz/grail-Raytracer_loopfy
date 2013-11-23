package raytracer;

import jgfutil.*;

public class JGFRayTracerBench extends RayTracer implements JGFSection3 {

    public static int nthreads;

    public static long checksum1 = 0;

    public static int staticnumobjects;

    public JGFRayTracerBench(int nthreads) {
        this.nthreads = nthreads;
    }

    public void JGFsetsize(int size) {
        this.size = size;
    }

    public void JGFinitialise() {
        width = height = datasizes[size];
    }

    public void JGFapplication() {
        Runnable thobjects[] = new Runnable[nthreads];
        Thread th[] = new Thread[nthreads];
        Barrier br = new TournamentBarrier(nthreads);
        edu.hkust.clap.monitor.Monitor.loopBegin(0);
for (int i = 1; i < nthreads; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(0);
{
            thobjects[i] = new RayTracerRunner(i, width, height, br);
            th[i] = new Thread(thobjects[i]);
            th[i].start();
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(0);

        thobjects[0] = new RayTracerRunner(0, width, height, br);
        thobjects[0].run();
        edu.hkust.clap.monitor.Monitor.loopBegin(1);
for (int i = 1; i < nthreads; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(1);
{
            try {
                th[i].join();
            } catch (InterruptedException e) {
            }
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(1);

    }

    public void JGFvalidate() {
        long refval[] = { 2676692, 29827635 };
        long dev = checksum1 - refval[size];
        if (dev != 0) {
            System.out.println("Validation failed");
            System.out.println("Pixel checksum = " + checksum1);
            System.out.println("Reference value = " + refval[size]);
        }
    }

    public void JGFtidyup() {
        scene = null;
        lights = null;
        prim = null;
        tRay = null;
        inter = null;
        System.gc();
    }

    public void JGFrun(int size) {
        JGFInstrumentor.addTimer("Section3:RayTracer:Total", "Solutions", size);
        JGFInstrumentor.addTimer("Section3:RayTracer:Init", "Objects", size);
        JGFInstrumentor.addTimer("Section3:RayTracer:Run", "Pixels", size);
        JGFsetsize(size);
        JGFInstrumentor.startTimer("Section3:RayTracer:Total");
        JGFinitialise();
        JGFapplication();
        JGFvalidate();
        JGFtidyup();
        JGFInstrumentor.stopTimer("Section3:RayTracer:Total");
        JGFInstrumentor.addOpsToTimer("Section3:RayTracer:Init", (double) staticnumobjects);
        JGFInstrumentor.addOpsToTimer("Section3:RayTracer:Run", (double) (width * height));
        JGFInstrumentor.addOpsToTimer("Section3:RayTracer:Total", 1);
        JGFInstrumentor.printTimer("Section3:RayTracer:Init");
        JGFInstrumentor.printTimer("Section3:RayTracer:Run");
        JGFInstrumentor.printTimer("Section3:RayTracer:Total");
    }
}

class RayTracerRunner extends RayTracer implements Runnable {

    int id, height, width;

    Barrier br;

    public RayTracerRunner(int id, int width, int height, Barrier br) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.br = br;
        JGFInstrumentor.startTimer("Section3:RayTracer:Init");
        scene = createScene();
        setScene(scene);
        numobjects = scene.getObjects();
        JGFRayTracerBench.staticnumobjects = numobjects;
        JGFInstrumentor.stopTimer("Section3:RayTracer:Init");
    }

    public void run() {
        Interval interval = new Interval(0, width, height, 0, height, 1, id);
        br.DoBarrier(id);
        if (id == 0) JGFInstrumentor.startTimer("Section3:RayTracer:Run");
        render(interval);
        synchronized (scene) {
            edu.hkust.clap.monitor.Monitor.loopBegin(2);
for (int i = 0; i < JGFRayTracerBench.nthreads; i++) { 
edu.hkust.clap.monitor.Monitor.loopInc(2);
if (id == i) JGFRayTracerBench.checksum1 = JGFRayTracerBench.checksum1 + checksum;} 
edu.hkust.clap.monitor.Monitor.loopEnd(2);

        }
        br.DoBarrier(id);
        if (id == 0) JGFInstrumentor.stopTimer("Section3:RayTracer:Run");
    }
}
