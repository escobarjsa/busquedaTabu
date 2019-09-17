/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package busqueda.tabu;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author pc
 */
public class BusquedaTabu {

    static int tabu = 3;
    static int soluciones = 3;
    static int iteraciones = 50;
    static int[] solucionActual = {0, 1, 0, 1, 1};
    static int[] mejorSolucion = {0, 1, 0, 1, 1};
    static int[][] solucionesAuxiliares = new int[soluciones][8];
    static int[] solucionAxiliar = new int[soluciones];
    static int[] listaTabu = {5, 5, 5, 5, 5}; // Solución actual
    static int[] costos = new int[soluciones];
    static int[] costosOrdenados = new int[soluciones];
    static int costoActual = 20 * solucionActual[0] + 25 * solucionActual[1] - 30 * solucionActual[2] - 45 * solucionActual[3] + 40 * solucionActual[4];
    static int mejorCosto = costoActual;
    static int[] posiciones = new int[soluciones];
    static ArrayList<String> mejoresCostos = new ArrayList<String>();
    static ArrayList<String> solucionesActuales = new ArrayList<String>();

    public static void main(String[] args) {

        costoActual += validarRestricciones(0, 1, 0, 1, 1);
        mejorCosto = costoActual;

        for (int i = 0; i < iteraciones; i++) {
            for (int j = 0; j < soluciones; j++) {
                solucionAxiliar = Arrays.copyOf(solucionActual, solucionActual.length);
                do {
                    int aux = obtenerNumeroAleatorio();
                    if (solucionAxiliar[aux] == 1) {
                        solucionAxiliar[aux] = 0;
                        break;
                    }
                } while (true);
                do {
                    posiciones[j] = obtenerNumeroAleatorio();
                    if (solucionAxiliar[posiciones[j]] == 0) {
                        solucionAxiliar[posiciones[j]] = 1;
                        break;
                    }
                } while (true);

                solucionesAuxiliares[j] = Arrays.copyOf(solucionAxiliar, solucionAxiliar.length);;
                costos[j] = 20 * solucionAxiliar[0] + 25 * solucionAxiliar[1] - 30 * solucionAxiliar[2] - 45 * solucionAxiliar[3] + 40 * solucionAxiliar[4];
                costos[j] += validarRestricciones(solucionAxiliar[0], solucionAxiliar[1], solucionAxiliar[2], solucionAxiliar[3], solucionAxiliar[4]);
            }

            costosOrdenados = ordenarCostos(costos);
            boolean k = false;
            int con = 0, con1 = 0;
            int menor = costosOrdenados[0];

            do {
                if (costos[con] == menor && costos[con] <= mejorCosto) {
                    solucionActual = Arrays.copyOf(solucionesAuxiliares[con], solucionesAuxiliares[con].length);
                    listaTabu[2] = listaTabu[1];
                    listaTabu[1] = listaTabu[0];
                    listaTabu[0] = posiciones[con];
                    costoActual = costos[con];
                    mejorCosto = costoActual;
                    mejorSolucion = Arrays.copyOf(solucionesAuxiliares[con], solucionesAuxiliares[con].length);
                    mejoresCostos.add(String.valueOf(i + 1) + " " + String.valueOf(mejorCosto));
                    k = true;
                } else if (costos[con] == menor && costos[con] > mejorCosto && buscarLista(costos[con]) == 0) {
                    solucionActual = Arrays.copyOf(solucionesAuxiliares[con], solucionesAuxiliares[con].length);
                    listaTabu[2] = listaTabu[1];
                    listaTabu[1] = listaTabu[0];
                    listaTabu[0] = posiciones[con];
                    costoActual = costos[con];
                    solucionesActuales.add(String.valueOf(i + 1) + " " + String.valueOf(costoActual));
                    k = true;
                } else if (costos[con] == menor && costos[con] > mejorCosto && buscarLista(costos[con]) == 1) {
                    if (con == 2) {
                        con1++;
                        menor = costosOrdenados[con1];
                        con = (-1);
                    }
                    if (con1 == 4) {
                        k = true;
                    }
                }
                con++;
            } while (k != true);
        }
        System.out.print("\nMejor Costo:" + mejorCosto + "\nMejor Solucion: \n");

        for (int i = 0; i < 5; i++) {
            System.out.print("x" + (i + 1) + "=" + mejorSolucion[i] + ", ");
        }
        imprimirDatos();
    }

    public static int validarRestricciones(int x1, int x2, int x3, int x4, int x5) {
        int suma = 0;
        if (x1 + x2 - x3 + x4 + x5 < 1) {
            suma += 100;
        }
        if (x1 + x2 - x4 + 2 * x5 < 2) {
            suma += 100;
        }
        if (-x2 + x4 + x5 > 1) {
            suma += 100;
        }
        if (x2 + x3 + x5 > 2) {
            suma += 100;
        }
        return suma;
    }

    static int[] ordenarCostos(int costos[]) {
        int auxCosto[] = Arrays.copyOf(costos, costos.length);
        Arrays.sort(auxCosto);
        return auxCosto;
    }

    static int buscarLista(int posicion) {
        for (int i = 0; i < tabu; i++) {
            if (listaTabu[i] == posicion) {
                return 1;
            }
        }
        return 0;
    }

    static int obtenerNumeroAleatorio() {
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            return number.nextInt(5);
        } catch (NoSuchAlgorithmException ex) {
            System.out.print("Error");
            return 0;
        }
    }

    static void imprimirDatos() {
        System.out.print("\n\nDatos de mejores Costos \n\n");
        for (int i = 0; i < mejoresCostos.size(); i++) {
            System.out.print(mejoresCostos.get(i) + "\n");
        }
        System.out.print("\n\n Datos de Soluciones Actuales \n\n");
        for (int i = 0; i < solucionesActuales.size(); i++) {
            System.out.print(solucionesActuales.get(i) + "\n");
        }
    }
}
