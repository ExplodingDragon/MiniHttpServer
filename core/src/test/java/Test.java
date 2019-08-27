/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class Test {
    private volatile int xx;
    private int[] mdbytes = new int[12];

    public Test(int xx) {
        this.xx = xx;
    }

    public Test() {
        int s = mdbytes[12] & 0xff;
    }

    public int getXx() {
        return xx;
    }

    public  class Run2{
        public Run2() {
            xx = 1;
        }
    }
}
