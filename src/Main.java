import suffix_tree.SuffixTree;

public class Main {
    public static void main(String[] args) {
        String s= "guaguaalagua\0";
        SuffixTree st = new SuffixTree(s);

        for(int i = 0; i < s.length(); i++) {
            st.insert(i);
        }

        System.out.println("Done!");
    }
}
