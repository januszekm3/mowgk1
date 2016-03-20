package pl.worker;

import java.util.HashSet;
import java.util.Set;

import pl.datamodel.DataHolder;

public class Worker {

	public static void main(String[] args) {
		long start, stop;
		int flag, i, j, k, l, wierzcholek0, wierzcholek1, element0, element1;
		Set<Integer> sasiedzi0, sasiedzi1;
		DataHolder dataHolder = new DataHolder("resources/ant.ply");
		//DataHolder dataHolder = new DataHolder("resources/cam.off");
		
		System.out.println("Tablica wierzcholkow:");
		for (i = 0; i < dataHolder.vertices.length; i++) {
			System.out.println(dataHolder.vertices[i][0] + " " + dataHolder.vertices[i][1] + " " + dataHolder.vertices[i][2]);
		}
		
		System.out.println("\nTablica elementow:");
		for (j = 0; j < dataHolder.faces.length; j++) {
			System.out.println(dataHolder.faces[j][0] + " " + dataHolder.faces[j][1] + " " + dataHolder.faces[j][2]);
		}
		System.out.println("\n3.1. Tablica wspolrzednych wierzcholkow oraz tzw. tablica polaczen"); 
		System.out.println("\n3.1.1.dla kazdego wierzcholka wyznaczanie otoczenia wierzcholkow (pierwsza i druga warstwa sasiednich wierzcholkow)");
		start = System.nanoTime();
		for (i = 0; i < dataHolder.vertices.length; i ++){
			System.out.print("Wierzcholek nr " + i + " pierwsi sasiedzi: ");
			sasiedzi0 = new HashSet<Integer>();
			sasiedzi1 = new HashSet<Integer>();
			for (j = 0; j < dataHolder.faces.length; j++) {
				if (dataHolder.faces[j][0] == i) {
					sasiedzi0.add(dataHolder.faces[j][1]);	//dodawanie
					sasiedzi0.add(dataHolder.faces[j][2]);	//obu sasiadow
				}
				if (dataHolder.faces[j][1] == i) {
					sasiedzi0.add(dataHolder.faces[j][0]);
					sasiedzi0.add(dataHolder.faces[j][2]);
				}
				if (dataHolder.faces[j][2] == i) {
					sasiedzi0.add(dataHolder.faces[j][0]);
					sasiedzi0.add(dataHolder.faces[j][1]);
				}
			}
			for (Integer a : sasiedzi0) {
				System.out.print(a + " ");
				for (j = 0; j < dataHolder.faces.length; j++) {
					if (dataHolder.faces[j][0] == a) {
						//czy nie dodajemy tego samego wierzcholka jako drugiego sasiada
						if (dataHolder.faces[j][1] != i) sasiedzi1.add(dataHolder.faces[j][1]);
						if (dataHolder.faces[j][2] != i) sasiedzi1.add(dataHolder.faces[j][2]);
					}
					if (dataHolder.faces[j][1] == a) {
						if (dataHolder.faces[j][0] != i) sasiedzi1.add(dataHolder.faces[j][0]);
						if (dataHolder.faces[j][2] != i) sasiedzi1.add(dataHolder.faces[j][2]);
					}
					if (dataHolder.faces[j][2] == a) {
						if (dataHolder.faces[j][0] != i) sasiedzi1.add(dataHolder.faces[j][0]);
						if (dataHolder.faces[j][1] != i) sasiedzi1.add(dataHolder.faces[j][1]);
					}
				}
			}
			System.out.print("drudzy sasiedzi: ");
			for (Integer a : sasiedzi1) {
				System.out.print(a + " ");
			}
			System.out.println();
		}
		
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.1.2.dla kazdego wierzcholka znalezienie elementow, do ktorych nalezy");
		start = System.nanoTime();
		for (i = 0; i < dataHolder.vertices.length; i ++){
			System.out.print("Wierzcholek nr " + i + " nalezy do elementow: ");
			for (j = 0; j < dataHolder.faces.length; j++)
				if(dataHolder.faces[j][0] == i || dataHolder.faces[j][1] == i || dataHolder.faces[j][2] == i)
					System.out.print(j + " ");
			System.out.println();
		}
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.1.3.dla kazdego elementu wyznaczenie otoczenia elementow (pierwsza i druga warstwa sasiednich elementow)");
		start = System.nanoTime();
		for (i = 0; i < dataHolder.faces.length; i++){
			System.out.print("Element nr " + i + " pierwsi sasiedzi: ");
			sasiedzi0 = new HashSet<Integer>();
			sasiedzi1 = new HashSet<Integer>();
			for (j = 0; j < 3; j++) {	//3 krawedzie w obrebie elementu
				wierzcholek0 = dataHolder.faces[i][j];
				wierzcholek1 = dataHolder.faces[i][(j+1)%3];
				for (k = 0; k < dataHolder.faces.length; k++) {
					if ((i != k) &&	//czy nie dodajemy tego samego elementu (przechodzimy drugi raz po tej samej tablicy)
						(((wierzcholek0 == dataHolder.faces[k][0] && wierzcholek1 == dataHolder.faces[k][1]) 
							|| (wierzcholek0 == dataHolder.faces[k][1] && wierzcholek1 == dataHolder.faces[k][0]))
						|| ((wierzcholek0 == dataHolder.faces[k][1] && wierzcholek1 == dataHolder.faces[k][2]) 
							|| (wierzcholek0 == dataHolder.faces[k][2] && wierzcholek1 == dataHolder.faces[k][1]))
						|| ((wierzcholek0 == dataHolder.faces[k][0] && wierzcholek1 == dataHolder.faces[k][2]) 
							|| (wierzcholek0 == dataHolder.faces[k][2] && wierzcholek1 == dataHolder.faces[k][0]))))
								sasiedzi0.add(k);
				}
			}
			for (Integer a : sasiedzi0) {
				System.out.print(a + " ");
				for (j = 0; j < 3; j++) {	//3 krawedzie w obrebie elementu
					wierzcholek0 =  dataHolder.faces[a][j];
					wierzcholek1 =  dataHolder.faces[a][(j+1)%3];
					for (l = 0; l < dataHolder.faces.length; l++) {
						//czy nie dodajemy tego samego elementu jako drugiego sasiada
						if ((l != i) && (l != a) &&
							(((wierzcholek0 == dataHolder.faces[l][0] && wierzcholek1 == dataHolder.faces[l][1]) 
								|| (wierzcholek0 == dataHolder.faces[l][1] && wierzcholek1 == dataHolder.faces[l][0]))
							|| ((wierzcholek0 == dataHolder.faces[l][1] && wierzcholek1 == dataHolder.faces[l][2]) 
								|| (wierzcholek0 == dataHolder.faces[l][2] && wierzcholek1 == dataHolder.faces[l][1]))
							|| ((wierzcholek0 == dataHolder.faces[l][0] && wierzcholek1 == dataHolder.faces[l][2]) 
								|| (wierzcholek0 == dataHolder.faces[l][2] && wierzcholek1 == dataHolder.faces[l][0]))))
									sasiedzi1.add(l);
					}
				}
			}
			System.out.print("drudzy sasiedzi: ");
			for (Integer a : sasiedzi1) {
				System.out.print(a + " ");
			}
			System.out.println();
		}
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.1.4.zamiana krawedzi dla wskazanej pary przyleglych trojkatow wraz z odpowiednia zmiana struktury danych");
		start = System.nanoTime();
		int wspolnyWierzcholek0, wspolnyWierzcholek1, samotnyWierzcholek0, samotnyWierzcholek1;
		wspolnyWierzcholek0 = -1;
		wspolnyWierzcholek1 = -1;
		element0 = -1;
		element1 = -1;
		samotnyWierzcholek0 = -1;
		samotnyWierzcholek1 = -1;		
		i = 0;
		while (i < dataHolder.faces.length && element0 == -1){
			j = 0;
			while (j < 3 && element0 == -1){	//3 krawedzie w obrebie elementu	
				wierzcholek0 = dataHolder.faces[i][j];
				wierzcholek1 = dataHolder.faces[i][(j+1)%3];
				k = 0;
				while (k < dataHolder.faces.length && element0 == -1){
					if (i != k)
						if (wierzcholek0 == dataHolder.faces[k][0] && wierzcholek1 == dataHolder.faces[k][1]) {
							element0 = i;
							element1 = k;
							wspolnyWierzcholek0 = dataHolder.faces[i][j];
							wspolnyWierzcholek1 = dataHolder.faces[i][(j+1)%3];
							samotnyWierzcholek0 = dataHolder.faces[i][(j+2)%3];
							samotnyWierzcholek1 = dataHolder.faces[k][2];
						} else if (wierzcholek0 == dataHolder.faces[k][1] && wierzcholek1 == dataHolder.faces[k][0]) {
							element0 = i;
							element1 = k;
							wspolnyWierzcholek0 = dataHolder.faces[i][j];
							wspolnyWierzcholek1 = dataHolder.faces[i][(j+1)%3];
							samotnyWierzcholek0 = dataHolder.faces[i][(j+2)%3];
							samotnyWierzcholek1 = dataHolder.faces[k][2];
						} else if (wierzcholek0 == dataHolder.faces[k][1] && wierzcholek1 == dataHolder.faces[k][2]) {
							element0 = i;
							element1 = k;
							wspolnyWierzcholek0 = dataHolder.faces[i][j];
							wspolnyWierzcholek1 = dataHolder.faces[i][(j+1)%3];
							samotnyWierzcholek0 = dataHolder.faces[i][(j+2)%3];
							samotnyWierzcholek1 = dataHolder.faces[k][0];
						} else if (wierzcholek0 == dataHolder.faces[k][2] && wierzcholek1 == dataHolder.faces[k][1]) {
							element0 = i;
							element1 = k;
							wspolnyWierzcholek0 = dataHolder.faces[i][j];
							wspolnyWierzcholek1 = dataHolder.faces[i][(j+1)%3];
							samotnyWierzcholek0 = dataHolder.faces[i][(j+2)%3];
							samotnyWierzcholek1 = dataHolder.faces[k][0];
						} else if (wierzcholek0 == dataHolder.faces[k][0] && wierzcholek1 == dataHolder.faces[k][2]) {
							element0 = i;
							element1 = k;
							wspolnyWierzcholek0 = dataHolder.faces[i][j];
							wspolnyWierzcholek1 = dataHolder.faces[i][(j+1)%3];
							samotnyWierzcholek0 = dataHolder.faces[i][(j+2)%3];
							samotnyWierzcholek1 = dataHolder.faces[k][1];
						} else if (wierzcholek0 == dataHolder.faces[k][2] && wierzcholek1 == dataHolder.faces[k][0]) {
							element0 = i;
							element1 = k;
							wspolnyWierzcholek0 = dataHolder.faces[i][j];
							wspolnyWierzcholek1 = dataHolder.faces[i][(j+1)%3];
							samotnyWierzcholek0 = dataHolder.faces[i][(j+2)%3];
							samotnyWierzcholek1 = dataHolder.faces[k][1];
						}
					k++;
				}
				j++;
			}
			i++;
		}
		
		System.out.println("Element0: " + element0);
		System.out.println("Element1: " + element1);
		System.out.println("Samotny wierzcholek0: " + samotnyWierzcholek0);
		System.out.println("Samotny wierzcholek1: " + samotnyWierzcholek1);
		System.out.println("Wspolny wierzcholek0: " + wspolnyWierzcholek0);
		System.out.println("Wspolny wierzcholek0: " + wspolnyWierzcholek1);
		
		dataHolder.faces[element0][0] = samotnyWierzcholek0;
		dataHolder.faces[element0][1] = samotnyWierzcholek1;
		dataHolder.faces[element0][2] = wspolnyWierzcholek0;
		dataHolder.faces[element1][0] = samotnyWierzcholek0;
		dataHolder.faces[element1][1] = samotnyWierzcholek1;
		dataHolder.faces[element1][2] = wspolnyWierzcholek1;
		
		System.out.println("\nTablica elementow po zamianie krawedzi:");
		for (j = 0; j < dataHolder.faces.length; j++) {
			System.out.println(dataHolder.faces[j][0] + " " + dataHolder.faces[j][1] + " " + dataHolder.faces[j][2]);
		}
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));

		System.out.println("\n3.1.5.okreslenie, czy dana siatka posiada brzeg");
		start = System.nanoTime();
		flag = 0;
		i = 0;
		j = 0;
		k = 0;
		while (i < dataHolder.faces.length && flag == 0){
			while (j < 3 && flag == 0){	//3 krawedzie w obrebie elementu	
				wierzcholek0 =  dataHolder.faces[i][j];
				wierzcholek1 =  dataHolder.faces[i][(j+1)%3];
				while (k < dataHolder.faces.length && flag == 0){
					if (i != k) //czy nie porownujemy z tym samym elementem
					if (!(((wierzcholek0 == dataHolder.faces[k][0] && wierzcholek1 == dataHolder.faces[k][1]) 
							|| (wierzcholek0 == dataHolder.faces[k][1] && wierzcholek1 == dataHolder.faces[k][0]))
						|| ((wierzcholek0 == dataHolder.faces[k][1] && wierzcholek1 == dataHolder.faces[k][2]) 
							|| (wierzcholek0 == dataHolder.faces[k][2] && wierzcholek1 == dataHolder.faces[k][1]))
						|| ((wierzcholek0 == dataHolder.faces[k][0] && wierzcholek1 == dataHolder.faces[k][2]) 
							|| (wierzcholek0 == dataHolder.faces[k][2] && wierzcholek1 == dataHolder.faces[k][0])))){
						flag = 1;
					}
					k++;
				}
				j++;
			}
			i++;
		}
		if (flag==1) System.out.println("Siatka posiada brzeg");
		else System.out.println("Siatka nie posiada brzegu");
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.2. �half edge�/�winged edge�");
		System.out.println("\n3.2.1.dla kazdego wierzcholka wyznaczanie otoczenia wierzcholkow (pierwsza i druga warstwa sasiednich wierzcholkow)");
		start = System.nanoTime();
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.2.2.dla kazdego wierzcholka znalezienie elementow, do ktorych nalezy");
		start = System.nanoTime();
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.2.3.dla kazdego elementu wyznaczenie otoczenia elementow (pierwsza i druga warstwa sasiednich elementow)");
		start = System.nanoTime();
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
		
		System.out.println("\n3.2.4.zamiana krawedzi dla wskazanej pary przyleglych trojkatow wraz z odpowiednia zmiana struktury danych");
		start = System.nanoTime();
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));

		System.out.println("\n3.2.5.okreslenie, czy dana siatka posiada brzeg");
		start = System.nanoTime();
		stop = System.nanoTime();
		System.out.println("Czas wykonania: "+(stop-start));
	}
}