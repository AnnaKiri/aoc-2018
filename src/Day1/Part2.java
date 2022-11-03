package Day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Part2 {

	public static void main(String[] args) {
		String path = ".\\src\\Day1\\data.txt";
		File file = new File(path);
		
		try {
			Scanner scan = new Scanner(file);
			List<Integer> frequency = new ArrayList<>();
			
			while (scan.hasNextInt()) {
				frequency.add(scan.nextInt());
			}
			
			scan.close();

			System.out.println(fineDoubleFreq(frequency));
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}	

	}
	
	public static int fineDoubleFreq(List<Integer> frequency) {
		int [] array = new int [1000000];  // создали массив для  записи и счета частоты, пока это пустой массив длиной в 1000000 элементов
		
		int result = 0;
		while (true) {						// while (true) что бы цикл for крутился бесконечно
			for (int i = 0; i < frequency.size(); i++) {				
				result += frequency.get(i);		// считаем частоту на каждом шаге
				int index = result + 100000;	// получившуюся частоту мы рассматриваем как индекс элемента массива, но если частота будет отрицательная, то мы не сможем обратится к элементу массива с таким индексом, поэтому делаем + 100000, что бы индекс стал положительным числом, а положительные индексы будут начинаться с 100000, 100001 и т.д.
				if (array[index] == 1) {		//  1 - это счетчик нашей частоты, проверяем чему равер наш элемент массива, если он равен 1, значит такую частоту мы уже встречали
					return result;				// возвращаем найденную частоту
				}
				array[index] = array[index] + 1;	// обращаемся к элементу массива под индексом, котрый определили в строке 40 и присваиваем ему 1, 1 - это счетчик нашей частоты, если при дальнейшем поиске вычислится такая же частота, то мы увидим, что у нее уже счетчик равен 1, значит мы встретили ее второй раз
				
			}
		}
	}

}
