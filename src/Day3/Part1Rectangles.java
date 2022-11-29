package Day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Part1Rectangles {
	public static void main(String[] args) throws IOException {
		String path = ".\\src\\Day3\\data.txt";
		
		final String[] arr = Files.readString(Path.of(path)).lines().toArray(String[]::new);	

		List<Rectangles> rectangles = new ArrayList<>();

		Map<String, Integer> map = new HashMap<>();
		for (String s : arr) {
		    final String regex = "^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$";
		    final var id = Integer.parseInt(s.replaceAll(regex, "$1"));
		    final var x = Integer.parseInt(s.replaceAll(regex, "$2"));
		    final var y = Integer.parseInt(s.replaceAll(regex, "$3"));
		    final var width = Integer.parseInt(s.replaceAll(regex, "$4"));
		    final var height = Integer.parseInt(s.replaceAll(regex, "$5"));
		    
		    rectangles.add(new Rectangles(id, x, y, width, height));
		}
		
		for (Rectangles value : rectangles) {
			for (int newY = value.getY(); newY < value.getHeight() + value.getY(); newY++) {
		    	for (int newX = value.getX(); newX < value.getWidth() + value.getX(); newX++) {
					String coord = newY + ";" + newX;
					
					if (!map.containsKey(coord)) {
						map.put(coord, 1);
					} else {
						map.put(coord, 2);
					}	
				}
			} 
		}
		
		int counter = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() == 2) {
				counter++;
			}
		}
		
		System.out.println(counter);
	}
}

