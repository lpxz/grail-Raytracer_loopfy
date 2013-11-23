package raytracer;

import jgfutil.*;

public class JGFRayTracerBenchSizeB {

    public static int nthreads;

    public static void main(String argv[]) {
        if (argv.length != 0) {
            nthreads = Integer.parseInt(argv[0]);
        } else {
            System.out.println("The no of threads has not been specified, defaulting to 1");
            System.out.println("  ");
            nthreads = 1;
        }
        JGFInstrumentor.printHeader(3, 1, nthreads);
        JGFRayTracerBench rtb = new JGFRayTracerBench(nthreads);
        rtb.JGFrun(1);
    }
}
