package Day4;

import java.util.Objects;

public class LogEntry implements Comparable<LogEntry> {
	
	int year;
	int month;
	int day;
	int hour;
	int minute;
	int id;
	Action action;
	
	public LogEntry(int year, int month, int day, int hour, int minute, int id, Action action) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.id = id;
		this.action = action;
	}

	@Override
	public String toString() {
		return "[" + year + "-" + month + "-" + day + " " + hour + ":" + minute
				+ "] " + id + " " + action;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, day, hour, id, minute, month, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogEntry other = (LogEntry) obj;
		return action == other.action && day == other.day && hour == other.hour && id == other.id
				&& minute == other.minute && month == other.month && year == other.year;
	}

	public int getMinute() {
		return minute;
	}

	public int getId() {
		return id;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public int compareTo(LogEntry o) {
		long thisMinutes = this.daysAmountFromYear() * 24 * 60 + this.day * 24 * 60 + this.hour * 60 + this.minute;
		long oMinutes = o.daysAmountFromYear() * 24 * 60 + o.day * 24 * 60 + o.hour * 60 + o.minute;
		return (int) (thisMinutes - oMinutes);
	}
	
	public int daysAmountFromYear() {
		int sum = 0;
		for (int i = 1; i < this.month; i++) {
			sum += daysAmountInMonth(i);
		}
		return sum;
	}
	
	public static int daysAmountInMonth(int i) {
		int days = 0;
		switch (i) {
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			days = 28;
			break;
		default:
			days = 31;
		}
		return days;
	}
}
