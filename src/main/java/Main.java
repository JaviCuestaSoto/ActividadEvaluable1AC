import conexion.GestorDB;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestorDB gestorDB = new GestorDB();
        gestorDB.llamarConexion();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("--- BIENVENIDO AL MENÚ DEL SISTEMA DE GESTIÓN DE DATOS ---");
            System.out.println("1. Agregar un empleado.");
            System.out.println("2. Agregar un pedido.");
            System.out.println("3. Mostrar empleados.");
            System.out.println("4. Mostrar productos.");
            System.out.println("5. Mostrar pedidos.");
            System.out.println("6. Mostrar productos inferiores a 600 €.");
            System.out.println("7. Insertar los productos favoritos que tengan un valor superior a 1.000 €.");
            System.out.println("8. Salir.");
            System.out.print("Seleccione la opción deseada: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    gestorDB.agregarEmpleado();
                    break;
                case 2:
                    gestorDB.agregarPedido();
                    break;
                case 3:
                    gestorDB.mostrarEmpleados();
                    break;
                case 4:
                    gestorDB.mostrarProductos();
                    break;
                case 5:
                    gestorDB.mostrarPedidos();
                    break;
                case 6:
                    gestorDB.mostrarProductosBaratos();
                    break;
                case 7:
                    gestorDB.ProductosFavoritos();
                    break;
                case 8:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
            }

        } while (opcion != 8);
        scanner.close();
    }
}
