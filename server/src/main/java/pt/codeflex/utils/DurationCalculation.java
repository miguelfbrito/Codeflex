package pt.codeflex.utils;

import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.codeflex.dto.DateStatus;

public class DurationCalculation {

	// Calculates time without overlapping
	public static long calculateDuration(List<DateStatus> dates) {

		long totalMilliseconds = 0;

		Collections.sort(dates, new Comparator<DateStatus>() {
			@Override
			public int compare(DateStatus arg0, DateStatus arg1) {
				return Long.compare(arg0.getDate(), arg1.getDate());
			}
		});
		
		System.out.println("");
		for(DateStatus d : dates) {
			System.out.println(d.getDate() + " - " + d.isOpeningDate());
		}
		System.out.println("");

		long openingDate = dates.get(0).getDate();
		int openedProblems = 1;

		for (int i = 1; i < dates.size(); i++) {
			DateStatus d = dates.get(i);

			if (!d.isOpeningDate()) {
				openedProblems--;
				if (openedProblems == 0) {
					totalMilliseconds += d.getDate() - openingDate;
				}
			} else {
				if (openedProblems == 0) {
					openingDate = d.getDate();
				}
				openedProblems++;
			}

		}

		System.out.println();
		return totalMilliseconds;
	}

	public static long getMillisecondsBetweenDate(Date d1, Date d2) {
		return d2.getTime() - d1.getTime();
	}

}
