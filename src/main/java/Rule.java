import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhuaze on 2018/5/20.
 */
public class Rule {
    // 规则一：备选词组合的长度之和最大
    public static List<Chunk> rule1(List<Chunk> chunkList) {
        List<Chunk> results = new ArrayList<>();
        int maxLen = findMaxLen(chunkList);
        for (Chunk chunk: chunkList) {
            if (chunk.charsLenTotal == maxLen) {
                results.add(chunk);
            }
        }
        return results;
    }

    // 规则二：备选词组合的平均词长最大
    public static List<Chunk> rule2(List<Chunk> chunkList) {
        List<Chunk> results = new ArrayList<>();
        // 由于经过规则一之后，chunkList中所有词的词长总和已经是一样的了，这里要选
        // 平均词长最大的，有两种方法，一种是选size最小的，另一种是直接选平均词长最
        // 长的。
        // 这里选size最小的，即包含的word数最少的Chunk。
        int minSize = findMinSize(chunkList);
        for (Chunk chunk: chunkList) {
            if (chunk.size == minSize) {
                results.add(chunk);
            }
        }
        return results;
    }

    // 规则三：备选词组合的词长标准差最小
    public static List<Chunk> rule3(List<Chunk> chunkList) {
        List<Chunk> results = new ArrayList<>();
        double minVar = findMinVar(chunkList);
        for (Chunk chunk: chunkList) {
            if (chunk.charsLenVariance == minVar) {
                results.add(chunk);
            }
        }
        return results;
    }

    // 规则四：备选词组合中单字词的log频率之和最高
    public static List<Chunk> rule4(List<Chunk> chunkList) {
        List<Chunk> results = new ArrayList<>();
        double maxFreedom = findMaxLogFreqSum(chunkList);
        for (Chunk chunk: chunkList) {
            if (chunk.freedom == maxFreedom) {
                results.add(chunk);
            }
        }
        return results;
    }

    public static int findMaxLen(List<Chunk> chunkList) {
        int maxLen = 0;
        for (Chunk chunk: chunkList) {
            if (chunk.charsLenTotal > maxLen) {
                maxLen = chunk.charsLenTotal;
            }
        }
        return maxLen;
    }

    public static double findMaxAve(List<Chunk> chunkList) {
        double maxAve = 0;
        for (Chunk chunk: chunkList) {
            if (chunk.charsLenAve > maxAve) {
                maxAve = chunk.charsLenAve;
            }
        }
        return maxAve;
    }

    public static int findMinSize(List<Chunk> chunkList) {
        // 这里minSize的初始值设为100，只要新来一个词，就会将它更新
        int minSize = 100;
        for (Chunk chunk: chunkList) {
            if (chunk.size < minSize) {
                minSize = chunk.size;
            }
        }
        return minSize;
    }

    public static double findMinVar(List<Chunk> chunkList) {
        double minVar = Double.MAX_VALUE;
        for (Chunk chunk: chunkList) {
            if (chunk.charsLenVariance < minVar) {
                minVar = chunk.charsLenVariance;
            }
        }
        return minVar;
    }

    public static double findMaxLogFreqSum(List<Chunk> chunkList) {
        double maxFreedom = 0;
        for (Chunk chunk: chunkList) {
            if (chunk.freedom > maxFreedom) {
                maxFreedom = chunk.freedom;
            }
        }
        return maxFreedom;
    }
}
