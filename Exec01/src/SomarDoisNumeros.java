import java.util.Scanner;

class SomarDoisNumeros {
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String args[]) {
		int num1, num2, sum;
		
		System.out.println("Digite o primeiro número");
		num1 = sc.nextInt();
		System.out.println("Digite o segundo número");
		num2 = sc.nextInt();
		
		sum = num1 + num2;
		
		System.out.println("A soma é: " + sum);
	}
}