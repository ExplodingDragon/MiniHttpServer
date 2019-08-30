package top.fksoft.server.http;

/**
 * @author ExplodingDragon
 * @version 1.0
 */
public class Test {


    public int BoyerMooreSearch(byte[] src, byte[] pat ,int startIndex) {
        int[] right = new int[256];
        int srcSize = src.length - startIndex;
        int patSize = pat.length;
        for (int i = 0; i < 256; i++) {
            right[i] = -1;
        }
        for (int i = 0; i < pat.length; i++) {
            right[pat[i]] = i;
        }
        int skip;
        for (int i = startIndex; i <= srcSize - patSize; i += skip) {
            skip = 0;
            for (int j = patSize - 1; j >= 0; j--) {
                if (pat[j] != src[i + j]) {
                    skip = j - right[src[i + j]];
                    if (skip < 1) {
                        skip = 1;
                    }
                    break;
                }
            }
            if (skip == 0)
                return i;
        }
        return -1;
    }

    @org.junit.Test
    public  void main() {
        String txt = "BBC ABCDAB AACDABABCDABCDABABCDABABCDABDABCDABDDDE";
        String pat = "ABCDABD";
        System.out.println(BoyerMooreSearch(txt.getBytes(), pat.getBytes(),21));
    }
}
