/**
 * Created by shenhuaze on 2018/5/18.
 */
public class DemoTest {
    public static void main(String[] args) {
        MMSeg mmSeg = new MMSeg();
        String[] strs = new String[]{"研究生命起源", "为首要考虑", "眼看就要来了", "中西伯利亚",
                "国际化", "化装和服装", "中国人民银行"};
        for (String str: strs) {
            System.out.println(mmSeg.complexSeg(str).toString());
        }
    }
}
