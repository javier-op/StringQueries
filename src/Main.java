import suffix_tree.SuffixTree;
import utils.Preprocessor;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Main {

	private static final int[] len = {8, 16, 32, 64};
	private static final int[] k = {3, 5, 10};
	private static final int[] q_e = {4, 5, 6, 7};
	private static final int[] q_d = {4, 8, 16, 32};
	private static final Random r = new Random();
	private static String text;
	private static String[] words;

	private static void createFile(String name, long[] array) {
		StringBuilder output = new StringBuilder();
		for(long l: array) {
			output.append(l);
			output.append(" ");
		}
		try (FileWriter writer = new FileWriter(name); BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(output.toString());
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	private static String randomString(char mode){
		String output = "";
		if(mode == 'e') {
		    while(output.equals("")) {
                int rnd = r.nextInt(words.length);
                output = words[rnd];
            }
		} else {
			int choice = r.nextInt(len.length);
			int index = r.nextInt(text.length() - len[choice]);
			output = text.substring(index, index + len[choice]);
		}
		return output;
	}

	private static int randomK() {
		int choice = r.nextInt(k.length);
		return k[choice];
	}

	private static int randomQ(char mode) {
		int[] q = (mode == 'e') ? q_e: q_d;
		int choice = r.nextInt(q.length);
		return q[choice];
	}

	private static String twoNString(int i, String p){

		int N = (int) Math.pow(2, i);
		String content = "";
		try {
			content = Files.readString(Paths.get(p));
			int len = content.length();
			content = content.substring(len-N, len);
		}catch(IOException e){
			e.printStackTrace();
		}
		return content;
	}

	public static void preprocess(){
		new Preprocessor("").preprocessDNA("data/dna.50MB", "data/dna_proc");
		new Preprocessor("").preprocessEnglish("data/english.50MB", "data/english_proc");
	}

	//TESTS DE TIEMPOS
	private static void testTiempos(int N, char mode){
		assert mode == 'e' || mode == 'd';
		if(mode == 'e') {
			System.out.println("Test para texto en ingles.");
		} else {
			System.out.println("Test para secuencias de ADN.");
		}
		int op_iterations = (int) Math.pow(2, N+9)/10;
		long[] tree_build_time = new long[N];
		long[] number_of_nodes = new long[N];
		long[] lenP = new long[op_iterations];
		long[] count_time = new long[op_iterations];
		long[] locate_time = new long[op_iterations];
		long[] k = new long[op_iterations];
		long[] q = new long[op_iterations];
		long[] topk_time = new long[op_iterations];

		File directory = new File("results");
		if (!directory.exists()){
			directory.mkdir();
		}
		String texto_i = "";
		SuffixTree st = new SuffixTree("");
		for(int i = 10; i<N+10; i++){
			//obtener texto tamaño i
			System.out.println("Iteracion " + i + ".\tCreando substrings");
			if(mode=='e') {
				texto_i = twoNString(i, "data/english_proc");
			}else{
				texto_i = twoNString(i, "data/dna_proc");
			}
			System.out.println("Iteracion " + i + ".\tCreando arbol");
			//crear arbol
			long start = System.nanoTime();
			st = new SuffixTree(texto_i);
			tree_build_time[i-10] = System.nanoTime() - start;

			//medir tamaño del arbol en disco

			number_of_nodes[i-10] = st.size();// Es dificil sacar la memoria del arbol y todos los nodos
		}

		System.out.println("Realizando consultas count & locate");

		text = texto_i;
		words = text.split(" ");
		for(int i=0; i < op_iterations; i++){
			String a = randomString(mode);
			lenP[i] = a.length();
			long start_count = System.nanoTime();
			st.count(a);
			count_time[i] = System.nanoTime() - start_count;
			long start_locate = System.nanoTime();
			st.locate(a);
			locate_time[i] = System.nanoTime() - start_locate;
		}


		System.out.println("Realizando consultas topK-Q");
		for(int i=0; i < op_iterations/10; i++){
			int current_k = randomK();
			k[i] = current_k;
			int current_q = randomQ(mode);
			q[i] = current_q;
			long start_topkq = System.nanoTime();
			st.top_K_Q(current_k, current_q);
			topk_time[i] = System.nanoTime() - start_topkq;
		}

		//guardar mediciones
		System.out.println("Creando archivos de datos");
		createFile("results/tree_build_time_" + mode, tree_build_time);
		createFile("results/nodes_" + mode, number_of_nodes);
		createFile("results/lenP_" + mode, lenP);
		createFile("results/count_time_" + mode, count_time);
		createFile("results/locate_time_" + mode, locate_time);
		createFile("results/k_" + mode, k);
		createFile("results/q_" + mode, q);
		createFile("results/topk_time_" + mode, topk_time);
	}

	public static void main(String[] args) {
		//descomentar para obtener los textos preprocesados
		//preprocess();
		testTiempos(11, 'e');
		testTiempos(11, 'd');
	}
}
