package Day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part2 {
	public static void main(String[] args) throws IOException {
		String path = ".\\src\\Day4\\data.txt";
		
		final String[] arr = Files.readString(Path.of(path)).lines().toArray(String[]::new);
		
		Set<LogEntry> set = new TreeSet<>();
		for (String str : arr) {
			Matcher matcherDateTime = Pattern.compile("\\[(\\d+)\\-(\\d+)\\-(\\d+) (\\d+):(\\d+)\\]").matcher(str);
			
			if (!matcherDateTime.find()) {
				System.out.println("Error!");
				return;
			}
			int year = Integer.parseInt(matcherDateTime.group(1));
			int month = Integer.parseInt(matcherDateTime.group(2));
			int day = Integer.parseInt(matcherDateTime.group(3));
			int hour = Integer.parseInt(matcherDateTime.group(4));
			int minute = Integer.parseInt(matcherDateTime.group(5));
			
			Action action = null;
			
			Matcher matcherID = Pattern.compile("Guard \\#(\\d+) begins shift").matcher(str);
			int id = 0;
			if (matcherID.find()) {
				id = Integer.parseInt(matcherID.group(1));
				action = Action.begins_shift;
			}

			Matcher matcherSleep = Pattern.compile("falls asleep").matcher(str);
			Matcher matcherWakeaUp = Pattern.compile("wakes up").matcher(str);
			
			if (matcherSleep.find()) {
				action = Action.falls_asleep;
			} else if (matcherWakeaUp.find()) {
				action = Action.wakes_up;
			}
			
			LogEntry log = new LogEntry(year, month, day, hour, minute, id, action);
			set.add(log);			
			
		}
				
		int id = -1;
		int startSleeping = -1;
		int wakeUp = -1;
		Map<Integer, int[]> map = new HashMap<>();
		
		for (LogEntry logEntry : set) {
			
			if (logEntry.getId() != 0) {
				id = logEntry.getId();
			}
			
			if (logEntry.getAction().equals(Action.falls_asleep)) {
				startSleeping = logEntry.getMinute();
			}
			
			if (logEntry.getAction().equals(Action.wakes_up)) {
				wakeUp = logEntry.getMinute();
				if (!map.containsKey(id)) {
					map.put(id, new int[60]);
				}
				
				for (int i = startSleeping; i < wakeUp; i++) {
					map.get(id)[i] += 1;
				}
			}
		}
		
		int maxMinute = Integer.MIN_VALUE;
		int minuteIndex = 0;
		for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
			for (int i = 0; i < entry.getValue().length; i++) {
				if(entry.getValue()[i] > maxMinute) {
					maxMinute = entry.getValue()[i];
					minuteIndex = i;
					id = entry.getKey();
				}
			}	
		}
				
		System.out.println(id * minuteIndex);
		
	}
}

