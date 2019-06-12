package suffix_tree;

public class SuffixTree {

    private String text;
    private SuffixNode root;

    public SuffixTree(String text) {
        this.text = text;
        root = new SuffixNode(this);
    }

    public char getChar(int index){
        return text.charAt(index);
    }

    public void insert(int index){
        root.insert(index);
    }

    public int getLastIndex(){
        return text.length();
    }

    public String getSubString(int from){
        return text.substring(from);
    }

    public String getSubString(int from, int to){
        return text.substring(from, to);
    }

    public SuffixNode searchNode(){
        return root.
    }

}
