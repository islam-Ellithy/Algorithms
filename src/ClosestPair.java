import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

public class ClosestPair {

	Point[] points;
	Point pairs[];

	private double cloestPair(Point[] pX, Point[] pY, int n) {

		if (n == 1) {
			return Double.MAX_VALUE;
		}
		if (n == 2) {
			pairs[0] = pY[0];
			pairs[1] = pY[1];
			return dist(pairs[0], pairs[1]);
		}

		int mid = n / 2; // this is a vertical line
		Point midPoint = pX[mid];

		Point[] Pyl = new Point[mid + 1]; // y sorted points on left of vertical
											// line
		Point[] Pyr = new Point[n - mid - 1]; // y sorted points on right of
												// vertical line
		Point[] Pxl = new Point[mid + 1]; // x sorted points on left of vertical
											// line
		Point[] Pxr = new Point[n - mid - 1]; // x sorted points on right of
												// vertical line
		int li = 0, ri = 0; // indexes of left and right subarrays
		for (int i = 0; i < n; i++) {
			if (pY[i].x <= midPoint.x && li < Pyl.length) {
				Pyl[li] = Pxl[li] = pY[i];
				li++;
			} else {
				Pyr[ri] = Pxr[ri] = pY[i];
				ri++;
			}
		}

		// 4- Recursively find the smallest distances in both subarrays.
		sortByXCoordinates(Pxl);
		sortByXCoordinates(Pxr);

		double dl = cloestPair(Pxl, Pyl, mid + 1);
		double dr = cloestPair(Pxr, Pyr, n - mid - 1);

		double minD = dl < dr ? dl : dr;

		// 5- create stip[] array that contain points close to minD
		// - Create a vector and add to it only the points in the strip. Sort it
		// by Y-Coordinate.

		Point[] strip = new Point[n];
		int count = 0;
		for (int i = 0; i < n; i++) {
			if (Math.abs(pY[i].x - midPoint.x) < minD) {
				strip[count] = pY[i];
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

	public static void sortByYCoordinates(Point[] pointsByY) {

		Arrays.sort(pointsByY, new Comparator<Point>() {
			public int compare(Point a, Point b) {
				return Integer.compare(a.y, b.y);
			}
		});
	}

	private double cloestStripPair(Point[] strip, int size, double minD) {
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; (j < size) && strip[j] != null && (strip[j].y - strip[i].y < minD); j++) {
				if (dist(strip[j], strip[i]) < minD) {
					minD = dist(strip[j], strip[i]);
					pairs[0] = strip[j];
					pairs[1] = strip[i];
				}
			}
		}
		return minD;
	}

	public ClosestPair() {
		pairs = new Point[2];
	}

	private  double dist(Point p1, Point p2) {
		if((p1.y - p2.y)!=0 && (p1.x - p2.x)!=0)
			return Math.abs((p1.x - p2.x))* Math.sqrt(1+Math.pow((double)(p1.y - p2.y)/(double)(p1.x - p2.x),2));
		 return Math.sqrt(Math.pow(p1.y-p2.y, 2)+Math.pow(p1.x-p2.x, 2));
	}
/*
	public double dist(Point p, Point q) {
		return Math.sqrt((double) (p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y));
	}
*/
	public void print() {
		System.out.println("two pairs are \n" + pairs[0].getX() +" - "+ pairs[0].getY() + 
				"\n" + pairs[1].getX() +" - "+ pairs[1].getY()+"\n");
	}

	public int Read(String path) {
		Scanner in;
		int sz = -1;

		try {
			in = new Scanner(new File(path));

			sz = in.nextInt();
			points = new Point[sz];
			int i = 0;
			int x, y;
			while (in.hasNextInt()) {
				x = in.nextInt();
				y = in.nextInt();
				points[i++] = new Point(x, y);
			}

			in.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return sz;
	}

	public static void main(String[] args) {

		for (int j = 1; j <= 4; j++) {

			ClosestPair cp = new ClosestPair();
			int n = cp.Read("input" + j + ".txt");
			Point[] pX = new Point[n];
			Point[] pY = new Point[n];
			for (int i = 0; i < n; i++) {
				pX[i] = pY[i] = cp.points[i];
			}
			sortByXCoordinates(pX);
			sortByYCoordinates(pY);
			double distance = cp.cloestPair(pX, pY, n);
			cp.print();
			System.out.println(distance);
		}
	}

}
