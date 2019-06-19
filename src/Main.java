import suffix_tree.SuffixTree;
import utils.Preprocessor;

import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Main {

	private static void createFilelong(String name, ArrayList<long[]> arreglo) {
		StringBuilder output = new StringBuilder();
		for(long[] l: arreglo) {
			for (long ll : l){
				output.append(ll);
				output.append(" ");
			}
			output.append("\n");
		}
		try (FileWriter writer = new FileWriter(name); BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(output.toString());
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	private static void createFileintlist(String name, ArrayList<int[]> arreglo) {
		StringBuilder output = new StringBuilder();
		for(int[] l: arreglo) {
			for (int ll : l){
				output.append(ll);
				output.append(" ");
			}
			output.append("\n");
		}
		try (FileWriter writer = new FileWriter(name); BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(output.toString());
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	private static void createFileint(String name, long[] arreglo) {
		StringBuilder output = new StringBuilder();
		for(long l: arreglo) {
			output.append(l);
			output.append("\n");
		}
		try (FileWriter writer = new FileWriter(name); BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(output.toString());
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}

	public static void test1(){
		String s = "guaguaalagua\0";
		SuffixTree st = new SuffixTree(s);
		String target = "guaa";
		Integer[] numbers = st.locate(target);
		System.out.println("Done!");
	}

	public static String randomString(String baseText){
		Random random = new Random();
		int len = baseText.length();
		int start, off;
		do {
			start = random.nextInt(len);
			off = random.nextInt(len);
		} while (start < 1 || len < 1 || start == off);
		return baseText.substring(Math.min(start,off), Math.max(start,off));
	}

	public static String twoNString(int i, String p){

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

	public static void testCount(int N, String p, String target){
		int num = 0;
		String content = twoNString(N, p);
		//System.out.println(content); // printer for debug
		SuffixTree st = new SuffixTree(content);
		num = st.count(target);
		System.out.println("Numero de repeticiones: " + num);
	}

	public static void testLocate(int N, String p, String target){
		String content = twoNString(N, p);
		//System.out.println(content); // printer for debug
		SuffixTree st = new SuffixTree(content);
		Integer[] numbers = st.locate(target);
		try{
			for (int num : numbers)
				System.out.print(num + " ");
		}catch(ArrayIndexOutOfBoundsException e){
		}finally {
			System.out.println("No se encuentra");
		}

	}

	public static void testTopKQ(int N, String p, int k, int q) {
		String content = twoNString(N, p);
		//System.out.println(content); // printer for debug
		SuffixTree st = new SuffixTree(content);
		String[] strList = st.top_K_Q( k,  q);
		if(strList[0] != null) {
			for (String str : strList)
				System.out.println(str);
			System.out.println("");
		}
	}

	public static void preprocess(){
		new Preprocessor("").preprocessDNA("data/dna.50MB", "data/dna_proc");
		new Preprocessor("").preprocessEnglish("data/english.50MB", "data/english_proc");
	}

	//TESTS DE TIEMPOS
	public static void testTiempos(int N, char origin_text){
		assert origin_text == 'e' || origin_text == 'd';
		if(origin_text == 'e') {
			System.out.println("Test para texto en ingles.");
		} else {
			System.out.println("Test para secuencias de ADN.");
		}
		long[] tree_build_time = new long[N];
		long[] number_of_nodes = new long[N];
		ArrayList<int[]> all_lenP = new ArrayList<>();
		ArrayList<long[]> all_count_time = new ArrayList<>();
		ArrayList<long[]> all_locate_time = new ArrayList<>();
		ArrayList<int[]> all_k = new ArrayList<>();
		ArrayList<int[]> all_q = new ArrayList<>();
		ArrayList<long[]> all_topk_time = new ArrayList<>();

		File directory = new File("results");
		if (!directory.exists()){
			directory.mkdir();
		}

		for(int i = 10; i<N+10; i++){
			//obtener texto tamaño i
			System.out.println("Iteracion " + i + ".\tCreando substrings");
			String texto_i;
			if(origin_text=='e') {
				texto_i = twoNString(i, "data/english_proc");
			}else{
				texto_i = twoNString(i, "data/dna_proc");
			}
			//crear strings para las consultas
			int sub_ammount = Math.round(texto_i.length()/10);
			ArrayList<String> sub_str = new ArrayList<>();
			for(int j=0; j<sub_ammount; j++){
				sub_str.add(randomString(texto_i));
			}
			System.out.println("Iteracion " + i + ".\tCreando arbol");
			//crear arbol
			long start = System.currentTimeMillis();
			SuffixTree st = new SuffixTree(texto_i);
			tree_build_time[i-10] = System.currentTimeMillis() - start;

			//medir tamaño del arbol en disco

			number_of_nodes[i-10] = st.size();// Es dificil sacar la memoria del arbol y todos los nodos

			System.out.println("Iteracion " + i + ".\tRealizando consultas count & locate");
			//realizar consultas count y locate
			long[] countTime = new long[sub_ammount];
			long[] locateTime = new long[sub_ammount];
			int[] lenP = new int[sub_ammount];
			for(int j=0; j<sub_ammount; j++){
				String a = sub_str.get(j);
				lenP[j] = a.length();
				long start_count_j = System.currentTimeMillis();
				st.count(a);
				countTime[j] = System.currentTimeMillis() - start_count_j;
				long start_locate_j = System.currentTimeMillis();
				st.locate(a);
				locateTime[j] = System.currentTimeMillis() - start_locate_j;
			}
			all_lenP.add(lenP);
			all_count_time.add(countTime);
			all_locate_time.add(locateTime);

			System.out.println("Iteracion " + i + ".\tRealizando consultas topK-Q");
			//consultas topK_Q
			Random random = new Random();
			int random_k = 0;
			int random_q = 0;
			int[] list_k = {3, 5, 10};
			int[] list_q_eng = {4, 5, 6, 7};
			int[] list_q_dna = {4, 8, 16, 32};
			int[] list_q;
			if(origin_text=='d'){
				list_q = list_q_eng.clone();
			}else{
				list_q = list_q_dna.clone();
			}
			long[] topkTime = new long[sub_ammount];
			int[] randomK = new int[sub_ammount];
			int[] randomQ = new int[sub_ammount];
			for (int k = 0; k < sub_ammount; k++) {

				random_k = list_k[random.nextInt(3)];

				random_q = list_q[random.nextInt(4)];

				randomK[k] = random_k;
				randomQ[k] = random_q;
				long start_top = System.currentTimeMillis();
				st.top_K_Q(random_k, random_q);
				topkTime[k] = System.currentTimeMillis() - start_top;
			}
			all_k.add(randomK);
			all_q.add(randomQ);
			all_topk_time.add(topkTime);

		}
		//guardar mediciones
		System.out.println("Creando archivos de datos");
		createFileint("results/tree_build_time_" + origin_text, tree_build_time);
		createFileint("results/nodes_" + origin_text, number_of_nodes);
		createFileintlist("results/all_lenP_" + origin_text, all_lenP);
		createFilelong("results/all_count_time_" + origin_text, all_count_time);
		createFilelong("results/all_locate_time_" + origin_text, all_locate_time);
		createFileintlist("results/all_k_" + origin_text, all_k);
		createFileintlist("results/all_q_" + origin_text, all_q);
		createFilelong("results/all_topk_time_" + origin_text, all_topk_time);
	}

	public static void main(String[] args) {
		//descomentar para obtener los textos preprocesados
		//preprocess();
		testTiempos(5, 'e');
		testTiempos(5, 'd');
	}
}
