public class CheckHeapSize {
 static long heapSize = Runtime.getRuntime().totalMemory();
 static long heapMaxSize = Runtime.getRuntime().maxMemory();
 static long heapFreeSize = Runtime.getRuntime().freeMemory();

 public static void main(String[] args) {
  System.out.println("(Approx) heapsize,\t" + formatSize(CheckHeapSize.heapSize));
  System.out.println("(Approx) heapsize max,\t" + formatSize(CheckHeapSize.heapMaxSize));
  System.out.println("(Approx) heapsize free,\t" + formatSize(CheckHeapSize.heapFreeSize));
  return;
 }
    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }
};