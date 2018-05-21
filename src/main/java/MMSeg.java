import java.io.*;
import java.util.*;

/**
 * Created by shenhuaze on 2018/5/18.
 */
public class MMSeg {
    // 词典中最长的词长大约为6，词长更长的词也存在，但很少，就不考虑了
    private final int WORD_MAX_LEN = 6;
    private final String CHARS_PATH = "dict/chars.dic";
    private final String WORDS_PATH = "dict/words.dic";
    public static Map<String, Double> charsDict = new HashMap<>();
    public static Set<String> wordsDict = new HashSet<>();

    public MMSeg() {
        loadCharsDict();
        loadWordsDict();
    }

    public List<String> complexSeg(String str) {
        List<String> segList = new ArrayList<>();
        String selectedWord = findSelectedWord(str);
        segList.add(selectedWord);
        int currWordLen = selectedWord.length();
        // 递归调用complexSeg方法
        if (str.length() > currWordLen) {
            segList.addAll(complexSeg(str.substring(currWordLen)));
        }
        return segList;
    }

    public String findSelectedWord(String str) {
        List<Chunk> chunkList = getChunks(str);
        // 如果只找到一个Chunk，就直接返回
        if (chunkList.size() == 1) {
            return chunkList.get(0).words.get(0);
        }
        // 如果找到多个Chunk，就用规则过滤
        Chunk selectedChunk = applyRules(chunkList);
        return selectedChunk.words.get(0);
    }

    public Chunk applyRules(List<Chunk> chunkList) {
        // 依次用四个规则过滤
        List<Chunk> chunksAfterRule1 = Rule.rule1(chunkList);
        if (chunksAfterRule1.size() == 1) {
            return chunksAfterRule1.get(0);
        } else {
            List<Chunk> chunksAfterRule2 = Rule.rule2(chunksAfterRule1);
            if (chunksAfterRule2.size() == 1) {
                return chunksAfterRule2.get(0);
            } else {
                List<Chunk> chunksAfterRule3 = Rule.rule3(chunksAfterRule2);
                if (chunksAfterRule3.size() == 1) {
                    return chunksAfterRule3.get(0);
                } else {
                    List<Chunk> chunksAfterRule4 = Rule.rule4(chunksAfterRule3);
                    // 用第四个规则过滤后，一般都只剩下一个Chunk了，可以直接返回，如果
                    // 仍存在多个Chunk，那就返回第一个就行了
                    return chunksAfterRule4.get(0);
                }
            }
        }
    }

    private List<Chunk> getChunks(String str) {
        List<Chunk> chunkList = new ArrayList<>();
        // 用三重循环找出当前字符串str从起始位置开始的所有Chunk
        for (int i = 1; i <= WORD_MAX_LEN && i <= str.length(); i++) {
            Chunk chunk = new Chunk();
            String first = str.substring(0, i);
            if (first.length() == 1 || wordsDict.contains(first)) {
                chunk.words.add(first);
                if (i == str.length()) {
                    Chunk newChunk = Chunk.copyChunk(chunk);
                    chunkList.add(newChunk);
                }
                for (int j = 1; j <= WORD_MAX_LEN && i + j <= str.length(); j++) {
                    String second = str.substring(i, i + j);
                    if (second.length() == 1 || wordsDict.contains(second)) {
                        chunk.words.add(second);
                        if (i + j == str.length()) {
                            Chunk newChunk = Chunk.copyChunk(chunk);
                            chunkList.add(newChunk);
                        }
                        for (int k = 1; k <= WORD_MAX_LEN && i + j + k <= str.length(); k++) {
                            String third = str.substring(i + j, i + j + k);
                            if (third.length() == 1 || wordsDict.contains(third)) {
                                chunk.words.add(third);
                                // deep copy
                                Chunk newChunk = Chunk.copyChunk(chunk);
                                chunkList.add(newChunk);
                                chunk.words.remove(chunk.words.size() - 1);
                            }
                        }
                        chunk.words.remove(chunk.words.size() - 1);
                    }
                }
                chunk.words.remove(chunk.words.size() - 1);
            }
        }
        for (Chunk chunk: chunkList) {
            chunk.setSize(chunk.words.size());
            chunk.setCharsLenTotal();
            chunk.setCharsLenAve();
            chunk.setCharLenVariance();
            chunk.setFreedom();
        }
        return chunkList;
    }

    public void loadCharsDict() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CHARS_PATH));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplit = line.split(" ");
                if (lineSplit.length == 2) {
                    String ch = lineSplit[0];
                    double freq = Double.valueOf(lineSplit[1]);
                    // 为了防止freq很小，乘以10之和再取log
                    freq = Math.log(freq * 10);
                    charsDict.put(ch, freq);
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadWordsDict() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(WORDS_PATH));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                wordsDict.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
