import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TEMPobsplace {
	public static ArrayList<Vector> getObstacles() {
		Scanner s = null;
		try {
			s = new Scanner(new File("TEMPobsplace.txt"));
		}
		catch (FileNotFoundException fnfe) {}

		ArrayList<Vector> arr = new ArrayList<>();

		if (s != null) {
			while (s.hasNextFloat()) {
				float x = s.nextFloat();
				float y = s.nextFloat();
				arr.add( new Vector(x, y) );
			}
		}

		return arr;
	}

	public static void main(String[] args) {
		for (Vector v : getObstacles())
			System.out.println(v);
	}
}
