//Kasper Rosenberg karo5568 

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Graph {
	private List<Station> stations = new ArrayList<>();
	private List<Edge> route = new ArrayList<>();
	private Queue<Edge> queue = new PriorityQueue<>();
	private int currentTime = 0;
	private Scanner input = new Scanner(System.in);
	public Graph() {
	}

	private class Station {
		String name;
		double x;
		double y;
		boolean checked;
		List<Edge> departures = new ArrayList<>();

		public Station(String name, double x, double y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}

		public void setChecked() {
			checked = true;
		}

		public boolean equals(Station other) {
			if (other instanceof Station) {
				Station otherStation = (Station) other;
				return name.equals(otherStation.name) && x == otherStation.x && y == otherStation.y;
			} else {
				return false;
			}
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private class Edge implements Comparable<Edge> {
		Station from;
		Station to;
		int timeCost;
		int departureTime;
		double totalTime;

		public Edge(Station from, Station to, int timeCost, int departureTime) {
			this.from = from;
			this.to = to;
			this.timeCost = timeCost;
			this.departureTime = departureTime;
		}

		public void setTotalTime(double time) {
			totalTime = time;
		}

		public int compareTo(Edge other) {
			if (totalTime < other.totalTime) {
				return -1;
			} else if (totalTime > other.totalTime) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private double heuristic(Station from, Station to) {
		double x = from.x - to.x;
		double y = from.y - to.y;
		return Math.sqrt((x * x) + (y * y));
	}

	/**
	 * This method takes two station nodes as parameters and finds the fastest route
	 * between them. Throws a NullPointerException if there is no such station.
	 * The method uses heuristic()-method to obtain A* by considering if edges leads to stations
	 * closer or further away from the destination in it's calculation for optimal route.
	 * @param from : This is the Node (station) from which you want to start the journey.
	 * @param to   : This is the Node of your desired destination.
	 * @return This method returns a List<EDGE> of all the edges (departures) required to
	 *         reach the destination by the fastest route. Showing what departures to take from what stations.
	 *         
	 */

	private List<Edge> findRoute(Station from, Station to) {
		try {
			while (!from.equals(to)) {
				for (Edge e : from.departures) {
					if (e.departureTime >= currentTime && !e.from.checked && e.departureTime < currentTime + 60) {
						double waitingTime = e.departureTime - currentTime;
						double heuristicTime = heuristic(e.to, to);						
						e.setTotalTime(waitingTime + e.timeCost + heuristicTime);
						queue.add(e);
					}
				}
				Edge edge = queue.poll();
				edge.from.setChecked();
				currentTime += (edge.departureTime - currentTime) + edge.timeCost;
				from = edge.to;
				route.add(edge);
				queue.clear();
			}
		} catch (NullPointerException e) {
			System.out.println("Det finns ingen station med det namnet");
		}
		return route;
	}
	private void connect(Station from, Station to, int timeCost, int interval) {
		if (!isConnected(from, to)) {
			for (int i = 0; i < 1440; i += interval) {
				Edge a = new Edge(from, to, timeCost, i);
				from.departures.add(a);
				Edge b = new Edge(to, from, timeCost, i);
				to.departures.add(b);
			}
		}
	}

	private boolean isConnected(Station station1, Station station2) {
		for (Edge e : station1.departures) {
			if (e.to.equals(station2)) {
				return true;
			}
		}
		return false;
	}

	private void createStations() {
		Station kristineberg = new Station("Kristineberg", -56, 1);
		Station thorildsplan = new Station("Thorildsplan", -45, -1);
		Station fridhemsplan = new Station("Fridhemsplan", -34, 0);
		Station stEriksplan = new Station("St Eriksplan", -22, 11);
		Station odenplan = new Station("Odenplan", -14, 18);
		Station r�dmansgatan = new Station("R�dmansgatan", -5, 15);
		Station h�torget = new Station("H�torget", 0, 7);
		Station tCentralen = new Station("T-Centralen", 0, 0);
		Station gamlaStan = new Station("Gamla stan", 10, -15);
		Station slussen = new Station("Slussen", 8, -25);
		Station huvudsta = new Station("Huvudsta", -73, 23);
		Station v�straSkogen = new Station("V�stra skogen", -55, 20);
		Station stadshagen = new Station("Stadshagen", -42, 7);
		Station r�dhuset = new Station("R�dhuset", -17, 0);
		Station solnaCentrum = new Station("Solna centrum", -61, 43);
		Station kungstr�dg�rden = new Station("Kungstr�dg�rden", 13, -3);
		Station �stermalmstorg = new Station("�stermalmstorg", 13, 6);

		Station alvik = new Station("Alvik", -77, 0);
		Station alvikStrand = new Station("Alviks strand", -75, -21);
		Station storaEssingen = new Station("Stora Essingen", -68, -34);
		Station gr�ndal = new Station("Gr�ndal", -59, -41);
		Station trekanten = new Station("Trekanten", -45, -44);
		Station liljeholmen = new Station("Liljeholmen", -37, -47);
		Station hornstull = new Station("Hornstull", -24, -34);
		Station zinkensdam = new Station("Zinkensdam", -13, -28);
		Station mariatorget = new Station("Mariatorget", 0, -29);

		connect(alvik, kristineberg, 3, 30);
		connect(alvik, alvikStrand, 3, 30);
		connect(alvikStrand, storaEssingen, 3, 30);
		connect(storaEssingen, gr�ndal, 3, 30);
		connect(gr�ndal, trekanten, 3, 30);
		connect(trekanten, liljeholmen, 3, 30);
		connect(liljeholmen, hornstull, 3, 30);
		connect(hornstull, zinkensdam, 3, 30);
		connect(zinkensdam, mariatorget, 3, 30);
		connect(mariatorget, slussen, 3, 30);

		connect(kristineberg, thorildsplan, 3, 30);
		connect(thorildsplan, fridhemsplan, 3, 30);
		connect(fridhemsplan, stEriksplan, 3, 30);
		connect(stEriksplan, odenplan, 3, 30);
		connect(odenplan, r�dmansgatan, 3, 30);
		connect(r�dmansgatan, h�torget, 3, 30);
		connect(h�torget, tCentralen, 3, 30);
		connect(tCentralen, gamlaStan, 3, 30);
		connect(gamlaStan, slussen, 3, 30);
		connect(huvudsta, v�straSkogen, 3, 30);
		connect(solnaCentrum, v�straSkogen, 3, 30);
		connect(v�straSkogen, stadshagen, 3, 30);
		connect(stadshagen, fridhemsplan, 3, 30);
		connect(fridhemsplan, r�dhuset, 3, 30);
		connect(r�dhuset, tCentralen, 3, 30);
		connect(tCentralen, kungstr�dg�rden, 3, 30);
		connect(tCentralen, �stermalmstorg, 3, 30);

		stations.add(kristineberg);
		stations.add(thorildsplan);
		stations.add(fridhemsplan);
		stations.add(stEriksplan);
		stations.add(odenplan);
		stations.add(r�dmansgatan);
		stations.add(h�torget);
		stations.add(tCentralen);
		stations.add(gamlaStan);
		stations.add(slussen);
		stations.add(huvudsta);
		stations.add(v�straSkogen);
		stations.add(solnaCentrum);
		stations.add(stadshagen);
		stations.add(r�dhuset);
		stations.add(kungstr�dg�rden);
		stations.add(�stermalmstorg);
		stations.add(alvik);
		stations.add(alvikStrand);
		stations.add(storaEssingen);
		stations.add(gr�ndal);
		stations.add(trekanten);
		stations.add(liljeholmen);
		stations.add(hornstull);
		stations.add(zinkensdam);
		stations.add(mariatorget);
	}

	private Station findStation(String name) {
		for (Station s : stations) {
			if (s.name.equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	public static void main(String[] args) {

		Graph sl = new Graph();
		sl.createStations();

		for (Station s : sl.stations) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("Varifr�n vill du �ka?");
		String from = sl.input.nextLine();
		System.out.println("Var vill du �ka?");
		String to = sl.input.nextLine();
		System.out.println("N�r vill du �ka?\nAnge tid mellan 0-1440:");
		sl.currentTime = sl.input.nextInt();
		System.out.println();

		Station position = sl.findStation(from);
		Station destination = sl.findStation(to);

		sl.findRoute(position, destination);

		int arriveTime = 0;
		int departureTime = 0;
		for (Edge e : sl.route) {
			if (sl.route.indexOf(e) == 0) {
				arriveTime += e.departureTime;
				departureTime = e.departureTime;
				System.out.println(e.from + " : departure at " + arriveTime);
				arriveTime += e.timeCost;
			} else if (sl.route.indexOf(e) == (sl.route.size() - 1)) {
				System.out.println(e.from + " : " + arriveTime);
				arriveTime += e.timeCost;
				int travelTime = arriveTime - departureTime;
				System.out.println(e.to + " : arriving at " + arriveTime);
				System.out.println();
				System.out.println("Travel time = " + travelTime + " minute");
			} else {
				System.out.println(e.from + " : " + arriveTime);
				arriveTime += e.timeCost;
			}
		}
	}
}