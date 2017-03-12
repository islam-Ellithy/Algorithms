import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ClosestPair {
	Point[] points ;
	Point[] pairs ;
	double minD ;
	private double cloestPair1D(Point[] pX, int n) {

		if (n == 1) {
			return Double.MAX_VALUE;
		}
		if (n == 2) {

			if (dist(pX[0], pX[1]) < minD) {
				pairs[0] = pX[0];
				pairs[1] = pX[1];
				minD = dist(pX[0], pX[1]);
			}
			return minD;
		}

		int mid = n / 2; // this is a vertical line
		Point midPoint = pX[mid];

		Point[] Pxl = new Point[mid + 1]; // x sorted points on left of vertical
											// line
		Point[] Pxr = new Point[n - mid - 1]; // x sorted points on right of
												// vertical line

		int li = 0, ri = 0; // indexes of left and right subarrays
		for (int i = 0; i < n; i++) {
			if (pX[i].x <= midPoint.x && li < Pxl.length) {
				Pxl[li] = pX[i];
				li++;
			} else {
				Pxr[ri] = pX[i];
				ri++;
			}
		}

		// 4- Recursively find the smallest distances in both subarrays.
		sortByXCoordinates(Pxl);
		sortByXCoordinates(Pxr);

		double dl = cloestPair1D(Pxl, mid + 1);
		double dr = cloestPair1D(Pxr, n - mid - 1);

		minD = dl < dr ? dl : dr;

		// 5- create stip[] array that contain points close to minD
		// - Create a vector and add to it only the points in the strip. Sort it
		// by Y-Coordinate.

		Point[] strip = new Point[n];
		int count = 0;
		for (int i = 0; i < n; i++) {
			if (Math.abs(pX[i].x - midPoint.x) < minD) {
				strip[count] = pX[i];
				count++;
			}
		}
		// 6- compute the distance of cloest pair inside the strip and
		// return it if it is < minD
		minD = cloestStripPair(strip, count, minD);
		return minD;
	}

	public static void sortByXCoordinates(Point[] pointsByX) {

		Arrays.sort(pointsByX, new Comparator<Point>() {
			public int compare(Point a, Point b) {
				return Integer.compare(a.x, b.x);
			}
		});
	}

	private double cloestStripPair(Point[] strip, int size, double minD) {
		int i, j;
		for (i = 0; i < size - 1; i++) {// greatest size is 2
			j = i+1;
			if (strip[j].y - strip[i].y < minD)
				if (dist(strip[j], strip[i]) < minD) {
					minD = dist(strip[j], strip[i]);
					pairs[0] = strip[j];
					pairs[1] = strip[i];
					System.out.println("Inside Strip\n" + pairs[0].toString() + "\n" + pairs[1].toString());
				}
		}
		return minD;
	}

	public ClosestPair(Point[] points) {
		pairs = new Point[2];
		this.points = points;
		sortByXCoordinates(this.points);

		pairs[0] = this.points[0];
		pairs[1] = this.points[1];

		System.out.println(pairs[0].toString()+"\t"+pairs[1].toString());
		minD = dist(pairs[0], pairs[1]);
		this.cloestPair1D(points, points.length);
	}
	
	/*
	 * private double dist(Point p1, Point p2) { if ((p1.y - p2.y) != 0 && (p1.x
	 * - p2.x) != 0) return Math.abs((p1.x - p2.x)) Math.sqrt(1 +
	 * Math.pow((double) (p1.y - p2.y) / (double) (p1.x - p2.x), 2)); return
	 * Math.sqrt(Math.pow(p1.y - p2.y, 2) + Math.pow(p1.x - p2.x, 2)); }
	 */

	public double dist(Point p, Point q) {
		return Math.sqrt((double) Math.abs((p.x - q.x) * (p.x - q.x)));
	}

	public void print() {
		System.out.println("two pairs are \n" + pairs[0].getX() + " - " + pairs[0].getY() + "\n" + pairs[1].getX()
				+ " - " + pairs[1].getY() + "\n" + this.minD);
	}

	public static void Write(String path, int len) {
		Random randomGen = new Random();
		FileWriter file;

		try {
			file = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(file);
			bw.write(len + "\n");
			for (int i = 0; i < len; i++) {
				bw.write(randomGen.nextInt(len) + "\n");
			}

			bw.close();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Point[] Read(String path) {
		Scanner in;
		int sz = -1;
		Point[] points = null;
		try {
			in = new Scanner(new File(path));
			sz = in.nextInt();
			points = new Point[sz];
			int i = 0;
			int x ;
			while (in.hasNextInt()) {
				x = in.nextInt();
				points[i++] = new Point(x, 0);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return points;
	}

	public static void main(String[] args) {
		// ClosestPair.Write("input" + 2 + ".txt", 10);
		for (int j = 2; j <= 2; j++) {
			Point[] pX = ClosestPair.Read("input" + j + ".txt");
			ClosestPair cp = new ClosestPair(pX);
			cp.print();
		}
	}
}
