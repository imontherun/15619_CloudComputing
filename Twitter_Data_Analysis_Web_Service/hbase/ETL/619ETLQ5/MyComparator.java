import java.util.Comparator;

public class MyComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			Item s1 = (Item) o1;
			Item s2 = (Item) o2;
			if ((s1.count - s2.count) != 0)
				return s2.count - s1.count;
			else if (s1.firsttweet.compareTo(s2.firsttweet) != 0)
				return s1.firsttweet.compareTo(s2.firsttweet);
			else if ((s1.indices - s2.indices) != 0)
				return s1.indices - s2.indices;
			else
				return s1.indices2 - s2.indices2;
		}

		public boolean equals(Object o) {
			return false;
		}
	}