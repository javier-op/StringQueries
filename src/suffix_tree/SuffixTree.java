package suffix_tree;

public class SuffixTree {

    private String text;
    private SuffixNode root;

    public SuffixTree(String text) {
        this.text = text;
        cleanText();
        root = new SuffixNode();
    }

    private void cleanText() {
    }

    
}
