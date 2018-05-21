import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhuaze on 2018/5/20.
 */
public class Chunk {
    List<String> words = new ArrayList<>();
    int size;

    int charsLenTotal; // Chunk中包含的词的字数之和
    double charsLenAve; // Chunk中包含的词的平均字数
    double charsLenVariance; // Chunk中包含的词的字数标准差
    double freedom; // Chunk中所有单字词的log频率之和

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCharsLenTotal() {
        return charsLenTotal;
    }

    public void setCharsLenTotal() {
        int len = 0;
        for (String word: words) {
            len += word.length();
        }
        this.charsLenTotal = len;
    }

    public double getCharsLenAve() {
        return charsLenAve;
    }

    public void setCharsLenAve() {
        this.charsLenAve = 1.0 * charsLenTotal / size;
    }

    public double getCharLenVariance() {
        return charsLenVariance;
    }

    public void setCharLenVariance() {
        double variance = 0;
        double total = 0;
        for (String word: words) {
            total += (word.length() - charsLenAve) * (word.length() - charsLenAve);
        }
        variance = total / size;
        this.charsLenVariance = variance;
    }

    public double getFreedom() {
        return freedom;
    }

    public void setFreedom() {
        double free = 0;
        for (String word: words) {
            if ((word.length() == 1) && MMSeg.charsDict.containsKey(word)) {
                free += MMSeg.charsDict.get(word);
            }
        }
        this.freedom = free;
    }

    public static Chunk copyChunk(Chunk chunk) {
        Chunk newChunk = new Chunk();
        newChunk.words.addAll(chunk.words);
        return newChunk;
    }
}